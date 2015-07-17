package io.vertx.ext.shell.command;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.impl.CommandManagerImpl;
import io.vertx.ext.shell.process.*;
import io.vertx.ext.shell.process.Process;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CommandManager {

  static CommandManager get(Vertx vertx) {
    return CommandManagerImpl.get(vertx);
  }

  void createProcess(String s, Handler<AsyncResult<Process>> handler);

  void createProcess(List<CliToken> line, Handler<AsyncResult<Process>> handler);

  void complete(Completion completion);

  void registerCommand(Command command);

  void registerCommand(Command command, Handler<AsyncResult<Void>> handler);

  void release();
}
