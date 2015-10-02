package io.vertx.ext.shell.command.base;

import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.core.impl.Deployment;
import io.vertx.core.impl.VertxInternal;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("verticle-ls")
@Summary("List all verticles")
public class VerticleLs implements Command {

  @Override
  public void process(CommandProcess process) {
    VertxInternal vertx = (VertxInternal) process.vertx();
    for (String id : vertx.deploymentIDs()) {
      Deployment deployment = vertx.getDeployment(id);
      process.write(id + ": " + deployment.verticleIdentifier() + ", options=" + deployment.deploymentOptions().toJson() + "\n");

    }
    process.end();
  }
}
