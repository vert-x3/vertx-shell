package io.vertx.ext.shell.command;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.impl.CommandManagerImpl;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CommandManager {

  static CommandManager create(Vertx vertx) {
    return new CommandManagerImpl(vertx);
  }

  void registerCommand(Command command, Handler<AsyncResult<Void>> handler);

  void close();
}
