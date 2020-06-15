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
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.command.base.Sleep;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.session.impl.SessionImpl;
import io.vertx.ext.shell.support.TestCommands;
import io.vertx.ext.shell.system.impl.InternalCommandManager;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.shell.system.ExecStatus;
import io.vertx.ext.shell.impl.ShellImpl;
import io.vertx.ext.shell.support.TestTtyConnection;
import io.vertx.ext.shell.system.impl.JobImpl;
import io.vertx.ext.shell.term.impl.TermImpl;
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
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class ShellTest {

  Vertx vertx;
  TestCommands commands;
  ShellServer server;

  @Before
  public void before() {
    vertx = Vertx.vertx();
    commands = new TestCommands(vertx);
    server = ShellServer.create(vertx).registerCommandResolver(commands);
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  private ShellImpl createShell(TestTtyConnection conn) {
    return new ShellImpl(new TermImpl(vertx, conn), new InternalCommandManager(commands));
  }

  @Test
  public void testVertx(TestContext context) {
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    shell.init().readline();
    Async done = context.async();
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      context.assertEquals(vertx, process.vertx());
      done.complete();
    }));
    conn.read("foo\r");
  }

  @Test
  public void testExecuteProcess(TestContext context) {
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    shell.init().readline();
    context.assertNull(shell.jobController().foregroundJob());
    context.assertEquals(Collections.emptySet(), shell.jobController().jobs());
    Async async = context.async();
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      context.assertEquals(1, shell.jobController().jobs().size());
      Job job = shell.jobController().getJob(1);
      context.assertEquals(job, shell.jobController().foregroundJob());
      context.assertEquals("foo", job.line());
      context.assertEquals(ExecStatus.RUNNING, job.status());
      async.complete();
    }));
    conn.read("foo\r");
  }

  @Test
  public void testHandleReadlineBuffered(TestContext context) {
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    shell.init().readline();
    Async async = context.async();
    commands.add(CommandBuilder.command("_not_consumed").processHandler(process -> async.complete()));
    commands.add(CommandBuilder.command("read").processHandler(process -> {
      StringBuilder buffer = new StringBuilder();
      process.stdinHandler(line -> {
        buffer.append(line);
        if (buffer.toString().equals("the_line")) {
          process.end();
        }
      });
    }));
    conn.read("read\rthe_line_not_consumed\r");
  }

  @Test
  public void testExecuteReadlineBuffered(TestContext context) {
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    shell.init().readline();
    Async async = context.async();
    AtomicInteger count = new AtomicInteger();
    commands.add(CommandBuilder.command("read").processHandler(process -> {
      if (count.incrementAndGet() == 2) {
        async.complete();
      }
      process.end(0);
    }));
    conn.read("read\rread\r");
  }

  @Test
  public void testSuspendProcess(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    shell.init().readline();
    Async done = context.async();
    Async latch2 = context.async();
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      Job job = shell.jobController().getJob(1);
      process.suspendHandler(v -> {
        context.assertEquals(ExecStatus.STOPPED, job.status());
        context.assertNull(shell.jobController().foregroundJob());
        done.complete();
      });
      latch2.complete();
    }));
    conn.read("foo\r");
    latch2.awaitSuccess(10000);
    conn.sendEvent(TtyEvent.SUSP);
  }

  @Test
  public void testSuspendedProcessDisconnectedFromTty(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    shell.init().readline();
    Async done = context.async();
    Async latch1 = context.async();
    Async latch2 = context.async();
    Async latch3 = context.async();
    commands.add(CommandBuilder.command("read").processHandler(process -> {
      process.stdinHandler(line -> context.fail("Should not process line " + line));
      process.suspendHandler(v -> {
        try {
          process.write("");
          context.fail();
        } catch (IllegalStateException ignore) {
        }
        latch2.countDown();
      });
      latch1.countDown();
    }));
    commands.add(CommandBuilder.command("wait").processHandler(process -> {
      // Do nothing, this command is used to escape from readline and make
      // sure that the read data is not sent to the stopped command
      latch3.countDown();
      process.suspendHandler(v -> process.end(0));
    }));
    commands.add(CommandBuilder.command("end").processHandler(process -> done.complete()));
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
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    shell.init().readline();
    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(1);
    CountDownLatch latch3 = new CountDownLatch(1);
    CountDownLatch latch4 = new CountDownLatch(1);
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      Job job = shell.jobController().getJob(1);
      context.assertTrue(process.isForeground());
      process.suspendHandler(v -> {
        context.assertFalse(process.isForeground());
        context.assertEquals(0L, latch1.getCount());
        try {
          process.write("");
          context.fail();
        } catch (IllegalStateException ignore) {
        }
        latch2.countDown();
      });
      process.resumeHandler(v -> {
        context.assertTrue(process.isForeground());
        context.assertEquals(0L, latch2.getCount());
        context.assertEquals(ExecStatus.RUNNING, job.status());
        process.write("");
        context.assertEquals(job, shell.jobController().foregroundJob());
        conn.out().setLength(0);
        process.write("resumed");
        latch3.countDown();
      });
      process.stdinHandler(txt -> {
        context.assertEquals(0L, latch3.getCount());
        context.assertEquals("hello", txt);
        latch4.countDown();
      });
      latch1.countDown();
    }));
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
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    shell.init().readline();
    Async latch1 = context.async();
    Async latch2 = context.async();
    Async latch3 = context.async();
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      Job job = shell.jobController().getJob(1);
      context.assertTrue(process.isForeground());
      process.suspendHandler(v -> {
        context.assertFalse(process.isForeground());
        context.assertEquals(0, latch1.count());
        try {
          process.write("");
          context.fail();
        } catch (IllegalStateException ignore) {
        }
        latch2.countDown();
      });
      process.resumeHandler(v -> {
        context.assertFalse(process.isForeground());
        context.assertEquals(0, latch2.count());
        context.assertEquals(ExecStatus.RUNNING, job.status());
        process.write("");
        context.assertNull(shell.jobController().foregroundJob());
        latch3.awaitSuccess(2000);
        process.write("resumed");
      });
      process.stdinHandler(txt -> context.fail());
      latch1.countDown();
    }));
    conn.read("foo\r");
    latch1.awaitSuccess(10000);
    conn.sendEvent(TtyEvent.SUSP);
    latch2.awaitSuccess(10000);
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
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    shell.init().readline();
    Async latch1 = context.async();
    Async latch2 = context.async();
    Async latch3 = context.async();
    Async async = context.async();
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      process.suspendHandler(v -> {
        context.assertFalse(process.isForeground());
        context.assertEquals(1, latch2.count());
        latch2.countDown();
      });
      process.resumeHandler(v -> context.assertFalse(process.isForeground()));
      process.foregroundHandler(v -> {
        context.assertTrue(process.isForeground());
        latch3.countDown();
      });
      process.stdinHandler(line -> async.complete());
      latch1.countDown();
    }));
    conn.read("foo\r");
    latch1.awaitSuccess(2000);
    conn.sendEvent(TtyEvent.SUSP);
    latch2.awaitSuccess(2000);
    conn.read("bg\r");
    context.assertNull(shell.jobController().foregroundJob());
    conn.read("fg\r");
    latch3.await();
    context.assertNotNull(shell.jobController().foregroundJob());
    context.assertEquals(shell.jobController().getJob(1), shell.jobController().foregroundJob());
    conn.read("whatever");
  }

  @Test
  public void testExecuteBufferedCommand(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl adapter = createShell(conn);
    adapter.init().readline();
    CountDownLatch latch = new CountDownLatch(1);
    Async done = context.async();
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      context.assertEquals(null, conn.checkWritten("% foo\n"));
      conn.read("bar");
      process.end();
      latch.countDown();
    }));
    commands.add(CommandBuilder.command("bar").processHandler(process -> {
      context.assertEquals(null, conn.checkWritten("\n"));
      done.complete();
    }));
    conn.read("foo\r");
    latch.await(10, TimeUnit.SECONDS);
    context.assertNull(conn.checkWritten("bar"));
    context.assertNull(conn.checkWritten("% bar"));
    conn.read("\r");
  }

  @Test
  public void testEchoCharsDuringExecute(TestContext testContext) throws Exception {
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    shell.init().readline();
    Async async = testContext.async();
    vertx.deployVerticle(new AbstractVerticle() {
      @Override
      public void start() {
        commands.add(CommandBuilder.command("foo").processHandler(process -> {
          testContext.assertEquals(null, conn.checkWritten("% foo\n"));
          conn.read("\u0007");
          testContext.assertNull(conn.checkWritten("^G"));
          conn.read("A");
          testContext.assertNull(conn.checkWritten("A"));
          conn.read("\r");
          testContext.assertNull(conn.checkWritten("\n"));
          conn.read("\003");
          testContext.assertNull(conn.checkWritten("^C"));
          conn.read("\004");
          testContext.assertNull(conn.checkWritten("^D"));
          async.complete();
        }));
      }
    }, testContext.asyncAssertSuccess(id -> conn.read("foo\r")));
  }

  public void testExit(TestContext context) throws Exception {
    commands.add(Command.create(vertx, Sleep.class));
    for (String cmd : Arrays.asList("exit", "logout")) {
      TestTtyConnection conn = new TestTtyConnection(vertx);
      ShellImpl adapter = createShell(conn);
      adapter.init().readline();
      conn.read("sleep 10000\r");
      long now = System.currentTimeMillis();
      while (adapter.jobController().jobs().size() == 0 || ((JobImpl) adapter.jobController().jobs().iterator().next()).actualStatus() != ExecStatus.RUNNING) {
        context.assertTrue(System.currentTimeMillis() - now < 2000);
        Thread.sleep(1);
      }
      conn.sendEvent(TtyEvent.SUSP);
      conn.read("bg\r");
      conn.read(cmd + "\r");
      context.assertTrue(conn.isClosed());
      now = System.currentTimeMillis();
      while (adapter.jobController().jobs().size() > 0) {
        context.assertTrue((System.currentTimeMillis() - now) < 2000);
        Thread.sleep(10);
      }
    }
  }

  @Test
  public void testEOF(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    shell.init().readline();
    conn.read("\u0004");
    context.assertTrue(conn.getCloseLatch().await(2, TimeUnit.SECONDS));
  }

  @Test
  public void testDefaultPrompt(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    shell.init().readline();
    conn.assertWritten("% ");
  }

  @Test
  public void testPromptException(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    Function<Session,String> dynamicPrompt = x ->  {
      System.out.println("Before");
      if (context != null) {
        throw new IllegalArgumentException("BAD_PROMPT");
      }
      System.out.println("After");
      return "OK";
      };
    shell.setPrompt(dynamicPrompt);
    shell.init().readline();
    conn.assertWritten("% ");
  }
  @Test
  public void testPrompt(TestContext context) throws Exception {
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    SessionImpl session = (SessionImpl)shell.session();
    Function<Session,String> dynamicPrompt = x -> x.get("CURRENT_PROMPT");
    String prompt1 = "PROMPT1";
    String prompt2 = "PROMPT2";
    shell.setPrompt(dynamicPrompt);
    session.put("CURRENT_PROMPT",prompt1);
    shell.init().readline();
    Async async = context.async();
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      context.assertEquals(null, conn.checkWritten(prompt1+"foo\n"));
      process.stdinHandler(cp -> context.fail());
      process.endHandler(v -> async.complete()
      );
      process.end();

    }));
    Async async2 = context.async();
    commands.add(CommandBuilder.command("bar2").processHandler(process -> {
      context.assertEquals(null, conn.checkWritten(prompt2+"bar2\n"));
      process.stdinHandler(cp -> context.fail());
      process.endHandler(v -> async2.complete()
      );
      process.end();
    }));
    conn.read("foo\r");
    session.put("CURRENT_PROMPT",prompt2);
    async.awaitSuccess(5000);
    conn.read("bar2\n");
  }

  @Test
  public void testAbc(TestContext context) throws Exception {
    Async barLatch = context.async();
    Async endLatch = context.async();
    commands.add(CommandBuilder.
        command("foo").
        processHandler(process -> {
          process.stdinHandler(cp -> context.fail());
          process.endHandler(v -> barLatch.complete()
          );
          process.end();
        }).
        build(vertx));
    commands.add(CommandBuilder.
        command("bar").
        processHandler(process -> {
          process.endHandler(v -> endLatch.complete());
          process.end();
        }).
        build(vertx));
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    shell.init().readline();
    conn.read("foo\r");
    barLatch.awaitSuccess(2000);
    conn.read("bar\r");
  }

  @Test
  public void testSetStdinOnResumeToForeground(TestContext context) throws Exception {
    Async fooRunning = context.async();
    Async fooSusp = context.async();
    Async fooResumed = context.async();
    Async readLatch = context.async();
    commands.add(
        CommandBuilder.command("foo").processHandler(process -> {
          process.suspendHandler(v -> fooSusp.complete());
          process.resumeHandler(v -> fooResumed.complete());
          process.stdinHandler(line -> {
            context.assertEquals("foo_msg", line);
            readLatch.complete();
          });
          fooRunning.complete();
        }));
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
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
    Async fooRunning = context.async();
    Async fooSusp = context.async();
    Async fooResumed = context.async();
    Async fooToForeground = context.async();
    Async readLatch = context.async();
    commands.add(
        CommandBuilder.command("foo").processHandler(process -> {
          process.suspendHandler(v -> fooSusp.complete());
          process.resumeHandler(v -> fooResumed.complete());
          process.foregroundHandler(v -> fooToForeground.complete());
          process.stdinHandler(line -> {
            context.assertEquals("foo_msg", line);
            readLatch.complete();
          });
          fooRunning.complete();
        }));
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    shell.init().readline();
    conn.read("foo\r");
    fooRunning.awaitSuccess(2000);
    conn.sendEvent(TtyEvent.SUSP);
    fooSusp.awaitSuccess(2000);
    conn.read("bg\r");
    fooResumed.awaitSuccess(2000);
    conn.read("fg\r");
    fooToForeground.awaitSuccess(20000000);
    conn.read("foo_msg");
  }

  @Test
  public void testEndInBackground(TestContext context) throws Exception {
    Async fooRunning = context.async();
    Async fooSusp = context.async();
    Async fooResumed = context.async();
    Async endLatch = context.async();
    AtomicReference<CommandProcess> cmdProcess = new AtomicReference<>();
    AtomicReference<Context> cmdContext = new AtomicReference<>();
    commands.add(
        CommandBuilder.command("foo").processHandler(process -> {
          cmdProcess.set(process);
          cmdContext.set(Vertx.currentContext());
          process.suspendHandler(v -> fooSusp.complete());
          process.resumeHandler(v -> fooResumed.complete());
          process.endHandler(v -> endLatch.complete());
          fooRunning.complete();
        }));
    TestTtyConnection conn = new TestTtyConnection(vertx);
    ShellImpl shell = createShell(conn);
    shell.init().readline();
    conn.read("foo\r");
    fooRunning.awaitSuccess(2000);
    conn.sendEvent(TtyEvent.SUSP);
    fooSusp.awaitSuccess(2000);
    conn.read("bg\r");
    fooResumed.awaitSuccess(2000);
    cmdContext.get().runOnContext(v -> cmdProcess.get().end());
    long now = System.currentTimeMillis();
    while (shell.jobController().jobs().size() > 0) {
      context.assertTrue(System.currentTimeMillis() - now < 2000);
      Thread.sleep(1);
    }
    conn.read("exit\r");
    conn.getCloseLatch().await(2, TimeUnit.SECONDS);
  }
}
