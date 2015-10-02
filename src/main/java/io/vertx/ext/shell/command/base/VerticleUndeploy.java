package io.vertx.ext.shell.command.base;

import io.vertx.core.cli.annotations.Argument;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("verticle-undeploy")
@Summary("Undeploy a verticle")
public class VerticleUndeploy implements Command {

  private String id;

  @Argument(index = 0, argName = "id")
  @Description("the verticle id")
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public void process(CommandProcess process) {
    process.vertx().undeploy(id, ar -> {
      if (ar.succeeded()) {
        process.write("Undeployed " + id + "\n").end();
      } else {
        process.write("Could not undeploy " + id + "\n");
        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter(buffer);
        ar.cause().printStackTrace(writer);
        process.write(buffer.toString()).end();
      }
    });
  }
}
