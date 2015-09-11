package io.vertx.ext.shell;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.BaseCommands;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.impl.ShellServiceImpl;
import io.vertx.ext.shell.registry.CommandRegistry;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ShellService {

  static ShellService create(Vertx vertx, ShellServiceOptions options) {
    CommandRegistry registry = CommandRegistry.get(vertx);
    registry.registerCommand(BaseCommands.echo());
    registry.registerCommand(BaseCommands.fs_cd());
    registry.registerCommand(BaseCommands.fs_pwd());
    registry.registerCommand(BaseCommands.fs_ls());
    registry.registerCommand(BaseCommands.sleep());
    registry.registerCommand(BaseCommands.help());
    registry.registerCommand(BaseCommands.server_ls());
    registry.registerCommand(BaseCommands.local_map_get());
    registry.registerCommand(BaseCommands.local_map_put());
    registry.registerCommand(BaseCommands.local_map_rm());
    registry.registerCommand(BaseCommands.bus_send());
    registry.registerCommand(BaseCommands.bus_tail());
    registry.registerCommand(BaseCommands.verticle_ls());
    registry.registerCommand(BaseCommands.verticle_deploy());
    registry.registerCommand(BaseCommands.verticle_undeploy());
    registry.registerCommand(BaseCommands.verticle_factories());

    // Register builtin commands so they are listed in help
    registry.registerCommand(Command.command("exit").processHandler(process -> {}));
    registry.registerCommand(Command.command("logout").processHandler(process -> {}));
    registry.registerCommand(Command.command("jobs").processHandler(process -> {}));
    registry.registerCommand(Command.command("fg").processHandler(process -> {}));
    registry.registerCommand(Command.command("bg").processHandler(process -> {}));

    //
    return new ShellServiceImpl(vertx, options, registry);
  }

  default void start() {
    start(ar -> {});
  }

  void start(Handler<AsyncResult<Void>> startHandler);

  void close(Handler<AsyncResult<Void>> closeHandler);

}
