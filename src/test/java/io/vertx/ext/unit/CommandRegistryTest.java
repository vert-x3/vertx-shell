package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.registry.CommandRegistry;
import io.vertx.ext.shell.registry.impl.CommandRegistryImpl;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class CommandRegistryTest {

  Vertx vertx = Vertx.vertx();

  @Test
  public void testEval(TestContext context) {
    CommandRegistry registry = CommandRegistry.get(vertx);
    CommandBuilder command = Command.builder("hello");
    command.processHandler(process -> {
      context.assertEquals(Arrays.asList(CliToken.createBlank(" "), CliToken.createText("world")), process.argsTokens());
      process.end(0);
    });
    registry.registerCommand(command.build(), context.asyncAssertSuccess(v -> {
      registry.createProcess("hello world", context.asyncAssertSuccess(job -> {
        Async async = context.async();
        TestProcessContext ctx = new TestProcessContext();
        ctx.endHandler(code -> {
          async.complete();
        });
        job.execute(ctx);
      }));
    }));
  }

  @Test
  public void testRegister(TestContext context) {
    CommandRegistry registry = CommandRegistry.get(vertx);
    CommandBuilder command = Command.builder("hello");
    registry.registerCommand(command.build(), context.asyncAssertSuccess(reg -> {
      registry.unregisterCommand("hello", context.asyncAssertSuccess(done -> {
        context.assertEquals(Collections.emptyList(), registry.registrations());
      }));
    }));
  }

  @Test
  public void testDuplicateRegistration(TestContext context) {
    CommandRegistry registry = CommandRegistry.get(vertx);
    CommandBuilder command = Command.builder("hello");
    registry.registerCommand(command.build(), context.asyncAssertSuccess(reg -> {
      registry.registerCommand(command.build(), context.asyncAssertFailure(err -> {
      }));
    }));
  }

  @Test
  public void testCloseRegistryOnVertxClose(TestContext context) {
    Vertx vertx = Vertx.vertx();
    CommandRegistryImpl registry = (CommandRegistryImpl) CommandRegistry.get(vertx);
    context.assertFalse(registry.isClosed());
    vertx.close(context.asyncAssertSuccess(v -> {
      context.assertTrue(registry.isClosed());
    }));
  }
}
