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
    helloCmd.setExecuteHandler(exec -> {
      exec.write("hello world\r\n");
      exec.end(0);
    });
    mgr.addCommand(helloCmd, ar -> {});

    Command helpCmd = Command.create("help");
    helpCmd.setExecuteHandler(exec -> {
      exec.write("available commands:\r\n");
      exec.write("hello\r\n");
      exec.write("help\r\n");
      exec.write("ls\r\n");
      exec.write("sleep\r\n");
      exec.end(0);
    });
    mgr.addCommand(helpCmd, ar -> {});

    Command sleepCmd = Command.create("sleep");
    sleepCmd.setExecuteHandler(exec -> {
      if (exec.arguments().isEmpty()) {
        exec.write("usage: sleep seconds\r\n");
        exec.end(0);
      } else {
        String arg = exec.arguments().get(0);
        int seconds = -1;
        try {
          seconds = Integer.parseInt(arg);
        } catch (NumberFormatException ignore) {
        }
        if (seconds > 0) {
          vertx.setTimer(seconds * 1000, id -> {
            exec.end(0);
          });
          return;
        }
        exec.end(0);
      }
    });
    mgr.addCommand(sleepCmd, ar -> {});

    Command lsCmd = Command.create("ls");
    lsCmd.setExecuteHandler(exec -> {
      vertx.fileSystem().readDir(".", ar -> {
        if (ar.succeeded()) {
          List<String> files = ar.result();
          for (String file : files) {
            exec.write(file + "\r\n");
          }
        } else {
          ar.cause().printStackTrace();
        }
        exec.end(0);
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
