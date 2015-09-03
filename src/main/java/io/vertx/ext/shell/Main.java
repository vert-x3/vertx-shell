package io.vertx.ext.shell;

import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandManager;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Main {

  public static void main(String[] args) {

    Vertx vertx = Vertx.vertx();

    CommandManager mgr = CommandManager.get(vertx);

    Command echoKeyboardCmd = Command.command("echo-keyboard");
    echoKeyboardCmd.processHandler(process -> {
      Stream stdout = process.stdout();
      process.setStdin(line -> {
        stdout.handle("-> " + line + "\n");
      });
      process.eventHandler("SIGINT", v -> process.end());
    });
    mgr.registerCommand(echoKeyboardCmd);

    Command windowCmd = Command.command("window");
    windowCmd.processHandler(process -> {
      process.write("[" + process.width() + "," + process.height() + "]\n");
      process.eventHandler("SIGWINCH", v -> {
        process.write("[" + process.width() + "," + process.height() + "]\n");
      });
      process.eventHandler("SIGINT", v -> {
        process.end();
      });
    });
    mgr.registerCommand(windowCmd);

    // Expose the shell
    ShellService service = ShellService.create(vertx, new ShellServiceOptions().
        setTelnet(new TelnetOptions().setPort(5000)).
        setSSH(new SSHOptions().setPort(5001)));
    service.start();
  }
}
