package io.vertx.ext.shell.io;

import io.vertx.codegen.annotations.VertxGen;

/**
 * An event emitted by a {@link Tty}
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public enum EventType {

  /**
   * Fired when the process is running and is stopped.
   */
  SIGTSTP,

  /**
   * Fired when an End Of File is encountered.
   */
  EOF,

  /**
   * Fired when the process is interrupted.
   */
  SIGINT,

  /**
   * Fired when the size of the terminal changes.
   */
  SIGWINCH,

  /**
   * Fired when the process is resumed.
   */
  SIGCONT

}
