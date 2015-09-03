package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.registry.CommandRegistry;
import io.vertx.ext.shell.registry.impl.CommandRegistryImpl;
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
    CommandRegistryImpl mgr = (CommandRegistryImpl) CommandRegistry.get(vertx);
    Command command = Command.command("hello");
    command.processHandler(process -> {
      context.assertEquals(Arrays.asList(CliToken.createBlank(" "), CliToken.createText("world")), process.argsTokens());
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
