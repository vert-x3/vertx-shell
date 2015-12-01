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

package io.vertx.ext.shell.command;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.system.*;
import io.vertx.ext.shell.system.Process;
import io.vertx.ext.shell.term.Pty;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class CommandProcessTest {

  Vertx vertx;

  @Before
  public void before() {
    vertx = Vertx.vertx();
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testRunReadyProcess(TestContext context) {
    AtomicInteger status = new AtomicInteger();
    CommandBuilder builder = CommandBuilder.command("hello");
    Async runningLatch = context.async(2);
    builder.processHandler(process -> {
      context.assertEquals(0, status.getAndIncrement());
      runningLatch.countDown();
    });
    Command command = builder.build(vertx);
    io.vertx.ext.shell.system.Process process = command.createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.run(v -> {
      context.assertEquals(1, status.getAndIncrement());
      runningLatch.countDown();
    });
  }

  @Test
  public void testRunRunningProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    Async runningLatch = context.async();
    builder.processHandler(process -> runningLatch.complete());
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.run();
    runningLatch.awaitSuccess(10000);
    try {
      process.run();
      context.fail();
    } catch (IllegalStateException ignore) {
    }
  }

  @Test
  public void testRunSuspendedProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    Async suspendedLatch = context.async();
    Async runningLatch = context.async();
    builder.processHandler(process -> {
      process.suspendHandler(v -> suspendedLatch.complete());
      runningLatch.complete();
    });
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.run();
    runningLatch.awaitSuccess(10000);
    process.suspend();
    suspendedLatch.awaitSuccess(10000);
    try {
      process.run();
      context.fail();
    } catch (IllegalStateException ignore) {
    }
  }

  @Test
  public void testRunTerminatedProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    Async terminatedLatch = context.async();
    builder.processHandler(CommandProcess::end);
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.terminateHandler(status -> terminatedLatch.complete());
    process.run();
    terminatedLatch.awaitSuccess(10000);
    try {
      process.run();
      context.fail();
    } catch (IllegalStateException ignore) {
    }
  }

  @Test
  public void testSuspendReadyProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    builder.processHandler(process -> context.fail());
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    try {
      process.suspend();
      context.fail();
    } catch (Exception ignore) {
    }
  }

  @Test
  public void testSuspendRunningProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    AtomicInteger status = new AtomicInteger();
    Async suspendedLatch = context.async(2);
    Async runningLatch = context.async();
    builder.processHandler(process -> {
      context.assertEquals(0, status.getAndIncrement());
      process.suspendHandler(v -> suspendedLatch.complete());
      runningLatch.complete();
    });
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.run();
    runningLatch.awaitSuccess(10000);
    process.suspend(v -> {
      context.assertEquals(1, status.getAndIncrement());
      suspendedLatch.countDown();
    });
  }

  @Test
  public void testSuspendSuspendedProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    Async suspendedLatch = context.async();
    Async runningLatch = context.async();
    builder.processHandler(process -> {
      process.suspendHandler(v -> suspendedLatch.complete());
      runningLatch.complete();
    });
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.run();
    runningLatch.awaitSuccess(10000);
    process.suspend();
    suspendedLatch.awaitSuccess(10000);
    try {
      process.suspend();
      context.fail();
    } catch (Exception ignore) {
    }
  }

  @Test
  public void testSuspendTerminatedProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    Async terminatedLatch = context.async();
    builder.processHandler(CommandProcess::end);
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.terminateHandler(status -> terminatedLatch.complete());
    process.run();
    terminatedLatch.awaitSuccess(10000);
    try {
      process.suspend();
      context.fail();
    } catch (IllegalStateException ignore) {
    }
  }

  @Test
  public void testResumeReadyProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    builder.processHandler(process -> context.fail());
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    try {
      process.resume();
      context.fail();
    } catch (Exception ignore) {
    }
  }

  @Test
  public void testResumeRunningProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    Async runningLatch = context.async();
    builder.processHandler(process -> runningLatch.complete());
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.run();
    runningLatch.awaitSuccess(10000);
    try {
      process.resume();
      context.fail();
    } catch (Exception ignore) {
    }
  }

  @Test
  public void testResumeSuspendedProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    AtomicInteger status = new AtomicInteger();
    Async runningLatch = context.async();
    Async suspendedLatch = context.async();
    Async resumedLatch = context.async(2);
    builder.processHandler(process -> {
      process.suspendHandler(v -> {
        suspendedLatch.complete();
      });
      process.resumeHandler(v -> {
        context.assertEquals(0, status.getAndIncrement());
        resumedLatch.countDown();
      });
      runningLatch.complete();
    });
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.run();
    runningLatch.awaitSuccess(10000);
    process.suspend();
    suspendedLatch.awaitSuccess(10000);
    process.resume(v -> {
      context.assertEquals(1, status.getAndIncrement());
      resumedLatch.countDown();
    });
  }

  @Test
  public void testResumeTerminatedProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    Async terminatedLatch = context.async();
    builder.processHandler(CommandProcess::end);
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.terminateHandler(status -> terminatedLatch.complete());
    process.run();
    terminatedLatch.awaitSuccess(10000);
    try {
      process.resume();
      context.fail();
    } catch (IllegalStateException ignore) {
    }
  }

  @Test
  public void testInterruptReadyProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    builder.processHandler(process -> context.fail());
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    try {
      process.interrupt();
      context.fail();
    } catch (Exception ignore) {
    }
  }

  @Test
  public void testInterruptRunningProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    AtomicInteger status = new AtomicInteger();
    Async runningLatch = context.async();
    Async interruptedLatch = context.async(6);
    builder.processHandler(process -> {
      process.interruptHandler(v -> {
        status.incrementAndGet();
        interruptedLatch.countDown();
      });
      runningLatch.complete();
    });
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.run();
    runningLatch.awaitSuccess(10000);
    process.interrupt(v -> {
      context.assertEquals(1, status.getAndIncrement());
      interruptedLatch.countDown();
    });
    process.interrupt(v -> {
      context.assertEquals(3, status.getAndIncrement());
      interruptedLatch.countDown();
    });
    process.interrupt(v -> {
      context.assertEquals(5, status.getAndIncrement());
      interruptedLatch.countDown();
    });
  }

  @Test
  public void testInterruptSuspendedProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    Async runningLatch = context.async();
    Async suspendedLatch = context.async();
    Async interruptedLatch = context.async(3);
    builder.processHandler(process -> {
      process.interruptHandler(v -> interruptedLatch.countDown());
      process.suspendHandler(v -> suspendedLatch.complete());
      runningLatch.complete();
    });
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.run();
    runningLatch.awaitSuccess(10000);
    process.suspend();
    suspendedLatch.awaitSuccess(10000);
    process.interrupt();
    process.interrupt();
    process.interrupt();
  }

  @Test
  public void testInterruptTerminatedProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    Async terminatedLatch = context.async();
    builder.processHandler(process -> process.end());
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.terminateHandler(v -> terminatedLatch.complete());
    process.run();
    terminatedLatch.awaitSuccess(10000);
    try {
      process.interrupt();
      context.fail();
    } catch (Exception ignore) {
    }
  }

  @Test
  public void testTerminateRunningProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    AtomicInteger status = new AtomicInteger();
    Async terminatedLatch = context.async(3);
    Async runningLatch = context.async();
    builder.processHandler(process -> {
      process.endHandler(v -> {
        context.assertEquals(0, status.getAndIncrement());
        terminatedLatch.countDown();
      });
      runningLatch.complete();
    });
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.terminateHandler(code -> terminatedLatch.countDown());
    process.run();
    runningLatch.awaitSuccess(10000);
    process.terminate(v -> {
      context.assertEquals(1, status.getAndIncrement());
      terminatedLatch.countDown();
    });
  }

  @Test
  public void testTerminateSuspendedProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    AtomicInteger status = new AtomicInteger();
    Async runningLatch = context.async();
    Async suspendedLatch = context.async();
    Async terminatedLatch = context.async();
    builder.processHandler(process -> {
      process.suspendHandler(v -> suspendedLatch.complete());
      process.endHandler(v -> {
        context.assertEquals(0, status.getAndIncrement());
        terminatedLatch.countDown();
      });
      runningLatch.complete();
    });
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.terminateHandler(code -> terminatedLatch.countDown());
    process.run();
    runningLatch.awaitSuccess(10000);
    process.suspend();
    suspendedLatch.awaitSuccess(10000);
    process.terminate(v -> {
      context.assertEquals(1, status.getAndIncrement());
      terminatedLatch.countDown();
    });
  }

  @Test
  public void testTerminateReadyProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    builder.processHandler(process -> context.fail());
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.terminate();
  }

  @Test
  public void testTerminateTerminatedProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    Async runningLatch = context.async();
    Async terminatedLatch = context.async();
    builder.processHandler(process -> runningLatch.complete());
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    process.terminateHandler(status -> terminatedLatch.complete());
    process.run();
    runningLatch.awaitSuccess(10000);
    process.terminate();
    terminatedLatch.awaitSuccess(10000);
    try {
      process.terminate();
      context.fail();
    } catch (IllegalStateException ignore) {
    }
  }

  @Test
  public void testEndRunningProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    Async endedHandler = context.async();
    builder.processHandler(process -> {
      process.endHandler(v -> endedHandler.complete());
      process.end(0);
    });
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    Async terminatedHandler = context.async();
    process.terminateHandler(code -> terminatedHandler.complete());
    process.run();
  }

  @Test
  public void testEndSuspendedProcess(TestContext context) {
    CommandBuilder builder = CommandBuilder.command("hello");
    Async runningLatch = context.async();
    Async endedHandler = context.async();
    builder.processHandler(process -> {
      process.suspendHandler(v -> {
        process.end(0);
      });
      process.endHandler(v -> endedHandler.complete());
      runningLatch.complete();
    });
    Process process = builder.build(vertx).createProcess().setSession(Session.create()).setTty(Pty.create().slave());
    Async terminatedHandler = context.async();
    process.terminateHandler(code -> terminatedHandler.complete());
    process.run();
    runningLatch.awaitSuccess(10000);
    process.suspend();
  }

/*
  @Test
  public void testTerminatedDoesNotExecute(TestContext context) throws InterruptedException {
    AtomicReference<Command> cmd = new AtomicReference<>();
    Async registrationLatch = context.async();
    CountDownLatch runningLatch = new CountDownLatch(1);
    Context ctx = vertx.getOrCreateContext();
    ctx.runOnContext(v -> {
      CommandBuilder builder = CommandBuilder.command("hello");
      builder.processHandler(process -> runningLatch.countDown());
      cmd.set(builder.build(vertx));
      registrationLatch.complete();
    });
    registrationLatch.awaitSuccess(10000);
    ctx.runOnContext(v -> {
      Process process = cmd.get().createProcess().setSession(Session.create()).setTty(Pty.create().slave());
      process.run();
      process.terminate();
    });
    context.assertFalse(runningLatch.await(1, TimeUnit.SECONDS));
  }
*/
}
