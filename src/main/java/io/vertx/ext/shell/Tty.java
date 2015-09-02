package io.vertx.ext.shell;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Tty {

  int width();

  int height();

  void setStdin(Stream stdin);

  Stream stdout();

  void eventHandler(String event, Handler<Void> handler);

}
