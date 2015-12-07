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

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandProcessTest;
import io.vertx.ext.shell.command.CommandResolver;
import io.vertx.ext.shell.session.impl.SessionImpl;
import io.vertx.ext.shell.support.TestCommands;
import io.vertx.ext.shell.term.Pty;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class ShellServerTest {

  Vertx vertx;
  ShellServer server;
  TestCommands commands;

  @Before
  public void before(TestContext context) {
    vertx = Vertx.vertx();
    server = ShellServer.create(vertx);
    commands = new TestCommands(vertx);
    server.
        registerCommandResolver(CommandResolver.baseCommands(vertx)).
        registerCommandResolver(commands).
        listen(context.asyncAssertSuccess());
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testRun(TestContext context) {
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      process.end(3);
    }));
    Shell shell = server.createShell();
    Job job = shell.createJob(CliToken.tokenize("foo"));
    Async async = context.async();
    job.setTty(Pty.create().slave()).statusUpdateHandler(CommandProcessTest.terminateHandler(code -> {
      context.assertEquals(3, job.process().exitCode());
      async.complete();
    })).run();
  }

  @Test
  public void testThrowExceptionInProcess(TestContext context) {
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      throw new RuntimeException();
    }));
    Shell shell = server.createShell();
    Job job = shell.createJob("foo");
    Async async = context.async();
    Pty pty = Pty.create();
    job.setTty(pty.slave()).statusUpdateHandler(CommandProcessTest.terminateHandler(code -> {
      context.assertEquals(1, job.process().exitCode());
      async.complete();
    })).run();
  }

  @Test
  public void testStdin(TestContext context) {
    CountDownLatch latch = new CountDownLatch(1);
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      process.stdinHandler(data -> {
        context.assertEquals("hello_world", data);
        process.end(0);
      });
      latch.countDown();
    }));
    Shell shell = server.createShell();
    Job job = shell.createJob(CliToken.tokenize("foo"));
    Async async = context.async();
    Pty pty = Pty.create();
    job.setTty(pty.slave());
    job.statusUpdateHandler(CommandProcessTest.terminateHandler(code -> {
      context.assertEquals(0, job.process().exitCode());
      async.complete();
    })).run();
    try {
      latch.await(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      context.fail(e);
    }
    pty.write("hello_world");
  }

  @Test
  public void testStdout(TestContext context) {
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      process.write("bye_world");
      process.end(0);
    }));
    Shell shell = server.createShell();
    Job job = shell.createJob("foo");
    Async async = context.async();
    LinkedList<String> out = new LinkedList<>();
    Pty pty = Pty.create();
    pty.stdoutHandler(out::add);
    job.setTty(pty.slave());
    job.statusUpdateHandler(CommandProcessTest.terminateHandler(code -> {
      context.assertEquals(0, job.process().exitCode());
      context.assertEquals(Arrays.asList("bye_world"), out);
      async.complete();
    })).run();
  }

  @Test
  public void testVertxContext(TestContext testContext) throws Exception {
    Context commandCtx = vertx.getOrCreateContext();
    Context shellCtx = vertx.getOrCreateContext();
    Async async = testContext.async();
    Async latch = testContext.async();
    commandCtx.runOnContext(v1 -> {
      CommandBuilder cmd = CommandBuilder.command("foo");
      cmd.processHandler(process -> {
        Context currentCtx = Vertx.currentContext();
        testContext.assertTrue(commandCtx == currentCtx);
        process.stdinHandler(text -> {
          testContext.assertTrue(commandCtx == Vertx.currentContext());
          testContext.assertEquals("ping", text);
          process.write("pong");
        });
        process.suspendHandler(event -> {
          testContext.assertTrue(commandCtx == Vertx.currentContext());
          process.end(0);
        });
        latch.countDown();
      });
      commands.add(cmd);
      shellCtx.runOnContext(v3 -> {
        testContext.assertTrue(shellCtx == Vertx.currentContext());
        Shell shell = server.createShell();
        Job job = shell.createJob("foo");
        Pty pty = Pty.create();
        pty.stdoutHandler(text -> {
          testContext.assertTrue(shellCtx == Vertx.currentContext());
          testContext.assertEquals("pong", text);
          job.suspend();
        });
        job.setTty(pty.slave()).statusUpdateHandler(CommandProcessTest.terminateHandler(code -> {
          testContext.assertTrue(shellCtx == Vertx.currentContext());
          async.complete();
        })).run();
        latch.awaitSuccess(10000);
        pty.write("ping");
      });
    });
  }

  @Test
  public void testSendEvent(TestContext context) {
    CountDownLatch latch = new CountDownLatch(1);
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      process.suspendHandler(v -> {
        process.end(0);
      });
      latch.countDown();
    }));
    Shell shell = server.createShell();
    Job job = shell.createJob("foo");
    Async async = context.async();
    Pty pty = Pty.create();
    job.setTty(pty.slave()).statusUpdateHandler(CommandProcessTest.terminateHandler(status -> {
      async.complete();
    })).run();
    try {
      latch.await(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      context.fail(e);
    }
    job.suspend();
  }

  @Test
  public void testResize(TestContext context) {
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      context.assertEquals(20, process.width());
      context.assertEquals(10, process.height());
      process.resizehandler(v -> {
        context.assertEquals(25, process.width());
        context.assertEquals(15, process.height());
        process.end(0);
      });
      process.write("ping");
    }));
    Shell shell = server.createShell();
    Job job = shell.createJob("foo");
    Pty pty = Pty.create();
    Async async = context.async();
    pty.setSize(20, 10);
    pty.stdoutHandler(text -> {
      pty.setSize(25, 15);
    });
    job.setTty(pty.slave()).statusUpdateHandler(CommandProcessTest.terminateHandler(status -> {
      async.complete();
    })).run();
  }

  @Test
  public void testSessionGet(TestContext context) throws Exception {
    Async async = context.async();
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      Session session = process.session();
      context.assertNotNull(session);
      context.assertEquals("the_value", session.get("the_key"));
      process.end();
    }));
    Shell shell = server.createShell();
    Job job = shell.createJob("foo");
    Session session = new SessionImpl();
    session.put("the_key", "the_value");
    Pty pty = Pty.create();
    job.setSession(session).setTty(pty.slave()).statusUpdateHandler(CommandProcessTest.terminateHandler(status -> {
      context.assertEquals(0, job.process().exitCode());
      async.complete();
    })).run();
  }

  @Test
  public void testSessionPut(TestContext context) throws Exception {
    Async async = context.async();
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      Session session = process.session();
      context.assertNotNull(session);
      context.assertNull(session.get("the_key"));
      session.put("the_key", "the_value");
      process.end();
    }));
    Shell shell = server.createShell();
    Job job = shell.createJob("foo");
    Pty pty = Pty.create();
    Session session = new SessionImpl();
    job.setSession(session).setTty(pty.slave()).statusUpdateHandler(CommandProcessTest.terminateHandler(status -> {
      context.assertEquals(0, job.process().exitCode());
      context.assertEquals("the_value", session.get("the_key"));
      async.complete();
    })).run();
  }

  @Test
  public void testSessionRemove(TestContext context) throws Exception {
    Async async = context.async();
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      Session session = process.session();
      context.assertNotNull(session);
      context.assertEquals("the_value", session.remove("the_key"));
      process.end();
    }));
    Shell shell = server.createShell();
    Job job = shell.createJob("foo");
    Pty pty = Pty.create();
    Session session = new SessionImpl();
    session.put("the_key", "the_value");
    job.setSession(session).setTty(pty.slave()).statusUpdateHandler(CommandProcessTest.terminateHandler(status -> {
      context.assertEquals(0, job.process().exitCode());
      context.assertNull(session.get("the_key"));
      async.complete();
    })).run();
  }

  @Test
  public void testClose(TestContext context) {
    Async endLatch = context.async(2);
    Async runningLatch = context.async();
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      process.endHandler(v -> endLatch.countDown());
      runningLatch.countDown();
    }));
    Shell shell = server.createShell();
    Job job = shell.createJob("foo");
    Pty pty = Pty.create();
    job.setTty(pty.slave()).statusUpdateHandler(CommandProcessTest.terminateHandler(status -> {
      endLatch.countDown();
    })).run();
    runningLatch.awaitSuccess(10000);
    shell.close();
    endLatch.awaitSuccess(10000);
    context.assertEquals(0, shell.jobController().jobs().size());
  }

  // Just some proof of concept, there are issues because we there is a timing
  // problem when wiring processes
/*
  @Test
  public void testPipe(TestContext context) {
    CommandManager manager = CommandManager.create(vertx);
    Command cmd1 = Command.create("cmd1");
    cmd1.processHandler(process -> {
      process.setStdin(text -> {
        process.stdout().handle("|" + text + "|");
        process.end(0);
      });
    });
    Command cmd2 = Command.create("cmd2");
    cmd2.processHandler(process -> {
      System.out.println("");
      process.setStdin(text -> {
        process.stdout().handle("<" + text + ">");
        process.end(0);
      });
    });
    manager.registerCommand(cmd.build()1, context.asyncAssertSuccess(v1 -> {
      manager.registerCommand(cmd.build()2, context.asyncAssertSuccess(v2 -> {
        Shell shell = Shell.create(vertx, manager);
        shell.createJob("cmd2", context.asyncAssertSuccess(process2 -> {
          Async async = context.async();
          process2().setStdout(text -> {
            context.assertEquals("<|hello|>", text);
            async.complete();
          });
          process2.run(a1 -> {
            shell.createJob("cmd1", context.asyncAssertSuccess(process1 -> {
              process1().setStdout(process2().stdin());
              process1.run(a2 -> {
                process1().stdin().handle("hello");
              });
            }));
          });
        }));
      }));
    }));
  }
*/

}
