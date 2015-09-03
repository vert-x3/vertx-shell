package io.vertx.ext.shell;

import io.termd.core.ssh.SshTtyConnection;
import io.termd.core.telnet.TelnetConnection;
import io.termd.core.telnet.TelnetHandler;
import io.termd.core.telnet.TelnetTtyConnection;
import io.termd.core.telnet.vertx.VertxTelnetBootstrap;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.BaseCommands;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.registry.CommandRegistry;
import io.vertx.ext.shell.impl.Shell;
import org.apache.sshd.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;

import java.io.IOException;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ShellService {

  static ShellService create(Vertx vertx, ShellServiceOptions options) {
    CommandRegistry mgr = CommandRegistry.get(vertx);
    mgr.registerCommand(BaseCommands.echo());
    mgr.registerCommand(BaseCommands.fs_ls());
    mgr.registerCommand(BaseCommands.sleep());
    mgr.registerCommand(BaseCommands.help());
    mgr.registerCommand(BaseCommands.server_ls());
    mgr.registerCommand(BaseCommands.local_map_get());
    mgr.registerCommand(BaseCommands.local_map_put());
    mgr.registerCommand(BaseCommands.local_map_rm());
    mgr.registerCommand(BaseCommands.bus_send());
    mgr.registerCommand(BaseCommands.bus_tail());

    // Register noop commands so they are listed in help
    // but they are builtins
    mgr.registerCommand(Command.command("jobs").processHandler(process -> {}));
    mgr.registerCommand(Command.command("fg").processHandler(process -> {}));
    mgr.registerCommand(Command.command("bg").processHandler(process -> {
    }));

    //
    return () -> {
      TelnetOptions telnetOptions = options.getTelnet();
      if (telnetOptions != null) {
        VertxTelnetBootstrap bootstrap = new VertxTelnetBootstrap(vertx, telnetOptions);
        bootstrap.start(new Supplier<TelnetHandler>() {
          @Override
          public TelnetHandler get() {
            return new TelnetTtyConnection() {

              final Shell shell = new Shell(vertx, this, mgr);

              @Override
              protected void onOpen(TelnetConnection conn) {
                super.onOpen(conn);
                shell.setWelcome(options.getWelcomeMessage());
                shell.init();
              }

              @Override
              protected void onClose() {
                shell.close();
                super.onClose();
              }
            };
          }
        });
      }
      SSHOptions sshOptions = options.getSSH();
      if (sshOptions != null) {
        try {
          SshServer sshd = SshServer.setUpDefaultServer();
          sshd.setShellFactory(() -> new SshTtyConnection(conn -> {
            Shell shell = new Shell(vertx, conn, mgr);
            shell.setWelcome(options.getWelcomeMessage());
            shell.init();
          }));
          sshd.setHost(sshOptions.getHost());
          sshd.setPort(sshOptions.getPort());
          sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider("hostkey.ser"));
          sshd.setPasswordAuthenticator((username, password, session) -> true);
          sshd.start();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    };
  }

  void start();

}
