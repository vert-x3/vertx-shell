package io.vertx.ext.shell.command.impl;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.Option;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.Execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CommandImpl implements Command {

  final String name;
  final HashMap<String, Option> options = new HashMap<>();
  Handler<Execution> handler;

  public CommandImpl(String name) {
    this.name = name;
  }

  @Override
  public CommandImpl option(Option option) {
    options.put(option.name(), option);
    return this;
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
