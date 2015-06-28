package io.vertx.ext.shell.command;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.shell.Stream;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Execution {

  List<String> arguments();

  List<String> getOption(String name);

  @Fluent
  Execution setStdin(Stream stdin);

  Stream stdout();

  @Fluent
  Execution write(String text);

  void end(int code);

}
