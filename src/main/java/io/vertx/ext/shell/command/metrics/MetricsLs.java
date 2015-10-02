package io.vertx.ext.shell.command.metrics;

import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.ext.dropwizard.MetricsService;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("metrics-ls")
@Summary("List the known metrics for the current Vert.x instance")
public class MetricsLs implements Command {

  @Override
  public void process(CommandProcess process) {
    MetricsService metrics = MetricsService.create(process.vertx());
    metrics.metricsNames().forEach(name -> {
      process.write(name + "\n");
    });
    process.end();
  }
}
