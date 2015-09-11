package io.vertx.ext.shell;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ShellVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    ShellServiceOptions options = new ShellServiceOptions(context.config());
    ShellService service = ShellService.create(vertx, options);
    service.start(ar -> {
      if (ar.succeeded()) {
        startFuture.complete();
      } else {
        startFuture.fail(ar.cause());
      }
    });
  }
}
