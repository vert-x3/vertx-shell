package io.vertx.ext.shell.command.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
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

  public CliRequest makeRequest(String s) {
    ListIterator<CliToken> tokens = CliToken.tokenize(s).collect(Collectors.toList()).listIterator();
    while (tokens.hasNext()) {
      CliToken token = tokens.next();
      switch (token.getKind()) {
        case TEXT:
          CommandContext ctx = commandMap.get(token.getValue());
          if (ctx == null) {
            throw new IllegalArgumentException(token.getValue() + ": command not found");
          }
          CliParser parser = new CliParser(ctx.command);
          return parser.parse(tokens);
        case BLANK:
          break;
        default:
          throw new IllegalArgumentException("Bad line " + s);
      }
    }
    throw new IllegalArgumentException();
  }

  @Override
  public void close() {

  }
}
