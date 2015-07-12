package io.vertx.ext.shell.cli;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.shell.cli.impl.CliTokenImpl;

import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CliToken {

  static CliToken createText(String s) {
    return new CliTokenImpl(true, s, s);
  }

  static CliToken createBlank(String s) {
    return new CliTokenImpl(false, s, s);
  }

  String raw();

  String value();

  boolean isText();

  boolean isBlank();

  public static List<CliToken> tokenize(String s) {
    return CliTokenImpl.tokenize(s);
  }
}
