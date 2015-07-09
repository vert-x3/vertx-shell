package io.vertx.ext.shell.completion.impl;

import io.vertx.ext.shell.completion.Entry;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class EntryImpl implements Entry {

  final String value;
  final boolean terminal;

  public EntryImpl(String value, boolean terminal) {
    this.value = value;
    this.terminal = terminal;
  }

  @Override
  public String value() {
    return value;
  }

  @Override
  public boolean isTerminal() {
    return terminal;
  }
}
