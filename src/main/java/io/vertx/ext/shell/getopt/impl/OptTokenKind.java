package io.vertx.ext.shell.getopt.impl;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
public enum OptTokenKind {

  BLANK,

  OPT,

  LONG_OPT,

  TEXT;

  public OptToken create(String value) {
    return new OptToken(this, value);
  }

}
