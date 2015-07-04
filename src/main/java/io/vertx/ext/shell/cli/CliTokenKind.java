package io.vertx.ext.shell.cli;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
public enum CliTokenKind {

  EOF,

  BLANK,

  OPT,

  LONG_OPT,

  TEXT;

  public CliToken create(String value) {
    return new CliToken(this, value);
  }

}
