/*
 * Copyright 2015 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 *
 *
 * Copyright (c) 2015 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 *
 */

package io.vertx.rxjava.ext.shell.cli;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import java.util.List;

/**
 * A parsed token in the command line interface.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.cli.CliToken original} non RX-ified interface using Vert.x codegen.
 */

public class CliToken {

  final io.vertx.ext.shell.cli.CliToken delegate;

  public CliToken(io.vertx.ext.shell.cli.CliToken delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Create a text token.
   * @param text the text
   * @return the token
   */
  public static CliToken createText(String text) { 
    CliToken ret= CliToken.newInstance(io.vertx.ext.shell.cli.CliToken.createText(text));
    return ret;
  }

  /**
   * Create a new blank token.
   * @param blank the blank value
   * @return the token
   */
  public static CliToken createBlank(String blank) { 
    CliToken ret= CliToken.newInstance(io.vertx.ext.shell.cli.CliToken.createBlank(blank));
    return ret;
  }

  /**
   * @return the token value
   * @return 
   */
  public String value() { 
    String ret = this.delegate.value();
    return ret;
  }

  /**
   * @return the raw token value, that may contain unescaped chars, for instance 
   * @return 
   */
  public String raw() { 
    String ret = this.delegate.raw();
    return ret;
  }

  /**
   * @return true when it's a text token
   * @return 
   */
  public boolean isText() { 
    boolean ret = this.delegate.isText();
    return ret;
  }

  /**
   * @return true when it's a blank token
   * @return 
   */
  public boolean isBlank() { 
    boolean ret = this.delegate.isBlank();
    return ret;
  }

  /**
   * Tokenize the string argument and return a list of tokens.
   * @param s the tokenized string
   * @return the tokens
   */
  public static List<CliToken> tokenize(String s) { 
    List<CliToken> ret = io.vertx.ext.shell.cli.CliToken.tokenize(s).stream().map(CliToken::newInstance).collect(java.util.stream.Collectors.toList());
    return ret;
  }


  public static CliToken newInstance(io.vertx.ext.shell.cli.CliToken arg) {
    return arg != null ? new CliToken(arg) : null;
  }
}
