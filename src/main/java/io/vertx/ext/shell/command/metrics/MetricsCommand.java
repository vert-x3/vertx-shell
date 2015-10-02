package io.vertx.ext.shell.command.metrics;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface MetricsCommand {

  static Command ls() {
    CommandBuilder cmd = Command.builder(CLI.
        create("metrics-ls").
        setDescription("List the known metrics for the current Vert.x instance"));
    cmd.processHandler(process -> {
      MetricsService metrics = MetricsService.create(process.vertx());
      metrics.metricsNames().forEach(name -> {
        process.write(name + "\n");
      });
      process.end();
    });
    return cmd.build();
  }

  static Command info() {
    CommandBuilder cmd = Command.builder(CLI.
        create("metrics-info").
        setDescription("Show metrics info for the current Vert.x instance in Json format").
        addArgument(new Argument().setArgName("name")));
    cmd.processHandler(process -> {
      String name = process.args().get(0);
      MetricsService metrics = MetricsService.create(process.vertx());
      JsonObject snapshot = metrics.getMetricsSnapshot(name);
      process.write(snapshot.encodePrettily() + "\n");
      process.end();
    });
    return cmd.build();
  }

}
