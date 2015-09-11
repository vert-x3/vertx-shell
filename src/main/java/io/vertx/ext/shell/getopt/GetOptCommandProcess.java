package io.vertx.ext.shell.getopt;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.shell.command.CommandProcess;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface GetOptCommandProcess extends CommandProcess {

  List<String> arguments();

  List<String> getOption(String name);

  @Override
  GetOptCommandProcess setStdin(Handler<String> stdin);

  @Override
  GetOptCommandProcess eventHandler(String event, Handler<Void> handler);

  @Override
  GetOptCommandProcess write(String text);

}
