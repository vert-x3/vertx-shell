package io.vertx.ext.shell.command;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.shell.cli.Completion;

/**
 * A shell command.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CommandBuilder {

  /**
   * Set a command process handler on the command, the process handler is called when the command is executed.
   *
   * @param handler the process handler
   * @return this command object
   */
  @Fluent
  CommandBuilder processHandler(Handler<CommandProcess> handler);

  /**
   * Set the command completion handler, the completion handler when the user asks for contextual command line
   * completion, usually hitting the <i>tab</i> key.
   *
   * @param handler the completion handler
   * @return this command object
   */
  @Fluent
  CommandBuilder completionHandler(Handler<Completion> handler);

  Command build();

}
