package io.vertx.ext.shell.command.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandManager;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CommandManagerImpl implements CommandManager {

  private final Vertx vertx;
  private final ConcurrentHashMap<String, ManagedCommand> commandMap = new ConcurrentHashMap<>();

  public CommandManagerImpl(Vertx vertx) {
    this.vertx = vertx;
  }

  public Collection<ManagedCommand> commands() {
    return commandMap.values();
  }

  @Override
  public void registerCommand(Command command, Handler<AsyncResult<Void>> handler) {
    Context context = vertx.getOrCreateContext();
    commandMap.put(command.name(), new ManagedCommand(context, (CommandImpl) command));
    handler.handle(Future.succeededFuture());
  }

  public ManagedCommand getCommand(String name) {
    return commandMap.get(name);
  }

  @Override
  public void close() {

  }
}
