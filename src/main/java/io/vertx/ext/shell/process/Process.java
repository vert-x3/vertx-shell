package io.vertx.ext.shell.process;

import io.vertx.codegen.annotations.VertxGen;

/**
 * A process managed by the shell.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Process {

  /**
   * Execute the process in the given context.
   *
   * @param context the context
   */
  void execute(ProcessContext context);

}
