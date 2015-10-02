package io.vertx.ext.shell.command.base;

import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.core.impl.Deployment;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.spi.VerticleFactory;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("verticle-factories")
@Summary("List all verticle factories")
public class VerticleFactories implements Command {

  @Override
  public void process(CommandProcess process) {
    VertxInternal vertx = (VertxInternal) process.vertx();
    for (VerticleFactory factory : vertx.verticleFactories()) {
      process.write(factory.getClass().getName() + ": prefix=" + factory.prefix() + ", order=" + factory.order() + ", requiresResolve=" + factory.requiresResolve() + "\n");
    }
    process.end();
  }
}
