package io.vertx.ext.shell.command.impl;

import io.vertx.core.Handler;
import io.vertx.ext.shell.command.Option;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

import java.util.HashMap;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CommandImpl implements Command {

  final String name;
  final HashMap<String, Option> options = new HashMap<>();
  Handler<CommandProcess> handler;

  public CommandImpl(String name) {
    this.name = name;
  }

  @Override
  public CommandImpl addOption(Option option) {
    options.put(option.name(), option);
    return this;
  }

  @Override
  public Option getOption(String name) {
    return options.get(name);
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
