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
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.io.EventType;
import io.vertx.ext.shell.io.Stream;
import io.vertx.ext.shell.registry.CommandRegistry;
import io.vertx.ext.shell.support.TestProcessContext;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
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
public class ShellServiceTest {

  Vertx vertx = Vertx.vertx();

  @Test
  public void testRun(TestContext context) {
    CommandRegistry manager = CommandRegistry.get(vertx);
    CommandBuilder cmd = CommandBuilder.builder("foo");
    cmd.processHandler(process -> {
      process.end(3);
    });
    manager.registerCommand(cmd.build(), context.asyncAssertSuccess(v -> {
      manager.createProcess("foo", context.asyncAssertSuccess(process -> {
        Async async = context.async();
        TestProcessContext ctx = new TestProcessContext();
        ctx.endHandler(code -> {
          context.assertEquals(3, code);
          async.complete();
        });
        process.execute(ctx);
      }));
    }));
  }

  @Test
  public void testThrowExceptionInProcess(TestContext context) {
    CommandRegistry manager = CommandRegistry.get(vertx);
    CommandBuilder cmd = CommandBuilder.builder("foo");
    cmd.processHandler(process -> {
      throw new RuntimeException();
    });
    manager.registerCommand(cmd.build(), context.asyncAssertSuccess(v -> {
      manager.createProcess("foo", context.asyncAssertSuccess(process -> {
        Async async = context.async();
        TestProcessContext ctx = new TestProcessContext();
        ctx.endHandler(code -> {
          context.assertEquals(1, code);
          async.complete();
        });
        process.execute(ctx);
      }));
    }));
  }

  @Test
  public void testStdin(TestContext context) {
    CommandRegistry manager = CommandRegistry.get(vertx);
    CommandBuilder cmd = CommandBuilder.builder("foo");
    CountDownLatch latch = new CountDownLatch(1);
    cmd.processHandler(process -> {
      process.setStdin(data -> {
        context.assertEquals("hello_world", data);
        process.end(0);
      });
      latch.countDown();
    });
    manager.registerCommand(cmd.build(), context.asyncAssertSuccess(v -> {
      Async async = context.async();
      manager.createProcess("foo", context.asyncAssertSuccess(job -> {
        TestProcessContext ctx = new TestProcessContext();
        ctx.endHandler(code -> {
          context.assertEquals(0, code);
          async.complete();
        });
        job.execute(ctx);
        try {
          latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
          context.fail(e);
        }
        ctx.stdin().handle("hello_world");
      }));
    }));
  }

  @Test
  public void testStdout(TestContext context) {
    CommandRegistry manager = CommandRegistry.get(vertx);
    CommandBuilder cmd = CommandBuilder.builder("foo");
    cmd.processHandler(process -> {
      process.stdout().write("bye_world");
      process.end(0);
    });
    manager.registerCommand(cmd.build(), context.asyncAssertSuccess(v -> {
      manager.createProcess("foo", context.asyncAssertSuccess(job -> {
        Async async = context.async();
        LinkedList<String> out = new LinkedList<>();
        TestProcessContext ctx = new TestProcessContext();
        ctx.setStdout(Stream.ofString(out::add));
        ctx.endHandler(code -> {
          context.assertEquals(0, code);
          context.assertEquals(Arrays.asList("bye_world"), out);
          async.complete();
        });
        job.execute(ctx);
      }));
    }));
  }

  @Test
  public void testVertxContext(TestContext testContext) throws Exception {
    Context commandCtx = vertx.getOrCreateContext();
    Context shellCtx = vertx.getOrCreateContext();
    CommandRegistry manager = CommandRegistry.get(vertx);
    Async async = testContext.async();
    CountDownLatch latch = new CountDownLatch(1);
    commandCtx.runOnContext(v1 -> {
      CommandBuilder cmd = CommandBuilder.builder("foo");
      cmd.processHandler(process -> {
        testContext.assertTrue(commandCtx == Vertx.currentContext());
        process.setStdin(text -> {
          testContext.assertTrue(commandCtx == Vertx.currentContext());
          testContext.assertEquals("ping", text);
          process.write("pong");
        });
        process.eventHandler(EventType.SIGTSTP, event -> {
          testContext.assertTrue(commandCtx == Vertx.currentContext());
          process.end(0);
        });
        latch.countDown();
      });
      manager.registerCommand(cmd.build(), testContext.asyncAssertSuccess(v2 -> {
        shellCtx.runOnContext(v3 -> {
          manager.createProcess("foo", testContext.asyncAssertSuccess(job -> {
            testContext.assertTrue(shellCtx == Vertx.currentContext());
            TestProcessContext ctx = new TestProcessContext();
            ctx.endHandler(code -> {
              testContext.assertTrue(shellCtx == Vertx.currentContext());
              async.complete();
            });
            job.execute(ctx);
            try {
              latch.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
              testContext.fail(e);
            }
            ctx.setStdout(Stream.ofObject(text -> {
              testContext.assertTrue(shellCtx == Vertx.currentContext());
              testContext.assertEquals("pong", text);
              testContext.assertTrue(ctx.sendEvent(EventType.SIGTSTP));
              testContext.assertFalse(ctx.sendEvent(EventType.EOF));
            }));
            ctx.stdin().handle("ping");
          }));
        });
      }));
    });
  }

  @Test
  public void testSendEvent(TestContext context) {
    CommandRegistry manager = CommandRegistry.get(vertx);
    CommandBuilder cmd = CommandBuilder.builder("foo");
    CountDownLatch latch = new CountDownLatch(1);
    cmd.processHandler(process -> {
      process.eventHandler(EventType.SIGTSTP, v -> {
        process.end(0);
      });
      latch.countDown();
    });
    manager.registerCommand(cmd.build(), context.asyncAssertSuccess(v -> {
      manager.createProcess("foo", context.asyncAssertSuccess(job -> {
        Async async = context.async();
        TestProcessContext ctx = new TestProcessContext();
        ctx.endHandler(status -> {
          async.complete();
        });
        job.execute(ctx);
        try {
          latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
          context.fail(e);
        }
        ctx.sendEvent(EventType.SIGTSTP);
      }));
    }));
  }

  @Test
  public void testResize(TestContext context) {
    CommandRegistry manager = CommandRegistry.get(vertx);
    CommandBuilder cmd = CommandBuilder.builder("foo");
    cmd.processHandler(process -> {
      context.assertEquals(20, process.width());
      context.assertEquals(10, process.height());
      process.eventHandler(EventType.SIGWINCH, v -> {
        context.assertEquals(25, process.width());
        context.assertEquals(15, process.height());
        process.end(0);
      });
      process.stdout().write("ping");
    });
    manager.registerCommand(cmd.build(), context.asyncAssertSuccess(v -> {
      manager.createProcess("foo", context.asyncAssertSuccess(job -> {
        Async async = context.async();
        TestProcessContext ctx = new TestProcessContext();
        ctx.endHandler(status -> {
          async.complete();
        });
        ctx.setWindowSize(20, 10);
        ctx.setStdout(Stream.ofObject(text -> {
          ctx.setWindowSize(25, 15);
        }));
        job.execute(ctx);
      }));
    }));
  }

  @Test
  public void testSessionGet(TestContext context) throws Exception {
    CommandRegistry manager = CommandRegistry.get(vertx);
    CommandBuilder cmd = CommandBuilder.builder("foo");
    Async async = context.async();
    cmd.processHandler(process -> {
      Session session = process.session();
      context.assertNotNull(session);
      context.assertEquals("the_value", session.get("the_key"));
      process.end();
    });
    manager.registerCommand(cmd.build());
    manager.createProcess("foo", context.asyncAssertSuccess(process -> {
      TestProcessContext ctx = new TestProcessContext();
      ctx.session().put("the_key", "the_value");
      ctx.endHandler(status -> {
        context.assertEquals(0, status);
        async.complete();
      });
      process.execute(ctx);
    }));
  }

  @Test
  public void testSessionPut(TestContext context) throws Exception {
    CommandRegistry manager = CommandRegistry.get(vertx);
    CommandBuilder cmd = CommandBuilder.builder("foo");
    Async async = context.async();
    cmd.processHandler(process -> {
      Session session = process.session();
      context.assertNotNull(session);
      context.assertNull(session.get("the_key"));
      session.put("the_key", "the_value");
      process.end();
    });
    manager.registerCommand(cmd.build());
    manager.createProcess("foo", context.asyncAssertSuccess(process -> {
      TestProcessContext ctx = new TestProcessContext();
      ctx.endHandler(status -> {
        context.assertEquals(0, status);
        context.assertEquals("the_value", ctx.session().get("the_key"));
        async.complete();
      });
      process.execute(ctx);
    }));
  }

  @Test
  public void testSessionRemove(TestContext context) throws Exception {
    CommandRegistry manager = CommandRegistry.get(vertx);
    CommandBuilder cmd = CommandBuilder.builder("foo");
    Async async = context.async();
    cmd.processHandler(process -> {
      Session session = process.session();
      context.assertNotNull(session);
      context.assertEquals("the_value", session.remove("the_key"));
      process.end();
    });
    manager.registerCommand(cmd.build());
    manager.createProcess("foo", context.asyncAssertSuccess(process -> {
      TestProcessContext ctx = new TestProcessContext();
      ctx.session().put("the_key", "the_value");
      ctx.endHandler(status -> {
        context.assertEquals(0, status);
        context.assertNull(ctx.session().get("the_key"));
        async.complete();
      });
      process.execute(ctx);
    }));
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
