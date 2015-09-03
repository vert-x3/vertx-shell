package io.vertx.ext.shell.command.impl;

import io.vertx.core.Handler;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CommandImpl implements Command {

  final String name;
  Handler<CommandProcess> processHandler;
  Handler<Completion> completeHandler;

  public CommandImpl(String name) {
    this.name = name;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public CommandImpl processHandler(Handler<CommandProcess> handler) {
    processHandler = handler;
    return this;
  }

  @Override
  public CommandImpl completeHandler(Handler<Completion> handler) {
    completeHandler = handler;
    return this;
  }
}
