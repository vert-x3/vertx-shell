package io.vertx.ext.shell.getopt;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.shell.getopt.impl.OptionImpl;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Option {

  static Option create(String name, int arity) {
    return new OptionImpl(name ,arity);
  }

  String name();

  int arity();

}
