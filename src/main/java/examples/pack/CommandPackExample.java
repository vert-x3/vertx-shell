package examples.pack;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandResolver;
import io.vertx.ext.shell.spi.CommandResolverFactory;

import java.util.ArrayList;
import java.util.List;

public class CommandPackExample implements CommandResolverFactory {

  @Override
  public void resolver(Vertx vertx, Handler<AsyncResult<CommandResolver>> resolveHandler) {
    List<Command> commands = new ArrayList<>();

    // Add commands
    commands.add(Command.create(vertx, JavaCommandExample.class));

    // Add another command
    commands.add(CommandBuilder.command("another-command").processHandler(process -> {
      // Handle process
    }).build(vertx));

    // Resolve with the commands
    resolveHandler.handle(Future.succeededFuture(() -> commands));
  }
}
