package io.vertx.ext.shell.command.impl;

import io.vertx.core.Handler;
import io.vertx.core.cli.CLI;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandProcess;

import java.util.Collections;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CommandBuilderImpl implements CommandBuilder {

  final String name;
  final CLI cli;
  public Handler<CommandProcess> processHandler;
  public Handler<Completion> completeHandler;

  public CommandBuilderImpl(String name, CLI cli) {
    this.name = name;
    this.cli = cli;
  }

  @Override
  public CommandBuilderImpl processHandler(Handler<CommandProcess> handler) {
    processHandler = handler;
    return this;
  }

  @Override
  public CommandBuilderImpl completionHandler(Handler<Completion> handler) {
    completeHandler = handler;
    return this;
  }

  @Override
  public Command build() {
    return new Command() {
      @Override
      public String name() {
        return name;
      }
      @Override
      public CLI cli() {
        return cli;
      }

      @Override
      public void process(CommandProcess process) {
        processHandler.handle(process);
      }

      @Override
      public void complete(Completion completion) {
        if (completeHandler != null) {
          completeHandler.handle(completion);
        } else {
          Command.super.complete(completion);
        }
      }
    };
  }
}
