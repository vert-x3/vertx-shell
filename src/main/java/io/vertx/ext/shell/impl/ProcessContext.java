package io.vertx.ext.shell.impl;

import io.vertx.ext.shell.Stream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface ProcessContext {

  void begin();

  void setStdin(Stream stdin);

  Stream stdout();

  void end(int code);

}
