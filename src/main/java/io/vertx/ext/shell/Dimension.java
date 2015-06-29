package io.vertx.ext.shell;

import io.vertx.codegen.annotations.VertxGen;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Dimension {

  static Dimension create(int width, int height) {
    return new Dimension() {
      @Override
      public int width() {
        return width;
      }
      @Override
      public int height() {
        return height;
      }
    };
  }

  int width();

  int height();

}
