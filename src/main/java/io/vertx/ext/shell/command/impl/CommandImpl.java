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
  public void processHandler(Handler<CommandProcess> handler) {
    processHandler = handler;
  }

  @Override
  public void completeHandler(Handler<Completion> handler) {
    completeHandler = handler;
  }

  @Override
  public void unregister() {

  }
}
