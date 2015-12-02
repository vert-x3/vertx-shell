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
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.command.base.Sleep;
import io.vertx.ext.shell.command.CommandRegistry;
import io.vertx.ext.shell.system.impl.InternalCommandManager;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.shell.system.ExecStatus;
import io.vertx.ext.shell.impl.ShellSession;
import io.vertx.ext.shell.support.TestTtyConnection;
import io.vertx.ext.shell.system.impl.JobImpl;
import io.vertx.ext.shell.term.impl.TtyImpl;
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
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class ShellSessionTest {

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
    ShellSession shell = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
    shell.init().readline();
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
    ShellSession shell = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
    shell.init().readline();
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
    ShellSession shell = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
    shell.init().readline();
    Async registrationLatch = context.async(2);
    Async async = context.async();
    registry.registerCommand(CommandBuilder.command("_not_consumed").processHandler(process -> {
      async.complete();
    }).build(vertx), reg -> {
      registrationLatch.countDown();
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
      registrationLatch.countDown();
    }));
    registrationLatch.awaitSuccess(10000);
    conn.read("read\rthe_line_not_consumed\r");
  }

  @Test
  public void testExecuteReadlineBuffered(TestContext context) {
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
    shell.init().readline();
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
    ShellSession shell = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
    shell.init().readline();
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
    ShellSession shell = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
    shell.init().readline();
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
        latch2.countDown();
      });
      latch1.countDown();
    }).build(vertx), ar -> {
      registrationsLatch.countDown();
    });
    registry.registerCommand(CommandBuilder.command("wait").processHandler(process -> {
      // Do nothing, this command is used to escape from readline and make
      // sure that the read data is not sent to the stopped command
      latch3.countDown();
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
    ShellSession shell = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
    shell.init().readline();
    Async registrationLatch = context.async();
    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(1);
    CountDownLatch latch3 = new CountDownLatch(1);
    CountDownLatch latch4 = new CountDownLatch(1);
    registry.registerCommand(CommandBuilder.command("foo").processHandler(process -> {
      Job job = shell.getJob(1);
      context.assertTrue(process.isInForeground());
      process.suspendHandler(v -> {
        context.assertFalse(process.isInForeground());
        context.assertEquals(0L, latch1.getCount());
        context.assertNull(process.stdout());
        latch2.countDown();
      });
      process.resumeHandler(v -> {
        context.assertTrue(process.isInForeground());
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
    ShellSession shell = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
    shell.init().readline();
    Async registrationLatch = context.async();
    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(1);
    CountDownLatch latch3 = new CountDownLatch(1);
    registry.registerCommand(CommandBuilder.command("foo").processHandler(process -> {
      Job job = shell.getJob(1);
      context.assertTrue(process.isInForeground());
      process.suspendHandler(v -> {
        context.assertFalse(process.isInForeground());
        context.assertEquals(0L, latch1.getCount());
        context.assertNull(process.stdout());
        latch2.countDown();
      });
      process.resumeHandler(v -> {
        context.assertFalse(process.isInForeground());
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
        context.fail();
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
    ShellSession shell = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
    shell.init().readline();
    Async registrationLatch = context.async();
    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(1);
    CountDownLatch latch3 = new CountDownLatch(1);
    CountDownLatch latch4 = new CountDownLatch(1);
    Async async = context.async();
    registry.registerCommand(CommandBuilder.command("foo").processHandler(process -> {
      process.suspendHandler(v -> {
        context.assertFalse(process.isInForeground());
        context.assertEquals(1L, latch2.getCount());
        latch2.countDown();
      });
      process.resumeHandler(v -> {
        context.assertFalse(process.isInForeground());
        context.assertEquals(1L, latch4.getCount());
        latch4.countDown();
      });
      process.foregroundHandler(v -> {
        context.assertTrue(process.isInForeground());
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
    latch3.await();
    context.assertNotNull(shell.foregroundJob());
    context.assertEquals(shell.getJob(1), shell.foregroundJob());
    latch4.await();
    conn.read("whatever");
  }

  @Test
  public void testExecuteBufferedCommand(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession adapter = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
    adapter.init().readline();
    CountDownLatch latch = new CountDownLatch(1);
    Async registrationLatch = context.async(2);
    Async done = context.async();
    registry.registerCommand(CommandBuilder.command("foo").processHandler(process -> {
      context.assertEquals(null, conn.checkWritten("% foo\n"));
      conn.read("bar");
      process.end();
      latch.countDown();
    }).build(vertx), reg -> {
      registrationLatch.countDown();
    });
    registry.registerCommand(CommandBuilder.command("bar").processHandler(process -> {
      context.assertEquals(null, conn.checkWritten("\n"));
      done.complete();
    }).build(vertx), reg2 -> {
      registrationLatch.countDown();
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
    ShellSession shell = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
    shell.init().readline();
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

  public void testExit(TestContext context) throws Exception {
    Async regLatch = context.async();
    registry.registerCommand(Command.create(vertx, Sleep.class), context.asyncAssertSuccess(v -> regLatch.complete()));
    regLatch.awaitSuccess(2000);
    for (String cmd : Arrays.asList("exit", "logout")) {
      TestTtyConnection conn = new TestTtyConnection();
      ShellSession adapter = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
      adapter.init().readline();
      conn.read("sleep 10000\r");
      long now = System.currentTimeMillis();
      while (adapter.jobs().size() == 0 || ((JobImpl)adapter.jobs().iterator().next()).actualStatus() != ExecStatus.RUNNING) {
        context.assertTrue(System.currentTimeMillis() - now < 2000);
        Thread.sleep(1);
      }
      conn.sendEvent(TtyEvent.SUSP);
      conn.read("bg\r");
      conn.read(cmd + "\r");
      context.assertTrue(conn.isClosed());
      now = System.currentTimeMillis();
      while (adapter.getShell().jobs().size() > 0) {
        context.assertTrue((System.currentTimeMillis() - now) < 2000);
        Thread.sleep(10);
      }
    }
  }

  @Test
  public void testEOF(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
    shell.init().readline();
    conn.read("\u0004");
    context.assertTrue(conn.isClosed());
  }

  @Test
  public void testAbc(TestContext context) throws Exception {
    Async regLatch = context.async(2);
    Async barLatch = context.async();
    Async endLatch = context.async();
    registry.registerCommand(CommandBuilder.
        command("foo").
        processHandler(process -> {
          process.setStdin(cp -> {
            context.fail();
          });
          process.endHandler(v -> barLatch.complete());
          process.end();
        }).
        build(vertx), context.asyncAssertSuccess(v -> regLatch.countDown()));
    registry.registerCommand(CommandBuilder.
        command("bar").
        processHandler(process -> {
          process.endHandler(v -> endLatch.complete());
          process.end();
        }).
        build(vertx), context.asyncAssertSuccess(v -> regLatch.countDown()));
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
    shell.init().readline();
    conn.read("foo\r");
    barLatch.awaitSuccess(2000);
    conn.read("bar\r");
  }

  @Test
  public void testSetStdinOnResumeToForeground(TestContext context) throws Exception {
    Async regLatch = context.async();
    Async fooRunning = context.async();
    Async fooSusp = context.async();
    Async fooResumed = context.async();
    Async readLatch = context.async();
    registry.registerCommand(
        CommandBuilder.command("foo").processHandler(process -> {
          process.suspendHandler(v -> fooSusp.complete());
          process.resumeHandler(v -> fooResumed.complete());
          process.setStdin(line -> {
            context.assertEquals("foo_msg", line);
            readLatch.complete();
          });
          fooRunning.complete();
        }).build(vertx), context.asyncAssertSuccess(v -> regLatch.complete())
    );
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
    shell.init().readline();
    conn.read("foo\r");
    fooRunning.awaitSuccess(2000);
    conn.sendEvent(TtyEvent.SUSP);
    fooSusp.awaitSuccess(2000);
    conn.read("fg\r");
    fooResumed.awaitSuccess(2000);
    conn.read("foo_msg");
  }

  @Test
  public void testSetStdinOnBackgroundToForeground(TestContext context) throws Exception {
    Async regLatch = context.async();
    Async fooRunning = context.async();
    Async fooSusp = context.async();
    Async fooResumed = context.async();
    Async fooToForeground = context.async();
    Async readLatch = context.async();
    registry.registerCommand(
        CommandBuilder.command("foo").processHandler(process -> {
          process.suspendHandler(v -> fooSusp.complete());
          process.resumeHandler(v -> fooResumed.complete());
          process.foregroundHandler(v -> fooToForeground.complete());
          process.setStdin(line -> {
            context.assertEquals("foo_msg", line);
            readLatch.complete();
          });
          fooRunning.complete();
        }).build(vertx), context.asyncAssertSuccess(v -> regLatch.complete())
    );
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
    shell.init().readline();
    conn.read("foo\r");
    fooRunning.awaitSuccess(2000);
    conn.sendEvent(TtyEvent.SUSP);
    fooSusp.awaitSuccess(2000);
    conn.read("bg\r");
    fooResumed.awaitSuccess(2000);
    conn.read("fg\r");
    fooToForeground.awaitSuccess(2000);
    conn.read("foo_msg");
  }

  @Test
  public void testEndInBackground(TestContext context) throws Exception {
    Async regLatch = context.async();
    Async fooRunning = context.async();
    Async fooSusp = context.async();
    Async fooResumed = context.async();
    Async endLatch = context.async();
    AtomicReference<CommandProcess> cmdProcess = new AtomicReference<>();
    AtomicReference<Context> cmdContext = new AtomicReference<>();
    registry.registerCommand(
        CommandBuilder.command("foo").processHandler(process -> {
          cmdProcess.set(process);
          cmdContext.set(Vertx.currentContext());
          process.suspendHandler(v -> fooSusp.complete());
          process.resumeHandler(v -> fooResumed.complete());
          process.endHandler(v -> {
            endLatch.complete();
          });
          fooRunning.complete();
        }).build(vertx), context.asyncAssertSuccess(v -> regLatch.complete())
    );
    TestTtyConnection conn = new TestTtyConnection();
    ShellSession shell = new ShellSession(vertx, new TtyImpl(vertx, conn), manager);
    shell.init().readline();
    conn.read("foo\r");
    fooRunning.awaitSuccess(2000);
    conn.sendEvent(TtyEvent.SUSP);
    fooSusp.awaitSuccess(2000);
    conn.read("bg\r");
    fooResumed.awaitSuccess(2000);
    cmdContext.get().runOnContext(v -> {
      cmdProcess.get().end();
    });
    long now = System.currentTimeMillis();
    while (shell.jobs().size() > 0) {
      context.assertTrue(System.currentTimeMillis() - now < 2000);
      Thread.sleep(1);
    }
    conn.read("exit\r");
    conn.getCloseLatch().await(2, TimeUnit.SECONDS);
  }
}
