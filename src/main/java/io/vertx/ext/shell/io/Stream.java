package io.vertx.ext.shell.io;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Stream extends Handler<String> {

  @Override
  void handle(String event);
}
