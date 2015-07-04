package io.vertx.ext.shell.getopt;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.getopt.impl.GetOptCommandImpl;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface GetOptCommand {

  static GetOptCommand create(String name) {
    return new GetOptCommandImpl(name);
  }

  void processHandler(Handler<GetOptCommandProcess> handler);

  @Fluent
  GetOptCommand addOption(Option option);

  Option getOption(String name);

  Command build();

}
