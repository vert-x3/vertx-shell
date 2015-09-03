package io.vertx.ext.shell;

import io.vertx.core.Vertx;
import io.vertx.core.file.FileProps;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.command.BaseCommands;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandManager;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.getopt.GetOptCommand;
import io.vertx.ext.shell.getopt.GetOptCommandProcess;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

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

    Command helpCmd = Command.command("help");
    helpCmd.processHandler(process -> {
      process.write("available commands:\n");
      process.write("echo\n");
      process.write("help\n");
      process.write("ls\n");
      process.write("sleep\n");
      process.write("window\n");
      process.end();
    });
    mgr.registerCommand(helpCmd);

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


    mgr.registerCommand(BaseCommands.echo());
    mgr.registerCommand(BaseCommands.ls());
    mgr.registerCommand(BaseCommands.sleep());

    // Expose the shell
    ShellService service = ShellService.create(vertx, new ShellServiceOptions().
        setTelnet(new TelnetOptions().setPort(5000)).
        setSSH(new SSHOptions().setPort(5001)));
    service.start();
  }
}
