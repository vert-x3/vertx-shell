package io.vertx.ext.shell.net.impl;

import io.termd.core.telnet.TelnetHandler;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;

import java.util.function.Supplier;

/**
 * Telnet server integration with Vert.x {@link io.vertx.core.net.NetServer}.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TelnetSocketHandler implements Handler<NetSocket> {

  final Vertx vertx;
  final Supplier<TelnetHandler> factory;

  public TelnetSocketHandler(Vertx vertx, Supplier<TelnetHandler> factory) {
    this.vertx = vertx;
    this.factory = factory;
  }

  @Override
  public void handle(final NetSocket socket) {
    TelnetHandler handler = factory.get();
    final VertxTelnetConnection connection = new VertxTelnetConnection(handler, Vertx.currentContext(), socket);
    socket.handler(event -> connection.receive(event.getBytes()));
    socket.closeHandler(event -> connection.onClose());
    connection.onInit();
  }
}
