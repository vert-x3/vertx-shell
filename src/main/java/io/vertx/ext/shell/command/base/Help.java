package io.vertx.ext.shell.command.base;

import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.registry.CommandRegistration;
import io.vertx.ext.shell.registry.CommandRegistry;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("help")
@Summary("Help")
public class Help implements Command {

  @Override
  public void process(CommandProcess process) {
    CommandRegistry manager = CommandRegistry.get(process.vertx());
    manager.registrations();
    process.write("available commands:\n");
    for (CommandRegistration command : manager.registrations()) {
      process.write(command.command().name()).write("\n");
    }
    process.end();
  }
}
