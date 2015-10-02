/*
 * Copyright 2014 Red Hat, Inc.
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
 * Copyright (c) 2011-2013 The original author or authors
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

package io.vertx.ext.shell.cli;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.shell.cli.impl.CliTokenImpl;

import java.util.List;

/**
 * A parsed token in the command line interface.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CliToken {

  /**
   * Create a text token.
   *
   * @param text the text
   * @return the token
   */
  static CliToken createText(String text) {
    return new CliTokenImpl(true, text, text);
  }

  /**
   * Create a new blank token.
   *
   * @param blank the blank value
   * @return the token
   */
  static CliToken createBlank(String blank) {
    return new CliTokenImpl(false, blank, blank);
  }

  /**
   * @return the token value
   */
  String value();

  /**
   * @return the raw token value, that may contain unescaped chars, for instance {@literal "ab\"cd"}
   */
  String raw();

  /**
   * @return true when it's a text token
   */
  boolean isText();

  /**
   * @return true when it's a blank token
   */
  boolean isBlank();

  /**
   * Tokenize the string argument and return a list of tokens.
   *
   * @param s the tokenized string
   * @return the tokens
   */
  static List<CliToken> tokenize(String s) {
    return CliTokenImpl.tokenize(s);
  }
}
