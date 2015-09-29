package io.vertx.ext.shell.command.base;

import io.vertx.codegen.annotations.VertxGen;
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
    Command cmd = Command.command("verticle-ls");
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
    Command cmd = Command.command("verticle-deploy");
    cmd.processHandler(process -> {
      if (process.args().isEmpty()) {
        process.write("no verticle name specified\n").end();
        return;
      }
      String name = process.args().get(0);
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
    Command cmd = Command.command("verticle-undeploy");
    cmd.processHandler(process -> {
      if (process.args().isEmpty()) {
        process.write("no deployment ID specified\n").end();
        return;
      }
      String id = process.args().get(0);
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
    Command cmd = Command.command("verticle-factories");
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
