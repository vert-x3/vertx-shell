package io.vertx.ext.shell.cli;

import io.vertx.ext.shell.cli.impl.CliTokenImpl;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
public enum CliTokenKind {

  BLANK,

  OPT,

  LONG_OPT,

  TEXT;

  public CliToken create(String value) {
    return new CliTokenImpl(this, value);
  }

}
