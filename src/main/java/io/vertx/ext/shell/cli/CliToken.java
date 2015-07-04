package io.vertx.ext.shell.cli;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.shell.cli.impl.CliTokenImpl;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CliToken {

  CliTokenKind getKind();

  String getRaw();

  String getValue();

  public static List<CliToken> tokenize(String s) {
    return CliTokenImpl.tokenize(s);
  }
}
