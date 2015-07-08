package io.vertx.ext.shell;

import io.vertx.codegen.annotations.VertxGen;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Completion {

  String text();

  void complete(List<String> candidates);

}
