package io.vertx.ext.unit;

import io.vertx.core.Vertx;
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
public class CommandTest {

  Vertx vertx = Vertx.vertx();

  @Test
  public void testSomething(TestContext context) {
    CommandManager manager = CommandManager.create(vertx);
    Command cmd = Command.create("foo");
    cmd.setExecuteHandler(ctx -> {
      ctx.end(3);
    });
    manager.addCommand(cmd, context.asyncAssertSuccess(v -> {
      Shell shell = Shell.create(vertx, manager);
      shell.createProcess("foo", context.asyncAssertSuccess(process -> {
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
    cmd.setExecuteHandler(ctx -> ctx.setStdin(data -> {
      context.assertEquals("hello_world", data);
      ctx.end(0);
    }));
    manager.addCommand(cmd, context.asyncAssertSuccess(v -> {
      Shell shell = Shell.create(vertx, manager);
      Async async = context.async();
      shell.createProcess("foo", context.asyncAssertSuccess(process -> {
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
    cmd.setExecuteHandler(ctx -> {
      ctx.stdout().handle("bye_world");
      ctx.end(0);
    });
    manager.addCommand(cmd, context.asyncAssertSuccess(v -> {
      Shell shell = Shell.create(vertx, manager);
      shell.createProcess("foo", context.asyncAssertSuccess(process -> {
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
}
