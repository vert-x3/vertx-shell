package io.vertx.ext.shell;

import io.vertx.core.AbstractVerticle;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ShellVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    ShellServiceOptions options = new ShellServiceOptions(context.config());
    ShellService service = ShellService.create(vertx, options);
    service.start();
  }
}
