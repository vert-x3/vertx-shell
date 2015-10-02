package io.vertx.ext.shell.command.base;

import io.vertx.core.cli.annotations.Argument;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.io.EventType;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("bus-tail")
@Summary("Subscribe to an event bus address and logs received messages on the console")
public class BusTail implements Command {

  private String address;

  @Argument(index =  0, argName = "address")
  @Description("the bus address destination")
  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public void process(CommandProcess process) {
    MessageConsumer<Object> consumer = process.vertx().eventBus().consumer(address, msg -> {
      process.write("" + msg.body() + "\n");
    });
    process.eventHandler(EventType.SIGINT, done -> {
      consumer.unregister();
      process.end();
    });
  }
}
