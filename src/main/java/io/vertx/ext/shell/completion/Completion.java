package io.vertx.ext.shell.completion;

import io.termd.core.util.Helper;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.shell.command.ArgToken;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Completion {

  static String findLongestCommonPrefix(Collection<String> values) {
    return Helper.fromCodePoints(io.termd.core.readline.Completion.findLongestCommonPrefix(
        values.stream().map(Helper::toCodePoints).collect(Collectors.toList())
    ));
  }

  String line();

  List<ArgToken> lineTokens();

  void complete(List<String> candidates);

  void complete(String value, boolean terminal);

}
