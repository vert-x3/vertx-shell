package io.vertx.ext.shell.command.metrics;

import io.vertx.core.cli.annotations.Argument;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("metrics-info")
@Summary("Show metrics info for the current Vert.x instance in Json format")
public class MetricsInfo implements Command {

  private String name;

  @Argument(index = 0, argName = "name")
  @Description("the metrics name, can be a metrics prefix or a precise name")
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void process(CommandProcess process) {
    String name = process.args().get(0);
    MetricsService metrics = MetricsService.create(process.vertx());
    JsonObject snapshot = metrics.getMetricsSnapshot(name);
    process.write(snapshot.encodePrettily() + "\n");
    process.end();
  }
}
