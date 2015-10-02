package io.vertx.ext.shell.registry;

import io.vertx.codegen.annotations.GenIgnore;
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
 * A registry that contains the commands known by a shell.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CommandRegistry {

  /**
   * Get the registry for the Vert.x instance
   *
   * @param vertx the vertx instance
   * @return the registry
   */
  static CommandRegistry get(Vertx vertx) {
    return CommandRegistryImpl.get(vertx);
  }

  /**
   * @return the current command registrations
   */
  List<CommandRegistration> registrations();

  /**
   * Parses a command line and try to create a process.
   *
   * @param line the command line to parse
   * @param handler the handler to be notified about process creation
   */
  void createProcess(String line, Handler<AsyncResult<Process>> handler);

  /**
   * Try to create a process from the command line tokens.
   *
   * @param line the command line tokens
   * @param handler the handler to be notified about process creation
   */
  void createProcess(List<CliToken> line, Handler<AsyncResult<Process>> handler);

  /**
   * Perform completion, the completion argument will be notified of the completion progress.
   *
   * @param completion the completion object
   */
  void complete(Completion completion);

  /**
   * Register a command
   *
   * @param command the class of the command to register
   */
  @GenIgnore
  void registerCommand(Class<? extends Command> command);

  @GenIgnore
  void registerCommand(Class<? extends Command> command, Handler<AsyncResult<CommandRegistration>> doneHandler);

  /**
   * Register a command
   *
   * @param command the command to register
   */
  void registerCommand(Command command);

  void registerCommand(Command command, Handler<AsyncResult<CommandRegistration>> doneHandler);

  /**
   * Unregister a command.
   *
   * @param commandName the command name
   */
  void unregisterCommand(String commandName);

  void unregisterCommand(String commandName, Handler<AsyncResult<Void>> doneHandler);

}
