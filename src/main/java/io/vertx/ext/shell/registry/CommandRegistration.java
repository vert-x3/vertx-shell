package io.vertx.ext.shell.registry;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.process.Process;

import java.util.List;

/**
 * A registration of a command in the {@link CommandRegistry}
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CommandRegistration {

  /**
   * @return the registered command.
   */
  Command command();

  /**
   * Complete the command for the given completion.
   *
   * @param completion the completion
   */
  void complete(Completion completion);

  /**
   * Create a new process with the passed arguments.
   *
   * @param args the process arguments
   * @return the process
   */
  Process createProcess(List<CliToken> args);

}
