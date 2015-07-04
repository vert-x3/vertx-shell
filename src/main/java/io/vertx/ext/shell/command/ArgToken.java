package io.vertx.ext.shell.command;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.shell.command.impl.ArgTokenImpl;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ArgToken {

  static ArgToken createText(String s) {
    return new ArgTokenImpl(true, s, s);
  }

  static ArgToken createBlank(String s) {
    return new ArgTokenImpl(false, s, s);
  }

  String raw();

  String value();

  boolean isText();

  boolean isBlank();

  public static List<ArgToken> tokenize(String s) {
    return ArgTokenImpl.tokenize(s);
  }
}
