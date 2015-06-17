package io.vertx.ext.shell.command.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.Execution;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CommandImpl implements Command {

  final Vertx vertx;
  final String name;
  Handler<Execution> handler;

  public CommandImpl(Vertx vertx, String name) {
    this.vertx = vertx;
    this.name = name;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public void setExecuteHandler(Handler<Execution> handler) {
    this.handler = handler;
  }

  @Override
  public void unregister() {

  }
}
