package io.vertx.ext.unit;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.Dimension;
import io.vertx.ext.shell.Shell;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandManager;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class ShellTest {

  Vertx vertx = Vertx.vertx();

  @Test
  public void testRun(TestContext context) {
    CommandManager manager = CommandManager.create(vertx);
    Command cmd = Command.create("foo");
    cmd.processHandler(process -> {
      process.end(3);
    });
    manager.registerCommand(cmd, context.asyncAssertSuccess(v -> {
      Shell shell = Shell.create(vertx, manager);
      shell.createJob("foo", context.asyncAssertSuccess(process -> {
        process.endHandler(code -> {
          context.assertEquals(3, code);
        });
        process.run();
      }));
    }));
  }

  @Test
  public void testStdin(TestContext context) {
    CommandManager manager = CommandManager.create(vertx);
    Command cmd = Command.create("foo");
    cmd.processHandler(process -> process.setStdin(data -> {
      context.assertEquals("hello_world", data);
      process.end(0);
    }));
    manager.registerCommand(cmd, context.asyncAssertSuccess(v -> {
      Shell shell = Shell.create(vertx, manager);
      Async async = context.async();
      shell.createJob("foo", context.asyncAssertSuccess(process -> {
        process.endHandler(code -> {
          context.assertEquals(0, code);
          async.complete();
        });
        process.run(v2 -> {
          process.stdin().handle("hello_world");
        });
      }));
    }));
  }

  @Test
  public void testStdout(TestContext context) {
    CommandManager manager = CommandManager.create(vertx);
    Command cmd = Command.create("foo");
    cmd.processHandler(process -> {
      process.stdout().handle("bye_world");
      process.end(0);
    });
    manager.registerCommand(cmd, context.asyncAssertSuccess(v -> {
      Shell shell = Shell.create(vertx, manager);
      shell.createJob("foo", context.asyncAssertSuccess(process -> {
        Async async = context.async();
        LinkedList<String> data = new LinkedList<>();
        process.setStdout(data::add);
        process.endHandler(code -> {
          context.assertEquals(0, code);
          context.assertEquals(Arrays.asList("bye_world"), data);
          async.complete();
        });
        process.run();
      }));
    }));
  }

  @Test
  public void testVertxContext(TestContext testContext) throws Exception {
    Context commandCtx = vertx.getOrCreateContext();
    Context shellCtx = vertx.getOrCreateContext();
    CommandManager manager = CommandManager.create(vertx);
    Async async = testContext.async();
    commandCtx.runOnContext(v1 -> {
      Command command = Command.create("foo");
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
      });
      manager.registerCommand(command, testContext.asyncAssertSuccess(v2 -> {
        shellCtx.runOnContext(v3 -> {
          Shell shell = Shell.create(vertx, manager);
          shell.createJob("foo", testContext.asyncAssertSuccess(process -> {
            testContext.assertTrue(shellCtx == Vertx.currentContext());
            process.endHandler(code -> {
              testContext.assertTrue(shellCtx == Vertx.currentContext());
              async.complete();
            });
            process.run(v4 -> {
              testContext.assertTrue(shellCtx == Vertx.currentContext());
              process.stdin().handle("ping");
              process.setStdout(text -> {
                testContext.assertTrue(shellCtx == Vertx.currentContext());
                testContext.assertEquals("pong", text);
                testContext.assertTrue(process.sendEvent("SIGTERM"));
                testContext.assertFalse(process.sendEvent("WHATEVER"));
              });
            });
          }));
        });
      }));
    });
  }

  @Test
  public void testSendEvent(TestContext context) {
    CommandManager manager = CommandManager.create(vertx);
    Command cmd = Command.create("foo");
    cmd.processHandler(process -> {
      process.eventHandler("SIGTERM", v -> {
        process.end(0);
      });
    });
    manager.registerCommand(cmd, context.asyncAssertSuccess(v -> {
      Shell shell = Shell.create(vertx, manager);
      shell.createJob("foo", context.asyncAssertSuccess(process -> {
        Async async = context.async();
        process.endHandler(status -> {
          async.complete();
        });
        process.run(v2 -> {
          process.sendEvent("SIGTERM");
        });
      }));
    }));
  }

  @Test
  public void testResize(TestContext context) {
    CommandManager manager = CommandManager.create(vertx);
    Command cmd = Command.create("foo");
    cmd.processHandler(process -> {
      context.assertEquals(20, process.windowSize().width());
      context.assertEquals(10, process.windowSize().height());
      process.eventHandler("RESIZE", v -> {
        context.assertEquals(25, process.windowSize().width());
        context.assertEquals(15, process.windowSize().height());
        process.end(0);
      });
      process.stdout().handle("ping");
    });
    manager.registerCommand(cmd, context.asyncAssertSuccess(v -> {
      Shell shell = Shell.create(vertx, manager);
      shell.createJob("foo", context.asyncAssertSuccess(process -> {
        Async async = context.async();
        process.endHandler(status -> {
          async.complete();
        });
        process.setWindowSize(Dimension.create(20, 10));
        process.setStdout(text -> {
          process.setWindowSize(Dimension.create(25, 15));
        });
        process.run();
      }));
    }));
  }

  // Just some proof of concept, there are issues because we there is a timing
  // problem when wiring processes
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
          process2.setStdout(text -> {
            context.assertEquals("<|hello|>", text);
            async.complete();
          });
          process2.run(a1 -> {
            shell.createJob("cmd1", context.asyncAssertSuccess(process1 -> {
              process1.setStdout(process2.stdin());
              process1.run(a2 -> {
                process1.stdin().handle("hello");
              });
            }));
          });
        }));
      }));
    }));
  }
}
