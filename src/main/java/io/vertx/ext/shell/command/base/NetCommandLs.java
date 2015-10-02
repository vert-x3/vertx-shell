package io.vertx.ext.shell.command.base;

import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.core.http.impl.HttpServerImpl;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.net.impl.NetServerImpl;
import io.vertx.core.net.impl.ServerID;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("net-ls")
@Summary("List all servers")
public class NetCommandLs implements Command {

  @Override
  public void process(CommandProcess process) {
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
  }
}
