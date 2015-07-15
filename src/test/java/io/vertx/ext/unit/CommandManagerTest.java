package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandManager;
import io.vertx.ext.shell.command.impl.CommandManagerImpl;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class CommandManagerTest {

  Vertx vertx = Vertx.vertx();

  @Test
  public void testEval(TestContext context) {
    CommandManagerImpl mgr = (CommandManagerImpl) CommandManager.create(vertx);
    Command command = Command.create("hello");
    command.processHandler(process -> {
      context.assertEquals(Arrays.asList(CliToken.createBlank(" "), CliToken.createText("world")), process.args());
      process.end(0);
    });
    mgr.registerCommand(command, context.asyncAssertSuccess(v -> {
      mgr.createProcess("hello world", context.asyncAssertSuccess(job -> {
        Async async = context.async();
        TestProcessContext ctx = new TestProcessContext();
        ctx.endHandler(code -> {
          async.complete();
        });
        job.execute(ctx);
      }));
    }));
  }

}
