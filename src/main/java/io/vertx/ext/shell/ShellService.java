package io.vertx.ext.shell;

import io.termd.core.telnet.TelnetTtyConnection;
import io.termd.core.telnet.vertx.VertxTelnetBootstrap;
import io.termd.core.tty.TtyConnection;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxInternal;
import io.vertx.ext.shell.command.BaseCommands;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.impl.SSHConnector;
import io.vertx.ext.shell.registry.CommandRegistry;
import io.vertx.ext.shell.impl.Shell;

import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ShellService {

  static ShellService create(Vertx vertx, ShellServiceOptions options) {
    CommandRegistry mgr = CommandRegistry.get(vertx);
    mgr.registerCommand(BaseCommands.echo());
    mgr.registerCommand(BaseCommands.fs_cd());
    mgr.registerCommand(BaseCommands.fs_pwd());
    mgr.registerCommand(BaseCommands.fs_ls());
    mgr.registerCommand(BaseCommands.sleep());
    mgr.registerCommand(BaseCommands.help());
    mgr.registerCommand(BaseCommands.server_ls());
    mgr.registerCommand(BaseCommands.local_map_get());
    mgr.registerCommand(BaseCommands.local_map_put());
    mgr.registerCommand(BaseCommands.local_map_rm());
    mgr.registerCommand(BaseCommands.bus_send());
    mgr.registerCommand(BaseCommands.bus_tail());
    mgr.registerCommand(BaseCommands.verticle_ls());
    mgr.registerCommand(BaseCommands.verticle_deploy());
    mgr.registerCommand(BaseCommands.verticle_undeploy());
    mgr.registerCommand(BaseCommands.verticle_factories());

    // Register builtin commands so they are listed in help
    mgr.registerCommand(Command.command("exit").processHandler(process -> {}));
    mgr.registerCommand(Command.command("logout").processHandler(process -> {}));
    mgr.registerCommand(Command.command("jobs").processHandler(process -> {}));
    mgr.registerCommand(Command.command("fg").processHandler(process -> {}));
    mgr.registerCommand(Command.command("bg").processHandler(process -> {}));

    Consumer<TtyConnection> shellInitializer = conn -> {
      Shell shell = new Shell(vertx, conn, mgr);
      conn.setCloseHandler(v -> {
        shell.close();
      });
      shell.setWelcome(options.getWelcomeMessage());
      shell.init();
    };

    //
    return () -> {
      TelnetOptions telnetOptions = options.getTelnet();
      if (telnetOptions != null) {
        VertxTelnetBootstrap bootstrap = new VertxTelnetBootstrap(vertx, telnetOptions);
        bootstrap.start(() -> new TelnetTtyConnection(shellInitializer));

        SSHOptions sshOptions = options.getSSH();
        if (sshOptions != null) {
          SSHConnector connector = new SSHConnector(sshOptions);
          connector.start((VertxInternal) vertx, shellInitializer);
        }
      }
    };
  }

  void start() throws Exception;

}
