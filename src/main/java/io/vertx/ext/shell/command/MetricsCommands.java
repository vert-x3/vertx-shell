package io.vertx.ext.shell.command;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.MetricsService;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface MetricsCommands {

  static Command ls() {
    Command cmd = Command.command("metrics-ls");
    cmd.processHandler(process -> {
      MetricsService metrics = MetricsService.create(process.vertx());
      metrics.getMetricsNames().forEach(name -> {
        process.write(name + "\n");
      });
      process.end();
    });
    return cmd;
  }

  static Command info() {
    Command cmd = Command.command("metrics-info", CLI.create("metrics-info").addArgument(new Argument().setArgName("name")));
    cmd.processHandler(process -> {
      String name = process.args().get(0);
      MetricsService metrics = MetricsService.create(process.vertx());
      JsonObject snapshot = metrics.getMetricsSnapshot(name);
      process.write(snapshot.encodePrettily() + "\n");
      process.end();
    });
    return cmd;
  }

}
