package io.vertx.ext.shell.command.base;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.Option;
import io.vertx.core.http.impl.HttpServerImpl;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.net.impl.NetServerImpl;
import io.vertx.core.net.impl.ServerID;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;

import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface NetCommand {

  static Command ls() {
    CommandBuilder cmd = Command.builder(CLI.
            create("net-ls").
            setSummary("List all servers").
            addOption(new Option().setArgName("help").setFlag(true).setShortName("h").setLongName("help"))
    );
    cmd.processHandler(process -> {
      VertxInternal vertx = (VertxInternal) process.vertx();
      process.write("\nNet Servers:\n");
      for (Map.Entry<ServerID, NetServerImpl> server : vertx.sharedNetServers().entrySet()) {
        process.write(server.getKey().host + ":" + server.getKey().port + "\n");
      }
      process.write("\nHTTP Servers:\n");
      for (Map.Entry<ServerID, HttpServerImpl> server : vertx.sharedHttpServers().entrySet()) {
        process.write(server.getKey().host + ":" + server.getKey().port + "\n");
      }
      process.end();
    });
    return cmd.build();
  }

}
