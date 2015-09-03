package io.vertx.ext.unit;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandManager;
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
    CommandManager manager = CommandManager.get(vertx);
    Command cmd = Command.command("foo");
    cmd.processHandler(process -> {
      process.end(3);
    });
    manager.registerCommand(cmd, context.asyncAssertSuccess(v -> {
      manager.createProcess("foo", context.asyncAssertSuccess(process -> {
        TestProcessContext ctx = new TestProcessContext();
        ctx.endHandler(code -> {
          context.assertEquals(3, code);
        });
        process.execute(ctx);
      }));
    }));
  }

  @Test
  public void testStdin(TestContext context) {
    CommandManager manager = CommandManager.get(vertx);
    Command cmd = Command.command("foo");
    CountDownLatch latch = new CountDownLatch(1);
    cmd.processHandler(process -> {
      process.setStdin(data -> {
        context.assertEquals("hello_world", data);
        process.end(0);
      });
      latch.countDown();
    });
    manager.registerCommand(cmd, context.asyncAssertSuccess(v -> {
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
        ctx.stdin.handle("hello_world");
      }));
    }));
  }

  @Test
  public void testStdout(TestContext context) {
    CommandManager manager = CommandManager.get(vertx);
    Command cmd = Command.command("foo");
    cmd.processHandler(process -> {
      process.stdout().handle("bye_world");
      process.end(0);
    });
    manager.registerCommand(cmd, context.asyncAssertSuccess(v -> {
      manager.createProcess("foo", context.asyncAssertSuccess(job -> {
        Async async = context.async();
        LinkedList<String> out = new LinkedList<>();
        TestProcessContext ctx = new TestProcessContext();
        ctx.setStdout(out::add);
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
    CommandManager manager = CommandManager.get(vertx);
    Async async = testContext.async();
    CountDownLatch latch = new CountDownLatch(1);
    commandCtx.runOnContext(v1 -> {
      Command command = Command.command("foo");
      command.processHandler(process -> {
        testContext.assertTrue(commandCtx == Vertx.currentContext());
        process.setStdin(text -> {
          testContext.assertTrue(commandCtx == Vertx.currentContext());
          testContext.assertEquals("ping", text);
          process.write("pong");
        });
        process.eventHandler("SIGTERM", event -> {
          testContext.assertTrue(commandCtx == Vertx.currentContext());
          process.end(0);
        });
        latch.countDown();
      });
      manager.registerCommand(command, testContext.asyncAssertSuccess(v2 -> {
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
            ctx.setStdout(text -> {
              testContext.assertTrue(shellCtx == Vertx.currentContext());
              testContext.assertEquals("pong", text);
              testContext.assertTrue(ctx.sendEvent("SIGTERM"));
              testContext.assertFalse(ctx.sendEvent("WHATEVER"));
            });
            ctx.stdin.handle("ping");
          }));
        });
      }));
    });
  }

  @Test
  public void testSendEvent(TestContext context) {
    CommandManager manager = CommandManager.get(vertx);
    Command cmd = Command.command("foo");
    CountDownLatch latch = new CountDownLatch(1);
    cmd.processHandler(process -> {
      process.eventHandler("SIGTERM", v -> {
        process.end(0);
      });
      latch.countDown();
    });
    manager.registerCommand(cmd, context.asyncAssertSuccess(v -> {
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
        ctx.sendEvent("SIGTERM");
      }));
    }));
  }

  @Test
  public void testResize(TestContext context) {
    CommandManager manager = CommandManager.get(vertx);
    Command cmd = Command.command("foo");
    cmd.processHandler(process -> {
      context.assertEquals(20, process.width());
      context.assertEquals(10, process.height());
      process.eventHandler("SIGWINCH", v -> {
        context.assertEquals(25, process.width());
        context.assertEquals(15, process.height());
        process.end(0);
      });
      process.stdout().handle("ping");
    });
    manager.registerCommand(cmd, context.asyncAssertSuccess(v -> {
      manager.createProcess("foo", context.asyncAssertSuccess(job -> {
        Async async = context.async();
        TestProcessContext ctx = new TestProcessContext();
        ctx.endHandler(status -> {
          async.complete();
        });
        ctx.setWindowSize(20, 10);
        ctx.setStdout(text -> {
          ctx.setWindowSize(25, 15);
        });
        job.execute(ctx);
      }));
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
    manager.registerCommand(cmd1, context.asyncAssertSuccess(v1 -> {
      manager.registerCommand(cmd2, context.asyncAssertSuccess(v2 -> {
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
