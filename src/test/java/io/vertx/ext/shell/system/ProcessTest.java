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

package io.vertx.ext.shell.system;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.registry.CommandRegistry;
import io.vertx.ext.shell.session.Session;
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
import java.util.concurrent.TimeoutException;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class ProcessTest {

  Vertx vertx;
  CommandRegistry registry;
  ShellService service;

  @Before
  public void before() {
    vertx = Vertx.vertx();
    registry = CommandRegistry.get(vertx);
    service = ShellService.create(vertx);
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testRunReadyProcess(TestContext context) {
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async runningLatch = context.async();
    command.processHandler(process -> runningLatch.complete());
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
    process.run();
  }

  @Test
  public void testRunRunningProcess(TestContext context) {
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async runningLatch = context.async();
    command.processHandler(process -> runningLatch.complete());
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
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
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async suspendedLatch = context.async();
    Async runningLatch = context.async();
    command.processHandler(process -> {
      process.suspendHandler(v -> suspendedLatch.complete());
      runningLatch.complete();
    });
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
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
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async terminatedLatch = context.async();
    command.processHandler(CommandProcess::end);
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
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
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    command.processHandler(process -> context.fail());
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
    try {
      process.suspend();
      context.fail();
    } catch (Exception ignore) {
    }
  }

  @Test
  public void testSuspendRunningProcess(TestContext context) {
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async suspendedLatch = context.async();
    Async runningLatch = context.async();
    command.processHandler(process -> {
      process.suspendHandler(v -> suspendedLatch.complete());
      runningLatch.complete();
    });
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
    process.run();
    runningLatch.awaitSuccess(10000);
    process.suspend();
  }

  @Test
  public void testSuspendSuspendedProcess(TestContext context) {
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async suspendedLatch = context.async();
    Async runningLatch = context.async();
    command.processHandler(process -> {
      process.suspendHandler(v -> suspendedLatch.complete());
      runningLatch.complete();
    });
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
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
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async terminatedLatch = context.async();
    command.processHandler(CommandProcess::end);
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
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
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    command.processHandler(process -> context.fail());
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
    try {
      process.resume();
      context.fail();
    } catch (Exception ignore) {
    }
  }

  @Test
  public void testResumeRunningProcess(TestContext context) {
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async runningLatch = context.async();
    command.processHandler(process -> runningLatch.complete());
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
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
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async runningLatch = context.async();
    Async suspendedLatch = context.async();
    Async resumedLatch = context.async();
    command.processHandler(process -> {
      process.suspendHandler(v -> {
        suspendedLatch.complete();
      });
      process.resumeHandler(v -> {
        resumedLatch.complete();
      });
      runningLatch.complete();
    });
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
    process.run();
    runningLatch.awaitSuccess(10000);
    process.suspend();
    suspendedLatch.awaitSuccess(10000);
    process.resume();
  }

  @Test
  public void testResumeTerminatedProcess(TestContext context) {
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async terminatedLatch = context.async();
    command.processHandler(CommandProcess::end);
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
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
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    command.processHandler(process -> context.fail());
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
    try {
      process.interrupt();
      context.fail();
    } catch (Exception ignore) {
    }
  }

  @Test
  public void testInterruptRunningProcess(TestContext context) {
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async runningLatch = context.async();
    Async interruptedLatch = context.async(3);
    command.processHandler(process -> {
      process.interruptHandler(v -> interruptedLatch.complete());
      runningLatch.complete();
    });
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
    process.run();
    runningLatch.awaitSuccess(10000);
    process.interrupt();
    process.interrupt();
    process.interrupt();
  }

  @Test
  public void testInterruptSuspendedProcess(TestContext context) {
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async runningLatch = context.async();
    Async suspendedLatch = context.async();
    Async interruptedLatch = context.async(3);
    command.processHandler(process -> {
      process.interruptHandler(v -> interruptedLatch.complete());
      process.suspendHandler(v -> suspendedLatch.complete());
      runningLatch.complete();
    });
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
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
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async terminatedLatch = context.async();
    command.processHandler(process -> process.end());
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
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
    CommandBuilder command = CommandBuilder.command("hello");
    Async terminatedLatch = context.async(2);
    Async registrationLatch = context.async();
    Async runningLatch = context.async();
    command.processHandler(process -> {
      process.endHandler(v -> terminatedLatch.complete());
      runningLatch.complete();
    });
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
    process.terminateHandler(status -> terminatedLatch.complete());
    process.run();
    runningLatch.awaitSuccess(10000);
    process.terminate();
  }

  @Test
  public void testTerminateSuspendedProcess(TestContext context) {
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async runningLatch = context.async();
    Async suspendedLatch = context.async();
    Async terminatedLatch = context.async(2);
    command.processHandler(process -> {
      process.suspendHandler(v -> suspendedLatch.complete());
      process.endHandler(v -> terminatedLatch.complete());
      runningLatch.complete();
    });
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
    process.terminateHandler(code -> terminatedLatch.complete());
    process.run();
    runningLatch.awaitSuccess(10000);
    process.suspend();
    suspendedLatch.awaitSuccess(10000);
    process.terminate();
  }

  @Test
  public void testTerminateReadyProcess(TestContext context) {
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    command.processHandler(process -> context.fail());
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
    process.terminate();
  }

  @Test
  public void testTerminateTerminatedProcess(TestContext context) {
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async runningLatch = context.async();
    Async terminatedLatch = context.async();
    command.processHandler(process -> runningLatch.complete());
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
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
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async endedHandler = context.async();
    command.processHandler(process -> {
      process.endHandler(v -> endedHandler.complete());
      process.end(0);
    });
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
    Async terminatedHandler = context.async();
    process.terminateHandler(code -> terminatedHandler.complete());
    process.run();
  }

  @Test
  public void testEndSuspendedProcess(TestContext context) {
    CommandBuilder command = CommandBuilder.command("hello");
    Async registrationLatch = context.async();
    Async runningLatch = context.async();
    Async endedHandler = context.async();
    command.processHandler(process -> {
      process.suspendHandler(v -> {
        process.end(0);
      });
      process.endHandler(v -> endedHandler.complete());
      runningLatch.complete();
    });
    registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    registrationLatch.awaitSuccess(10000);
    Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
    Async terminatedHandler = context.async();
    process.terminateHandler(code -> terminatedHandler.complete());
    process.run();
    runningLatch.awaitSuccess(10000);
    process.suspend();
  }

  @Test
  public void testTerminatedDoesNotExecute(TestContext context) throws InterruptedException {
    Async registrationLatch = context.async();
    CountDownLatch runningLatch = new CountDownLatch(1);
    Context ctx = vertx.getOrCreateContext();
    ctx.runOnContext(v -> {
      CommandBuilder command = CommandBuilder.command("hello");
      command.processHandler(process -> runningLatch.countDown());
      registry.registerCommand(command.build(), ar -> registrationLatch.complete());
    });
    registrationLatch.awaitSuccess(10000);
    ctx.runOnContext(v -> {
      Process process = registry.createProcess("hello").setSession(Session.create()).setTty(Pty.create().slave());
      process.run();
      process.terminate();
    });
    context.assertFalse(runningLatch.await(1, TimeUnit.SECONDS));
  }
}
