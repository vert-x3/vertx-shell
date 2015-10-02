package io.vertx.ext.shell.command;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.cli.CLI;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.impl.CommandBuilderImpl;

/**
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
  static CommandBuilder builder(String name) {
    return new CommandBuilderImpl(name, null);
  }

  /**
   * Create a new commmand with its {@link io.vertx.core.cli.CLI} descriptor. This command can then retrieve the parsed
   * {@link CommandProcess#commandLine()} when it executes to know get the command arguments and options.
   *
   * @param cli the cli to use
   * @return the command
   */
  static CommandBuilder builder(CLI cli) {
    return new CommandBuilderImpl(cli.getName(), cli);
  }

  /**
   * @return the command name
   */
  String name();

  /**
   * @return the command line interface, can be null
   */
  CLI cli();

  void process(CommandProcess process);

  void complete(Completion completion);

}
