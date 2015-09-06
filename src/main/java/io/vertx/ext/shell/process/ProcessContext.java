package io.vertx.ext.shell.process;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.shell.Session;
import io.vertx.ext.shell.Tty;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ProcessContext {

  /**
   * @return the tty assocated with this process
   */
  Tty tty();

  /**
   * @return the shell session
   */
  Session session();

  /**
   * End the process.
   *
   * @param status the termination status
   */
  void end(int status);

}
