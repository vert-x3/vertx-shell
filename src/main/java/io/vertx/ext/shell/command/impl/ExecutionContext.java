package io.vertx.ext.shell.command.impl;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.ext.shell.Stream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface ExecutionContext {

  void begin();

  @Fluent
  ExecutionContext setStdin(Stream stdin);

  Stream stdout();

  void end(int code);

}
