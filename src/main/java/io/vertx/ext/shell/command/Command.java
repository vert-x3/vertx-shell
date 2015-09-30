package io.vertx.ext.shell.command;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.cli.CLI;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.impl.CommandImpl;

/**
 * A shell command.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Command {

  /**
   * Create a new commmand, the command is responsible for managing the options and arguments via the
   * {@link CommandProcess#args() arguments}.
   *
   * @param name the command name
   * @return the command
   */
  static Command command(String name) {
    return new CommandImpl(name, null);
  }

  /**
   * Create a new commmand with its {@link io.vertx.core.cli.CLI} descriptor. This command can then retrieve the parsed
   * {@link CommandProcess#commandLine()} when it executes to know get the command arguments and options.
   *
   * @param cli the cli to use
   * @return the command
   */
  static Command command(CLI cli) {
    return new CommandImpl(cli.getName(), cli);
  }

  /**
   * @return the command name
   */
  String name();

  /**
   * Set a command process handler on the command, the process handler is called when the command is executed.
   *
   * @param handler the process handler
   * @return this command object
   */
  @Fluent
  Command processHandler(Handler<CommandProcess> handler);

  /**
   * Set the command completion handler, the completion handler when the user asks for contextual command line
   * completion, usually hitting the <i>tab</i> key.
   *
   * @param handler the completion handler
   * @return this command object
   */
  @Fluent
  Command completionHandler(Handler<Completion> handler);

}
