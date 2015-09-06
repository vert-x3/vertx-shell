package io.vertx.ext.shell.registry;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.registry.impl.CommandRegistryImpl;
import io.vertx.ext.shell.process.Process;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CommandRegistry {

  static CommandRegistry get(Vertx vertx) {
    return CommandRegistryImpl.get(vertx);
  }

  /**
   * @return the current command registrations
   */
  List<CommandRegistration> registrations();

  void createProcess(String s, Handler<AsyncResult<Process>> handler);

  void createProcess(List<CliToken> line, Handler<AsyncResult<Process>> handler);

  void complete(Completion completion);

  void registerCommand(Command command);

  void registerCommand(Command command, Handler<AsyncResult<CommandRegistration>> doneHandler);

  void unregisterCommand(String commandName);

  void unregisterCommand(String commandName, Handler<AsyncResult<Void>> doneHandler);

  void release();
}
