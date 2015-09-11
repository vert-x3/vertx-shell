package io.vertx.ext.shell.impl;

import io.termd.core.telnet.TelnetTtyConnection;
import io.termd.core.telnet.vertx.VertxTelnetBootstrap;
import io.termd.core.tty.TtyConnection;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.TelnetOptions;

import java.util.function.Consumer;

/**
 * Encapsulate the Telnet server setup.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TelnetServer {

  private final TelnetOptions options;
  private Consumer<TtyConnection> handler;
  private VertxTelnetBootstrap server;

  public TelnetServer(TelnetOptions options) {
    this.options = options;
  }

  public Consumer<TtyConnection> getHandler() {
    return handler;
  }

  public TelnetServer setHandler(Consumer<TtyConnection> handler) {
    this.handler = handler;
    return this;
  }

  public void listen(Vertx vertx) {
    server = new VertxTelnetBootstrap(vertx, options);
    server.start(() -> new TelnetTtyConnection(handler));
  }

  public void close() {
    if (server != null) {
      server.stop();
    }
  }
}
