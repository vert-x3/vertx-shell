package io.vertx.ext.shell.command.base;

import io.vertx.core.cli.annotations.Argument;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("bus-send")
@Summary("Send a message to the event bus")
public class BusSend implements Command {

  private String address;
  private String message;

  @Argument(index =  0, argName = "address")
  @Description("the bus address destination")
  public void setAddress(String address) {
    this.address = address;
  }

  @Argument(index =  1, argName = "message")
  @Description("the message to send")
  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public void process(CommandProcess process) {
    process.vertx().eventBus().send(address, message);
    process.end();
  }
}
