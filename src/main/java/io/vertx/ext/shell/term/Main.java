package io.vertx.ext.shell.term;

import io.termd.core.readline.KeyDecoder;
import io.termd.core.readline.Keymap;
import io.termd.core.readline.Readline;
import io.termd.core.telnet.TelnetConnection;
import io.termd.core.telnet.TelnetHandler;
import io.termd.core.telnet.TelnetTtyConnection;
import io.termd.core.telnet.vertx.VertxTelnetBootstrap;
import io.termd.core.tty.TtyConnection;
import io.termd.core.util.Helper;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.Job;
import io.vertx.ext.shell.Shell;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandManager;

import java.io.InputStream;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Main {

  public static void main(String[] args) {

    Vertx vertx = Vertx.vertx();

    CommandManager mgr = CommandManager.create(vertx);

    Command helloCmd = Command.create("hello");
    helloCmd.setExecuteHandler(exec -> {
      exec.write("hello world\r\n");
      exec.end(0);
    });
    mgr.addCommand(helloCmd, ar -> {});

    Shell shell = Shell.create(vertx, mgr);

    VertxTelnetBootstrap bootstrap = new VertxTelnetBootstrap(vertx, "localhost", 5000);
    bootstrap.start(new Supplier<TelnetHandler>() {

      @Override
      public TelnetHandler get() {
        return new TelnetTtyConnection() {
          @Override
          protected void onOpen(TelnetConnection conn) {
            super.onOpen(conn);

            //
            InputStream inputrc = KeyDecoder.class.getResourceAsStream("inputrc");
            Keymap keymap = new Keymap(inputrc);
            Readline readline = new Readline(keymap);
            for (io.termd.core.readline.Function function : Helper.loadServices(Thread.currentThread().getContextClassLoader(), io.termd.core.readline.Function.class)) {
              readline.addFunction(function);
            }
            write("Welcome to vertx-shell\r\n\r\n");
            read(readline);
          }

          public void read(Readline readline) {
            readline.readline(this, "% ", line -> {
              shell.createJob(line, ar -> {
                if (ar.succeeded()) {
                  Job job = ar.result();
                  job.setStdout(this::write);
                  job.endHandler(code -> {
                    read(readline);
                  });
                  job.run();
                } else {
                  ar.cause().printStackTrace();
                  read(readline);
                }
              });
            });
          }
        };
      }
    });
  }
}
