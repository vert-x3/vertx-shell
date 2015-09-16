package io.vertx.ext.shell.command;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.cli.CommandLine;
import io.vertx.ext.shell.Session;
import io.vertx.ext.shell.io.Stream;
import io.vertx.ext.shell.io.Tty;
import io.vertx.ext.shell.cli.CliToken;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CommandProcess extends Tty {

  /**
   * @return the current Vert.x instance
   */
  Vertx vertx();

  /**
   * @return the unparsed arguments tokens
   */
  List<CliToken> argsTokens();

  /**
   * @return the actual string arguments of the command
   */
  List<String> args();

  /**
   * @return the command line object or null
   */
  CommandLine commandLine();

  /**
   * @return the shell session
   */
  Session session();

  @Fluent
  CommandProcess setStdin(Stream stdin);

  @Fluent
  CommandProcess eventHandler(String event, Handler<Void> handler);

  @Fluent
  CommandProcess write(String text);

  /**
   * End the process with the exit status {@literal 0}
   */
  void end();

  /**
   * End the process.
   *
   * @param status the exit status.
   */
  void end(int status);

}
