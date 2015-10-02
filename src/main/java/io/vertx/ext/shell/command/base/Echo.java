package io.vertx.ext.shell.command.base;

import io.vertx.core.cli.CLI;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Echo implements Command {

  @Override
  public String name() {
    return "echo";
  }

  @Override
  public CLI cli() {
    // CLI does not support variable arguments yet
    return null;
  }

  @Override
  public void process(CommandProcess process) {
    boolean first = true;
    for (String token : process.args()) {
      if (!first) {
        process.write(" ");
      }
      process.write(token);
      first = false;
    }
    process.write("\n");
    process.end();
  }
}
