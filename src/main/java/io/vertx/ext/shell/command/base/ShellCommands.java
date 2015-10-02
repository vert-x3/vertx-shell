package io.vertx.ext.shell.command.base;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;
import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.Option;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.io.EventType;
import io.vertx.ext.shell.registry.CommandRegistration;
import io.vertx.ext.shell.registry.CommandRegistry;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ShellCommands {

  static Command sleep() {
    class SleepImpl {

      void run(CommandProcess process) {
        if (process.commandLine().getArgumentValue(0) == null) {
          process.write("usage: sleep seconds\n");
          process.end();
        } else {
          String arg = process.commandLine().getArgumentValue(0);
          int seconds = -1;
          try {
            seconds = Integer.parseInt(arg);
          } catch (NumberFormatException ignore) {
          }
          scheduleSleep(process, seconds * 1000L);
        }
      }

      void scheduleSleep(CommandProcess process, long millis) {
        Vertx vertx = process.vertx();
        if (millis > 0) {
          long now = System.currentTimeMillis();
          AtomicLong remaining = new AtomicLong(-1);
          long id = process.vertx().setTimer(millis, v -> {
            process.end();
          });
          process.eventHandler(EventType.SIGINT, v -> {
            if (vertx.cancelTimer(id)) {
              process.end();
            }
          });
          process.eventHandler(EventType.SIGTSTP, v -> {
            if (vertx.cancelTimer(id)) {
              remaining.set(millis - (System.currentTimeMillis() - now));
            }
          });
          process.eventHandler(EventType.SIGCONT, v -> {
            scheduleSleep(process, remaining.get());
          });
        } else {
          process.end();
        }
      }
    }

    SleepImpl sleep = new SleepImpl();
    CommandBuilder sleepCmd = Command.builder(CLI.create("sleep").
        addArgument(new Argument().setArgName("seconds")).
        addOption(new Option().setArgName("help").setFlag(true).setShortName("h").setLongName("help")));
    sleepCmd.processHandler(sleep::run);
    return sleepCmd.build();
  }

  static Command echo() {
    CommandBuilder cmd = Command.builder("echo");
    cmd.processHandler(process -> {
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
    });
    return cmd.build();
  }

  static Command help() {
    CommandBuilder cmd = Command.builder("help");
    cmd.processHandler(process -> {
      CommandRegistry manager = CommandRegistry.get(process.vertx());
      manager.registrations();
      process.write("available commands:\n");
      for (CommandRegistration command : manager.registrations()) {
        process.write(command.command().name()).write("\n");
      }
      process.end();
    });
    return cmd.build();
  }
}
