package io.vertx.ext.shell;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.shell.Dimension;
import io.vertx.ext.shell.Stream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Tty {

  Dimension windowSize();

  void setStdin(Stream stdin);

  Stream stdout();

  void eventHandler(String event, Handler<Void> handler);

}
