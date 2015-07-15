package io.vertx.ext.shell.process;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.shell.Tty;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ProcessContext {

  Tty tty();

  /**
   * End the process.
   *
   * @param status the termination code
   */
  void end(int status);

}
