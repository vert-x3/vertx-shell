package io.vertx.ext.shell;

import io.termd.core.telnet.TelnetConnection;
import io.termd.core.telnet.TelnetHandler;
import io.termd.core.telnet.TelnetTtyConnection;
import io.termd.core.telnet.vertx.VertxTelnetBootstrap;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.CommandManager;
import io.vertx.ext.shell.impl.Shell;

import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ShellService {

  static ShellService create(Vertx vertx, CommandManager mgr, int port) {
    return () -> {
      VertxTelnetBootstrap bootstrap = new VertxTelnetBootstrap(vertx, "localhost", port);
      bootstrap.start(new Supplier<TelnetHandler>() {

        @Override
        public TelnetHandler get() {
          return new TelnetTtyConnection() {

            final Shell shell = new Shell(vertx, this, mgr);

            @Override
            protected void onOpen(TelnetConnection conn) {
              super.onOpen(conn);
              shell.setWelcome("Welcome to Vert.x Shell\r\n\r\n");
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
    };
  }

  void listen();

}
