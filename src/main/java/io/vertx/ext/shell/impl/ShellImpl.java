package io.vertx.ext.shell.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.*;
import io.vertx.ext.shell.Process;
import io.vertx.ext.shell.command.CommandManager;
import io.vertx.ext.shell.command.impl.CommandContext;
import io.vertx.ext.shell.command.impl.CommandManagerImpl;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ShellImpl implements Shell {

  final Vertx vertx;
  final CommandManagerImpl manager;

  public ShellImpl(Vertx vertx, CommandManager manager) {
    this.vertx = vertx;
    this.manager = (CommandManagerImpl) manager;
  }

  @Override
  public void createProcess(String name, Handler<AsyncResult<Process>> handler) {

    CommandContext commandCtx = manager.getCommand(name);

    if (commandCtx != null) {
      ProcessImpl process = new ProcessImpl(vertx, commandCtx);
      handler.handle(Future.succeededFuture(process));
    } else {
      handler.handle(Future.failedFuture("Command " + name + " does not exist"));
    }

  }
}
