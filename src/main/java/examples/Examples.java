package examples;

import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.registry.CommandRegistry;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Examples {

  public void helloWorld(Vertx vertx) {

    Command command = Command.command("my-command");
    command.processHandler(process -> {

      // Write a message to the console
      process.write("Hello World");

      // End the process
      process.end();
    });

    // Register the command
    CommandRegistry registry = CommandRegistry.get(vertx);
    registry.registerCommand(command);
  }

  public void commandArgs(Command command) {
    command.processHandler(process -> {

      for (String arg : process.args()) {
        // Print each argument on the console
        process.write("Argument " + arg);
      }

      process.end();
    });
  }

  public void terminalSize(Command command) {
    command.processHandler(process -> {
      process.write("Current terminal size: (" + process.width() + ", " + process.height() + ")").end();
    });
  }

  public void asyncCommand(Command command) {
    command.processHandler(process -> {
      Vertx vertx = process.vertx();

      // Set a timer
      vertx.setTimer(1000, id -> {

        // End the command when the timer is fired
        process.end();
      });
    });
  }

  public void SIGINT(Command command) {
    command.processHandler(process -> {
      Vertx vertx = process.vertx();

      // Every second print a message on the console
      long periodicId = vertx.setPeriodic(1000, id -> {
        process.write("tick\n");
      });

      // When user press Ctrl+C: cancel the timer and end the process
      process.eventHandler("SIGINT", event -> {
        vertx.cancelTimer(periodicId);
        process.end();
      });
    });
  }
}
