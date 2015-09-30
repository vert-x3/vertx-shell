package io.vertx.ext.shell;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.base.ShellCommands;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.metrics.MetricsCommand;
import io.vertx.ext.shell.command.base.BusCommand;
import io.vertx.ext.shell.command.base.FileSystemCommand;
import io.vertx.ext.shell.command.base.LocalMapCommand;
import io.vertx.ext.shell.command.base.NetCommand;
import io.vertx.ext.shell.command.base.VerticleCommand;
import io.vertx.ext.shell.impl.ShellServiceImpl;
import io.vertx.ext.shell.registry.CommandRegistry;

/**
 * The shell service, provides a remotely accessible shell available via Telnet or SSH according to the
 * {@link io.vertx.ext.shell.ShellServiceOptions} configuration.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ShellService {

  static ShellService create(Vertx vertx, ShellServiceOptions options) {
    CommandRegistry registry = CommandRegistry.get(vertx);

    // Base commands
    registry.registerCommand(ShellCommands.echo());
    registry.registerCommand(FileSystemCommand.cd());
    registry.registerCommand(FileSystemCommand.pwd());
    registry.registerCommand(FileSystemCommand.ls());
    registry.registerCommand(ShellCommands.sleep());
    registry.registerCommand(ShellCommands.help());
    registry.registerCommand(NetCommand.ls());
    registry.registerCommand(LocalMapCommand.get());
    registry.registerCommand(LocalMapCommand.put());
    registry.registerCommand(LocalMapCommand.rm());
    registry.registerCommand(BusCommand.send());
    registry.registerCommand(BusCommand.tail());
    registry.registerCommand(VerticleCommand.ls());
    registry.registerCommand(VerticleCommand.deploy());
    registry.registerCommand(VerticleCommand.undeploy());
    registry.registerCommand(VerticleCommand.factories());

    // Metrics commands
    registry.registerCommand(MetricsCommand.ls());
    registry.registerCommand(MetricsCommand.info());

    // Register builtin commands so they are listed in help
    registry.registerCommand(Command.command("exit").processHandler(process -> {}));
    registry.registerCommand(Command.command("logout").processHandler(process -> {}));
    registry.registerCommand(Command.command("jobs").processHandler(process -> {}));
    registry.registerCommand(Command.command("fg").processHandler(process -> {
    }));
    registry.registerCommand(Command.command("bg").processHandler(process -> {
    }));

    return new ShellServiceImpl(vertx, options, registry);
  }

  /**
   * @return the command registry for this service
   */
  CommandRegistry getCommandRegistry();

  /**
   * Start the shell service, this is an asynchronous start.
   */
  default void start() {
    start(ar -> {});
  }

  /**
   * Start the shell service, this is an asynchronous start.
   *
   * @param startHandler handler for getting notified when service is started
   */
  void start(Handler<AsyncResult<Void>> startHandler);

  /**
   * Stop the shell service, this is an asynchronous stop.
   */
  default void stop() {
    stop(ar -> {
    });
  }

  /**
   * Stop the shell service, this is an asynchronous start.
   *
   * @param stopHandler handler for getting notified when service is stopped
   */
  void stop(Handler<AsyncResult<Void>> stopHandler);

}
