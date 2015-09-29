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
import io.vertx.ext.shell.command.base.ServerCommand;
import io.vertx.ext.shell.command.base.VerticleCommand;
import io.vertx.ext.shell.impl.ShellServiceImpl;
import io.vertx.ext.shell.registry.CommandRegistry;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ShellService {

  static ShellService create(Vertx vertx, ShellServiceOptions options) {
    CommandRegistry registry = CommandRegistry.get(vertx);
    registry.registerCommand(ShellCommands.echo());
    registry.registerCommand(FileSystemCommand.cd());
    registry.registerCommand(FileSystemCommand.pwd());
    registry.registerCommand(FileSystemCommand.ls());
    registry.registerCommand(ShellCommands.sleep());
    registry.registerCommand(ShellCommands.help());
    registry.registerCommand(ServerCommand.ls());
    registry.registerCommand(LocalMapCommand.get());
    registry.registerCommand(LocalMapCommand.put());
    registry.registerCommand(LocalMapCommand.rm());
    registry.registerCommand(BusCommand.send());
    registry.registerCommand(BusCommand.tail());
    registry.registerCommand(VerticleCommand.ls());
    registry.registerCommand(VerticleCommand.deploy());
    registry.registerCommand(VerticleCommand.undeploy());
    registry.registerCommand(VerticleCommand.factories());

    // Register builtin commands so they are listed in help
    registry.registerCommand(Command.command("exit").processHandler(process -> {}));
    registry.registerCommand(Command.command("logout").processHandler(process -> {}));
    registry.registerCommand(Command.command("jobs").processHandler(process -> {}));
    registry.registerCommand(Command.command("fg").processHandler(process -> {}));
    registry.registerCommand(Command.command("bg").processHandler(process -> {}));

    //
    registry.registerCommand(MetricsCommand.ls());
    registry.registerCommand(MetricsCommand.info());

    //
    return new ShellServiceImpl(vertx, options, registry);
  }

  default void start() {
    start(ar -> {});
  }

  void start(Handler<AsyncResult<Void>> startHandler);

  void close(Handler<AsyncResult<Void>> closeHandler);

}
