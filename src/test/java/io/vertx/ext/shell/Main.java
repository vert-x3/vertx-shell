package io.vertx.ext.shell;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.shell.auth.ShiroAuthOptions;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.io.EventType;
import io.vertx.ext.shell.io.Stream;
import io.vertx.ext.shell.net.SSHOptions;
import io.vertx.ext.shell.net.TelnetOptions;
import io.vertx.ext.shell.registry.CommandRegistry;

/**
 * A simple class for testing from command line directly.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Main {

  public static void main(String[] args) throws Exception {

    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
        new DropwizardMetricsOptions().setEnabled(true)));

    CommandRegistry mgr = CommandRegistry.get(vertx);

    Command echoKeyboardCmd = Command.command("echo-keyboard");
    echoKeyboardCmd.processHandler(process -> {
      Stream stdout = process.stdout();
      process.setStdin(line -> {
        stdout.write("-> " + line + "\n");
      });
      process.eventHandler(EventType.SIGINT, v -> process.end());
    });
    mgr.registerCommand(echoKeyboardCmd);

    Command windowCmd = Command.command("window");
    windowCmd.processHandler(process -> {
      process.write("[" + process.width() + "," + process.height() + "]\n");
      process.eventHandler(EventType.SIGWINCH, v -> {
        process.write("[" + process.width() + "," + process.height() + "]\n");
      });
      process.eventHandler(EventType.SIGINT, v -> {
        process.end();
      });
    });
    mgr.registerCommand(windowCmd);

    // JS command
    // vertx.deployVerticle("command.js");

    // Expose the shell
    SSHOptions options = new SSHOptions().setPort(5001);
    options.setKeyPairOptions(new JksOptions().
        setPath("src/test/resources/server-keystore.jks").
        setPassword("wibble")).
        setShiroAuthOptions(
            new ShiroAuthOptions().
                setType(ShiroAuthRealmType.PROPERTIES).
                setConfig(new JsonObject().put("properties_path", "file:src/test/resources/test-auth.properties"))
    );
    ShellService service = ShellService.create(vertx, new ShellServiceOptions().
        setTelnetOptions(new TelnetOptions().setPort(5000)).
        setSSHOptions(options));
    service.start();

  }
}
