package io.vertx.ext.shell.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.*;
import io.vertx.ext.shell.Job;
import io.vertx.ext.shell.cli.CliParser;
import io.vertx.ext.shell.cli.CliRequest;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.command.CommandManager;
import io.vertx.ext.shell.command.impl.CommandContext;
import io.vertx.ext.shell.command.impl.CommandManagerImpl;

import java.util.ListIterator;
import java.util.stream.Collectors;

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
  public void createProcess(String name, Handler<AsyncResult<Job>> handler) {

    CommandContext commandCtx = manager.getCommand(name);

    if (commandCtx != null) {
      JobProcess process = new JobProcess(vertx, commandCtx.createProcess());
      handler.handle(Future.succeededFuture(process));
    } else {
      handler.handle(Future.failedFuture("Command " + name + " does not exist"));
    }
  }

  public CliRequest makeRequest(String s) {
    ListIterator<CliToken> tokens = CliToken.tokenize(s).collect(Collectors.toList()).listIterator();
    while (tokens.hasNext()) {
      CliToken token = tokens.next();
      switch (token.getKind()) {
        case TEXT:
          CommandContext ctx = manager.getCommand(token.getValue());
          if (ctx == null) {
            throw new IllegalArgumentException(token.getValue() + ": command not found");
          }
          CliParser parser = new CliParser(ctx.command());
          return parser.parse(tokens);
        case BLANK:
          break;
        default:
          throw new IllegalArgumentException("Bad line " + s);
      }
    }
    throw new IllegalArgumentException();
  }
}
