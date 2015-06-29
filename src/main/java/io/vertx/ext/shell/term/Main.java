package io.vertx.ext.shell.term;

import io.termd.core.telnet.TelnetConnection;
import io.termd.core.telnet.TelnetHandler;
import io.termd.core.telnet.TelnetTtyConnection;
import io.termd.core.telnet.vertx.VertxTelnetBootstrap;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.Shell;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandManager;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Main {

  public static void main(String[] args) {

    // Needed for telnet : find a better solution later
    System.setProperty("line.separator","\r\n");

    Vertx vertx = Vertx.vertx();

    CommandManager mgr = CommandManager.create(vertx);

    Command helloCmd = Command.create("hello");
    helloCmd.processHandler(process -> {
      process.write("hello world\r\n");
      process.end(0);
    });
    mgr.addCommand(helloCmd, ar -> {});

    Command helpCmd = Command.create("help");
    helpCmd.processHandler(process -> {
      process.write("available commands:\r\n");
      process.write("hello\r\n");
      process.write("help\r\n");
      process.write("ls\r\n");
      process.write("sleep\r\n");
      process.end(0);
    });
    mgr.addCommand(helpCmd, ar -> {});

    Command sleepCmd = Command.create("sleep");
    sleepCmd.processHandler(process -> {
      if (process.arguments().isEmpty()) {
        process.write("usage: sleep seconds\r\n");
        process.end(0);
      } else {
        String arg = process.arguments().get(0);
        int seconds = -1;
        try {
          seconds = Integer.parseInt(arg);
        } catch (NumberFormatException ignore) {
        }
        if (seconds > 0) {
          long id = vertx.setTimer(seconds * 1000, v -> {
            process.end(0);
          });
          process.signalHandler("SIGINT", v -> {
            if (vertx.cancelTimer(id)) {
              process.end(0);
            }
          });
          return;
        }
        process.end(0);
      }
    });
    mgr.addCommand(sleepCmd, ar -> {});

    Command lsCmd = Command.create("ls");
    lsCmd.processHandler(process -> {
      vertx.fileSystem().readDir(".", ar -> {
        if (ar.succeeded()) {
          List<String> files = ar.result();
          for (String file : files) {
            process.write(file + "\r\n");
          }
        } else {
          ar.cause().printStackTrace();
        }
        process.end(0);
      });
    });
    mgr.addCommand(lsCmd, ar -> {});

    Shell shell = Shell.create(vertx, mgr);

    VertxTelnetBootstrap bootstrap = new VertxTelnetBootstrap(vertx, "localhost", 5000);
    bootstrap.start(new Supplier<TelnetHandler>() {

      @Override
      public TelnetHandler get() {
        return new TelnetTtyConnection() {

          final ShellTty shellTty = new ShellTty(this, shell);

          @Override
          protected void onOpen(TelnetConnection conn) {
            super.onOpen(conn);
            shellTty.init();
          }

          @Override
          protected void onClose() {
            shellTty.close();
            super.onClose();
          }
        };
      }
    });
  }
}
