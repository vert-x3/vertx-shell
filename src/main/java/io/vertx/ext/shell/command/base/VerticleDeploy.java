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
@Name("verticle-deploy")
@Summary("Deploy a verticle")
public class VerticleDeploy implements Command {

  private String name;

  @Argument(index = 0, argName = "name")
  @Description("the verticle name")
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void process(CommandProcess process) {
    process.vertx().deployVerticle(name, ar -> {
      if (ar.succeeded()) {
        process.write("Deployed " + ar.result() + "\n").end();
      } else {
        process.write("Could not deploy " + name + "\n");
        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter(buffer);
        ar.cause().printStackTrace(writer);
        process.write(buffer.toString()).end();
      }
    });
  }
}
