package io.vertx.ext.shell.completion;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.shell.completion.impl.EntryImpl;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Entry {

  static Entry entry(boolean terminal, String value) {
    return new EntryImpl(value, terminal);
  }

  static Entry terminalEntry(String value) {
    return new EntryImpl(value, true);
  }

  static Entry nonTerminalEntry(String value) {
    return new EntryImpl(value, false);
  }

  String value();

  boolean isTerminal();

}
