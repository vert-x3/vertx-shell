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
   * @return the current width, i.e the number of rows or {@literal -1} if unknown
   */
  int width();

  /**
   * @return the current height, i.e the number of columns or {@literal -1} if unknown
   */
  int height();

  @Fluent
  Tty setStdin(Stream stdin);

  @Fluent
  Tty setStdin(Handler<String> stdin);

  Stream stdout();

  @Fluent
  Tty eventHandler(String event, Handler<Void> handler);

}
