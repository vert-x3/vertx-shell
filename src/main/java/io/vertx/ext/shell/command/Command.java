package io.vertx.ext.shell.command;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.impl.CommandImpl;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Command {

  static Command create(Vertx vertx, String name) {
    return new CommandImpl(vertx, name);
  }

  String name();

  void setExecuteHandler(Handler<Execution> handler);

  void unregister();

}
