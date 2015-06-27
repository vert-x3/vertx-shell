package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandManager;
import io.vertx.ext.shell.command.impl.CliRequest;
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
  public void testFoo(TestContext context) {
    CommandManagerImpl mgr = (CommandManagerImpl) CommandManager.create(vertx);
    mgr.addCommand(Command.create("hello"), context.asyncAssertSuccess(v -> {
      CliRequest request = mgr.makeRequest("hello world");
      context.assertEquals(Arrays.asList("world"), request.getArguments());
    }));
  }

}
