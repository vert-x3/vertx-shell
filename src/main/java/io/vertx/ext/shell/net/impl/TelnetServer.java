package io.vertx.ext.shell.net.impl;

import io.termd.core.telnet.TelnetTtyConnection;
import io.termd.core.telnet.vertx.TelnetSocketHandler;
import io.termd.core.tty.TtyConnection;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.ext.shell.net.TelnetOptions;

import java.util.function.Consumer;

/**
 * Encapsulate the Telnet server setup.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TelnetServer {

  private final Vertx vertx;
  private final TelnetOptions options;
  private Consumer<TtyConnection> handler;
  private NetServer server;

  public TelnetServer(Vertx vertx, TelnetOptions options) {
    this.vertx = vertx;
    this.options = options;
  }

  public Consumer<TtyConnection> getHandler() {
    return handler;
  }

  public TelnetServer setHandler(Consumer<TtyConnection> handler) {
    this.handler = handler;
    return this;
  }

  public void listen(Handler<AsyncResult<Void>> listenHandler) {
    if (server == null) {
      server = vertx.createNetServer(options);
      server.connectHandler(new TelnetSocketHandler(vertx, () -> new TelnetTtyConnection(handler)));
      server.listen(ar -> {
        if (ar.succeeded()) {
          listenHandler.handle(Future.succeededFuture());
        } else {
          listenHandler.handle(Future.failedFuture(ar.cause()));
        }
      });
    } else {
      listenHandler.handle(Future.failedFuture("Already started"));
    }
  }

  public void close(Handler<AsyncResult<Void>> listenHandler) {
    if (server != null) {
      server.close(listenHandler);
    } else {
      listenHandler.handle(Future.failedFuture("No started"));
    }
  }
}
