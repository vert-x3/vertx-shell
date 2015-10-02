package io.vertx.ext.shell.command.base;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.Option;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.io.EventType;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface BusCommand {

  static Command send() {
    CommandBuilder cmd = Command.builder(CLI.
            create("bus-send").
            setSummary("Send a message to the event bus").
            addOption(new Option().setArgName("help").setFlag(true).setShortName("h").setLongName("help")).
            addArgument(new Argument().setArgName("address").setIndex(0).setDefaultValue("the bus address destination")).
            addArgument(new Argument().setArgName("message").setIndex(1).setDefaultValue("the message"))
    );
    cmd.processHandler(process -> {
      String address = process.commandLine().getArgumentValue("address");
      String msg = process.commandLine().getArgumentValue("message");
      process.vertx().eventBus().send(address, msg);
      process.end();
    });
    return cmd.build();
  }

  static Command tail() {
    CommandBuilder cmd = Command.builder(CLI.
            create("bus-tail").
            setSummary("Subscribe to an event bus address and logs received messages on the console").
            addOption(new Option().setArgName("help").setFlag(true).setShortName("h").setLongName("help")).
            addArgument(new Argument().setArgName("address").setDefaultValue("the bus address to listen to"))
    );
    cmd.processHandler(process -> {
      String address = process.commandLine().getArgumentValue("address");
      MessageConsumer<Object> consumer = process.vertx().eventBus().consumer(address, msg -> {
        process.write("" + msg.body() + "\n");
      });
      process.eventHandler(EventType.SIGINT, done -> {
        consumer.unregister();
        process.end();
      });
    });
    return cmd.build();
  }
}
