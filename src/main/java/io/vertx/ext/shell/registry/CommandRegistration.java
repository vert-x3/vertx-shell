package io.vertx.ext.shell.registry;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.process.Process;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CommandRegistration {

  Command command();

  void complete(Completion completion);

  Process createProcess(List<CliToken> args);

}
