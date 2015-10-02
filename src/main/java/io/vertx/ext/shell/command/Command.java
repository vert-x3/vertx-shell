package io.vertx.ext.shell.command;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.annotations.CLIConfigurator;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.impl.CommandBuilderImpl;

import java.util.Collections;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Command {

  @GenIgnore
  static Command create(Class<? extends Command> clazz) {

    CLI cli = CLIConfigurator.define(clazz);

    return new Command() {

      @Override
      public String name() {
        return cli.getName();
      }

      @Override
      public CLI cli() {
        return cli;
      }

      @Override
      public void process(CommandProcess process) {
        Command instance;
        try {
          instance = clazz.newInstance();
        } catch (Exception e) {
          process.end();
          return;
        }
        CLIConfigurator.inject(process.commandLine(), instance);
        instance.process(process);
      }

      @Override
      public void complete(Completion completion) {
        Command instance;
        try {
          instance = clazz.newInstance();
        } catch (Exception e) {
          completion.complete(Collections.emptyList());
          return;
        }
        instance.complete(completion);
      }
    };
  }

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
  default String name() {
    return null;
  }

  /**
   * @return the command line interface, can be null
   */
  default CLI cli() {
    return null;
  }

  void process(CommandProcess process);

  default void complete(Completion completion) {

  }

}
