package io.vertx.ext.shell;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.CommandManager;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.impl.ShellImpl;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Shell {

  static Shell create(Vertx vertx, CommandManager manager) {
    return new ShellImpl(vertx, manager);
  }

  void createJob(String s, Handler<AsyncResult<Job>> handler);

  void complete(Completion completion);


}
