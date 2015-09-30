package io.vertx.ext.shell.command.base;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.Option;
import io.vertx.core.impl.Deployment;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.spi.VerticleFactory;
import io.vertx.ext.shell.command.Command;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface VerticleCommand {

  static Command ls() {
    Command cmd = Command.command(CLI.
            create("verticle-ls").
            setSummary("List all verticles").
            addOption(new Option().setArgName("help").setFlag(true).setShortName("h").setLongName("help"))
    );
    cmd.processHandler(process -> {
      VertxInternal vertx = (VertxInternal) process.vertx();
      for (String id : vertx.deploymentIDs()) {
        Deployment deployment = vertx.getDeployment(id);
        process.write(id + ": " + deployment.verticleIdentifier() + ", options=" + deployment.deploymentOptions().toJson() + "\n");

      }
      process.end();

    });
    return cmd;
  }

  static Command deploy() {
    Command cmd = Command.command(CLI.
            create("verticle-deploy").
            setSummary("Deploy a verticle").
            addOption(new Option().setArgName("help").setFlag(true).setShortName("h").setLongName("help")).
            addArgument(new Argument().setArgName("name").setDescription("the verticle name"))
    );
    cmd.processHandler(process -> {
      String name = process.commandLine().getArgumentValue("name");
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
    });
    return cmd;
  }

  static Command undeploy() {
    Command cmd = Command.command(CLI.
            create("verticle-undeploy").
            setSummary("Undeploy a verticle").
            addOption(new Option().setArgName("help").setFlag(true).setShortName("h").setLongName("help")).
            addArgument(new Argument().setArgName("id").setDescription("the verticle id"))
    );
    cmd.processHandler(process -> {
      String id = process.commandLine().getArgumentValue("id");
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
    });
    return cmd;
  }

  static Command factories() {
    Command cmd = Command.command(CLI.
            create("verticle-factories").
            setSummary("List all verticle factories").
            addOption(new Option().setArgName("help").setFlag(true).setShortName("h").setLongName("help"))
    );
    cmd.processHandler(process -> {
      VertxInternal vertx = (VertxInternal) process.vertx();
      for (VerticleFactory factory : vertx.verticleFactories()) {
        process.write(factory.getClass().getName() + ": prefix=" + factory.prefix() + ", order=" + factory.order() + ", requiresResolve=" + factory.requiresResolve() + "\n");
      }
      process.end();
    });
    return cmd;
  }
}
