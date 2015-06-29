package io.vertx.ext.shell.command;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.shell.Stream;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CommandProcess {

  List<String> arguments();

  List<String> getOption(String name);

  @Fluent
  CommandProcess setStdin(Stream stdin);

  @Fluent
  CommandProcess eventHandler(String event, Handler<Void> handler);

  Stream stdout();

  @Fluent
  CommandProcess write(String text);

  void end(int code);

}
