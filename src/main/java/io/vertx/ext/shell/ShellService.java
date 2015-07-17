package io.vertx.ext.shell;

import io.termd.core.ssh.SshTtyConnection;
import io.termd.core.telnet.TelnetConnection;
import io.termd.core.telnet.TelnetHandler;
import io.termd.core.telnet.TelnetTtyConnection;
import io.termd.core.telnet.vertx.VertxTelnetBootstrap;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServerOptions;
import io.vertx.ext.shell.command.CommandManager;
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
    CommandManager mgr = CommandManager.get(vertx);
    return () -> {
      options.getConnectors().forEach(connectorOptions -> {
        if (connectorOptions instanceof TelnetOptions) {
          VertxTelnetBootstrap bootstrap = new VertxTelnetBootstrap(vertx, (NetServerOptions) connectorOptions);
          bootstrap.start(new Supplier<TelnetHandler>() {
            @Override
            public TelnetHandler get() {
              return new TelnetTtyConnection() {

                final Shell shell = new Shell(vertx, this, mgr);

                @Override
                protected void onOpen(TelnetConnection conn) {
                  super.onOpen(conn);
                }

                @Override
                protected void onClose() {
                  shell.close();
                  super.onClose();
                }
              };
            }
          });
        } else {
          try {
            SSHOptions sshOptions = (SSHOptions) connectorOptions;
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
      });
    };
  }

  void listen();

}
