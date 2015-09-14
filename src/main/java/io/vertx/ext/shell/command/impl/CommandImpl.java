package io.vertx.ext.shell.command.impl;

import io.vertx.core.Handler;
import io.vertx.core.cli.CLI;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CommandImpl implements Command {

  final String name;
  final CLI cli;
  public Handler<CommandProcess> processHandler;
  public Handler<Completion> completeHandler;

  public CommandImpl(String name, CLI cli) {
    this.name = name;
    this.cli = cli;
  }

  @Override
  public String name() {
    return name;
  }

  public CLI getCLI() {
    return cli;
  }

  @Override
  public CommandImpl processHandler(Handler<CommandProcess> handler) {
    processHandler = handler;
    return this;
  }

  @Override
  public CommandImpl completionHandler(Handler<Completion> handler) {
    completeHandler = handler;
    return this;
  }
}
