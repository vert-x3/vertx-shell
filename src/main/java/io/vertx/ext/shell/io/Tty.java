package io.vertx.ext.shell.io;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Tty {

  /**
   * @return the current width, the number of rows
   */
  int width();

  /**
   * @return the current height, the number of columns
   */
  int height();

  @Fluent
  Tty setStdin(Handler<String> stdin);

  Stream stdout();

  @Fluent
  Tty eventHandler(String event, Handler<Void> handler);

}
