package io.vertx.ext.shell.impl;

import io.vertx.core.Handler;
import io.vertx.ext.shell.Dimension;
import io.vertx.ext.shell.Stream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface ProcessContext {

  void begin();

  Dimension windowSize();

  void setStdin(Stream stdin);

  void eventHandler(String event, Handler<Void> handler);

  Stream stdout();

  void end(int code);

}
