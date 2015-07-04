package io.vertx.ext.shell.command.impl;

import io.vertx.core.Handler;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CommandImpl implements Command {

  final String name;
  Handler<CommandProcess> handler;

  public CommandImpl(String name) {
    this.name = name;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public void processHandler(Handler<CommandProcess> handler) {
    this.handler = handler;
  }

  @Override
  public void unregister() {

  }
}
