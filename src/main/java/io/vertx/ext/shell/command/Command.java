package io.vertx.ext.shell.command;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.impl.CommandImpl;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Command {

  /**
   * Create a new commmand.
   *
   * @param name the command name
   * @return the command object
   */
  static Command command(String name) {
    return new CommandImpl(name);
  }

  String name();

  @Fluent
  Command processHandler(Handler<CommandProcess> handler);

  @Fluent
  Command completeHandler(Handler<Completion> handler);

}
