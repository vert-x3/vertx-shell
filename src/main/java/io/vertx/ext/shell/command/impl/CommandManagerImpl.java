package io.vertx.ext.shell.command.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.cli.CliParser;
import io.vertx.ext.shell.cli.CliRequest;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandManager;

import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CommandManagerImpl implements CommandManager {

  private final Vertx vertx;
  private final ConcurrentHashMap<String, CommandContext> commandMap = new ConcurrentHashMap<>();

  public CommandManagerImpl(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  public void addCommand(Command command, Handler<AsyncResult<Void>> handler) {
    Context context = vertx.getOrCreateContext();
    commandMap.put(command.name(), new CommandContext(context, (CommandImpl) command));
    handler.handle(Future.succeededFuture());
  }

  public CommandContext getCommand(String name) {
    return commandMap.get(name);
  }

  @Override
  public void close() {

  }
}
