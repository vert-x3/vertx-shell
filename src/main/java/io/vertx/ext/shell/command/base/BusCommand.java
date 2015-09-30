package io.vertx.ext.shell.command.base;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.io.EventType;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface BusCommand {

  static Command send() {
    Command cmd = Command.command("bus-send");
    cmd.processHandler(process -> {
      List<String> args = process.args();
      if (args.size() < 2) {
        process.write("usage: bus-send address message\n");
      } else {
        String address = args.get(0);
        String msg = args.get(1);
        process.vertx().eventBus().send(address, msg);
      }
      process.end();
    });
    return cmd;
  }

  static Command tail() {
    Command cmd = Command.command("bus-tail");
    cmd.processHandler(process -> {
      List<String> args = process.args();
      if (args.size() < 1) {
        process.write("usage: bus-tail address\n").end();
      } else {
        String address = args.get(0);
        MessageConsumer<Object> consumer = process.vertx().eventBus().consumer(address, msg -> {
          process.write("" + msg.body() + "\n");
        });
        process.eventHandler(EventType.SIGINT, done -> {
          consumer.unregister();
          process.end();
        });
      }
    });
    return cmd;
  }
}
