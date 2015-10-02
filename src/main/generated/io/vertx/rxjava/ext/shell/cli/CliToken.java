/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
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
