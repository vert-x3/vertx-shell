/*
 * Copyright 2015 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 *
 *
 * Copyright (c) 2015 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 *
 */

package io.vertx.ext.shell;

import io.termd.core.tty.TtyEvent;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.base.Sleep;
import io.vertx.ext.shell.command.CommandRegistry;
import io.vertx.ext.shell.system.impl.InternalCommandManager;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.shell.system.ExecStatus;
import io.vertx.ext.shell.impl.ShellSession;
import io.vertx.ext.shell.support.TestTtyConnection;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class TtyAdapterTest {

  Vertx vertx;
  CommandRegistry registry;
  InternalCommandManager manager;
  ShellServer server;

  @Before
  public void before() {
    vertx = Vertx.vertx();
    registry = CommandRegistry.getShared(vertx);
    server = ShellServer.create(vertx).registerCommandResolver(registry);
    manager = new InternalCommandManager(registry);
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testVertx(TestContext context) {
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, conn, manager);
    shell.init().readLine();
    Async registrationLatch = context.async();
    Async done = context.async();
    registry.registerCommand(CommandBuilder.command("foo").processHandler(process -> {
      context.assertEquals(vertx, process.vertx());
      done.complete();
    }).build(vertx), context.asyncAssertSuccess(reg -> {
      registrationLatch.complete();
    }));
    registrationLatch.awaitSuccess(10000);
    conn.read("foo\r");
  }

  @Test
  public void testExecuteProcess(TestContext context) {
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, conn, manager);
    shell.init().readLine();
    context.assertNull(shell.foregroundJob());
    context.assertEquals(Collections.emptySet(), shell.jobs());
    Async registrationLatch = context.async();
    Async async = context.async();
    registry.registerCommand(CommandBuilder.command("foo").processHandler(process -> {
      context.assertEquals(1, shell.jobs().size());
      Job job = shell.getJob(1);
      context.assertEquals(job, shell.foregroundJob());
      context.assertEquals("foo", job.line());
      context.assertEquals(ExecStatus.RUNNING, job.status());
      async.complete();
    }).build(vertx), reg -> {
      registrationLatch.complete();
    });
    registrationLatch.awaitSuccess(10000);
    conn.read("foo\r");
  }

  @Test
  public void testHandleReadlineBuffered(TestContext context) {
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, conn, manager);
    shell.init().readLine();
    Async registrationLatch = context.async(2);
    Async async = context.async();
    registry.registerCommand(CommandBuilder.command("_not_consumed").processHandler(process -> {
      async.complete();
    }).build(vertx), reg -> {
      registrationLatch.complete();
    });
    registry.registerCommand(CommandBuilder.command("read").processHandler(process -> {
      StringBuilder buffer = new StringBuilder();
      process.setStdin(line -> {
        buffer.append(line);
        if (buffer.toString().equals("the_line")) {
          process.end();
        }
      });
    }).build(vertx), context.asyncAssertSuccess(v -> {
      registrationLatch.complete();
    }));
    registrationLatch.awaitSuccess(10000);
    conn.read("read\rthe_line_not_consumed\r");
  }

  @Test
  public void testExecuteReadlineBuffered(TestContext context) {
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, conn, manager);
    shell.init().readLine();
    Async registrationLatch = context.async();
    Async async = context.async();
    AtomicInteger count = new AtomicInteger();
    registry.registerCommand(CommandBuilder.command("read").processHandler(process -> {
      if (count.incrementAndGet() == 2) {
        async.complete();
      }
      process.end(0);
    }).build(vertx), context.asyncAssertSuccess(v -> {
      registrationLatch.complete();
    }));
    registrationLatch.awaitSuccess(10000);
    conn.read("read\rread\r");
  }

  @Test
  public void testSuspendProcess(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, conn, manager);
    shell.init().readLine();
    Async done = context.async();
    Async registrationLatch = context.async();
    Async latch2 = context.async();
    registry.registerCommand(CommandBuilder.command("foo").processHandler(process -> {
      Job job = shell.getJob(1);
      process.suspendHandler(v -> {
        context.assertEquals(ExecStatus.STOPPED, job.status());
        context.assertNull(shell.foregroundJob());
        done.complete();
      });
      latch2.complete();
    }).build(vertx), reg -> {
      registrationLatch.complete();
    });
    registrationLatch.awaitSuccess();
    conn.read("foo\r");
    latch2.awaitSuccess(10000);
    conn.sendEvent(TtyEvent.SUSP);
  }

  @Test
  public void testSuspendedProcessDisconnectedFromTty(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, conn, manager);
    shell.init().readLine();
    Async registrationsLatch = context.async(3);
    Async done = context.async();
    Async latch1 = context.async();;
    Async latch2 = context.async();;
    Async latch3 = context.async();;
    registry.registerCommand(CommandBuilder.command("read").processHandler(process -> {
      process.setStdin(line -> {
        context.fail("Should not process line " + line);
      });
      process.suspendHandler(v -> {
        context.assertNull(process.stdout());
        latch2.complete();
      });
      latch1.complete();
    }).build(vertx), ar -> {
      registrationsLatch.complete();
    });
    registry.registerCommand(CommandBuilder.command("wait").processHandler(process -> {
      // Do nothing, this command is used to escape from readline and make
      // sure that the read data is not sent to the stopped command
      latch3.complete();
      process.suspendHandler(v -> {
        process.end(0);
      });
    }).build(vertx), ar -> {
      registrationsLatch.complete();
    });
    registry.registerCommand(CommandBuilder.command("end").processHandler(process -> {
      done.complete();
    }).build(vertx), ar -> {
      registrationsLatch.complete();
    });
    registrationsLatch.awaitSuccess(10000);
    conn.read("read\r");
    latch1.awaitSuccess(10000);
    conn.sendEvent(TtyEvent.SUSP);
    latch2.awaitSuccess(10000);
    conn.read("wait\r");
    latch3.awaitSuccess(10000);
    conn.sendEvent(TtyEvent.SUSP);
    conn.read("end\r");
  }

  @Test
  public void testResumeProcessToForeground(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, conn, manager);
    shell.init().readLine();
    Async registrationLatch = context.async();
    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(1);
    CountDownLatch latch3 = new CountDownLatch(1);
    CountDownLatch latch4 = new CountDownLatch(1);
    registry.registerCommand(CommandBuilder.command("foo").processHandler(process -> {
      Job job = shell.getJob(1);
      process.suspendHandler(v -> {
        context.assertEquals(0L, latch1.getCount());
        context.assertNull(process.stdout());
        latch2.countDown();
      });
      process.resumeHandler(v -> {
        context.assertEquals(0L, latch2.getCount());
        context.assertEquals(ExecStatus.RUNNING, job.status());
        context.assertNotNull(process.stdout());
        context.assertEquals(job, shell.foregroundJob());
        conn.out().setLength(0);
        process.stdout().write("resumed");
        latch3.countDown();
      });
      process.setStdin(txt -> {
        context.assertEquals(0L, latch3.getCount());
        context.assertEquals("hello", txt);
        latch4.countDown();
      });
      latch1.countDown();
    }).build(vertx), reg -> {
      registrationLatch.complete();
    });
    registrationLatch.awaitSuccess(10000);
    conn.read("foo\r");
    latch1.await(10, TimeUnit.SECONDS);
    conn.sendEvent(TtyEvent.SUSP);
    latch2.await(10, TimeUnit.SECONDS);
    conn.read("fg\r");
    latch3.await(10, TimeUnit.SECONDS);
    conn.read("hello");
    latch4.await(10, TimeUnit.SECONDS);
    conn.assertWritten("resumed");
  }

  @Test
  public void testResumeProcessToBackground(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, conn, manager);
    shell.init().readLine();
    Async registrationLatch = context.async();
    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(1);
    CountDownLatch latch3 = new CountDownLatch(1);
    registry.registerCommand(CommandBuilder.command("foo").processHandler(process -> {
      Job job = shell.getJob(1);
      process.suspendHandler(v -> {
        context.assertEquals(0L, latch1.getCount());
        context.assertNull(process.stdout());
        latch2.countDown();
      });
      process.resumeHandler(v -> {
        context.assertEquals(0L, latch2.getCount());
        context.assertEquals(ExecStatus.RUNNING, job.status());
        context.assertNotNull(process.stdout());
        context.assertNull(shell.foregroundJob());
        try {
          latch3.await();
        } catch (InterruptedException e) {
          context.fail(e);
        }
        process.stdout().write("resumed");
      });
      process.setStdin(txt -> {
        context.assertEquals(0L, latch3.getCount());
        context.assertEquals("hello", txt);
        latch3.countDown();
      });
      latch1.countDown();
    }).build(vertx), reg -> {
      registrationLatch.complete();
    });
    registrationLatch.awaitSuccess(10000);
    conn.read("foo\r");
    latch1.await(10, TimeUnit.SECONDS);
    conn.sendEvent(TtyEvent.SUSP);
    latch2.await(10, TimeUnit.SECONDS);
    conn.out().setLength(0);
    conn.read("bg\r");
    conn.assertWritten("bg\n[1]+ Running foo\n% ");
    latch3.countDown();
    conn.assertWritten("resumed");
    conn.read("hello");
    conn.assertWritten("hello");
  }

  @Test
  public void backgroundToForeground(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, conn, manager);
    shell.init().readLine();
    Async registrationLatch = context.async();
    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(1);
    CountDownLatch latch3 = new CountDownLatch(1);
    Async async = context.async();
    registry.registerCommand(CommandBuilder.command("foo").processHandler(process -> {
      process.suspendHandler(v -> {
        context.assertEquals(1L, latch2.getCount());
        latch2.countDown();
      });
      process.resumeHandler(v -> {
        context.assertEquals(1L, latch3.getCount());
        latch3.countDown();
      });
      process.setStdin(line -> {
        async.complete();
      });
      latch1.countDown();
    }).build(vertx), reg -> {
      registrationLatch.complete();
    });
    registrationLatch.awaitSuccess(10000);
    conn.read("foo\r");
    latch1.await(10, TimeUnit.SECONDS);
    conn.sendEvent(TtyEvent.SUSP);
    latch2.await(10, TimeUnit.SECONDS);
    conn.read("bg\r");
    context.assertNull(shell.foregroundJob());
    conn.read("fg\r");
    context.assertNotNull(shell.foregroundJob());
    context.assertEquals(shell.getJob(1), shell.foregroundJob());
    latch3.await();
    conn.read("whatever");
  }

  @Test
  public void testExecuteBufferedCommand(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession adapter = new ShellSession(vertx, conn, manager);
    adapter.init().readLine();
    CountDownLatch latch = new CountDownLatch(1);
    Async registrationLatch = context.async(2);
    Async done = context.async();
    registry.registerCommand(CommandBuilder.command("foo").processHandler(process -> {
      context.assertEquals(null, conn.checkWritten("% foo\n"));
      conn.read("bar");
      process.end();
      latch.countDown();
    }).build(vertx), reg -> {
      registrationLatch.complete();
    });
    registry.registerCommand(CommandBuilder.command("bar").processHandler(process -> {
      context.assertEquals(null, conn.checkWritten("\n"));
      done.complete();
    }).build(vertx), reg2 -> {
      registrationLatch.complete();
    });
    registrationLatch.awaitSuccess(2000);
    conn.read("foo\r");
    latch.await(10, TimeUnit.SECONDS);
    context.assertNull(conn.checkWritten("bar"));
    context.assertNull(conn.checkWritten("% bar"));
    conn.read("\r");
  }

  @Test
  public void testEchoCharsDuringExecute(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, conn, manager);
    shell.init().readLine();
    Async registrationLatch = context.async();
    Async async = context.async();
    registry.registerCommand(CommandBuilder.command("foo").processHandler(process -> {
      context.assertEquals(null, conn.checkWritten("% foo\n"));
      conn.read("\u0007");
      context.assertNull(conn.checkWritten("^G"));
      conn.read("A");
      context.assertNull(conn.checkWritten("A"));
      conn.read("\r");
      context.assertNull(conn.checkWritten("\n"));
      conn.read("\003");
      context.assertNull(conn.checkWritten("^C"));
      conn.read("\004");
      context.assertNull(conn.checkWritten("^D"));
      async.complete();
    }).build(vertx), reg -> {
      registrationLatch.complete();
    });
    registrationLatch.awaitSuccess(10000);
    conn.read("foo\r");
  }

  @Test
  public void testExit(TestContext context) throws Exception {
    registry.registerCommand(Command.create(vertx, Sleep.class));
    for (String cmd : Arrays.asList("exit", "logout")) {
      TestTtyConnection conn = new TestTtyConnection();
      ShellSession adapter = new ShellSession(vertx, conn, manager);
      adapter.init().readLine();
      conn.read("sleep 10000\r");
      conn.sendEvent(TtyEvent.SUSP);
      conn.read("bg\r");
      conn.read(cmd + "\r");
      context.assertTrue(conn.isClosed());
      long now = System.currentTimeMillis();
      while (adapter.getShell().jobs().size() > 0) {
        context.assertTrue((System.currentTimeMillis() - now) < 10000);
        Thread.sleep(10);
      }
    }
  }

  @Test
  public void testEOF(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, conn, manager);
    shell.init().readLine();
    conn.read("\u0004");
    context.assertTrue(conn.isClosed());
  }
}
