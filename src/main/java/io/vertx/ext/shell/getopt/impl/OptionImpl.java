package io.vertx.ext.shell.getopt.impl;

import io.vertx.ext.shell.getopt.Option;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class OptionImpl implements Option {

  final String name;
  final int arity;

  public OptionImpl(String name, int arity) {
    if (name == null) {
      throw new NullPointerException("Option name can't be null");
    }
    if (arity < 0) {
      throw new IllegalArgumentException("Arity " + arity + " must be >= 0");
    }
    this.name = name;
    this.arity = arity;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public int arity() {
    return arity;
  }
}
