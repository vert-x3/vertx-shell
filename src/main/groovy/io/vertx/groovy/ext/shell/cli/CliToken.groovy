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

package io.vertx.groovy.ext.shell.cli;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import java.util.List
/**
 * A parsed token in the command line interface.
*/
@CompileStatic
public class CliToken {
  private final def io.vertx.ext.shell.cli.CliToken delegate;
  public CliToken(Object delegate) {
    this.delegate = (io.vertx.ext.shell.cli.CliToken) delegate;
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
    def ret = InternalHelper.safeCreate(io.vertx.ext.shell.cli.CliToken.createText(text), io.vertx.groovy.ext.shell.cli.CliToken.class);
    return ret;
  }
  /**
   * Create a new blank token.
   * @param blank the blank value
   * @return the token
   */
  public static CliToken createBlank(String blank) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.shell.cli.CliToken.createBlank(blank), io.vertx.groovy.ext.shell.cli.CliToken.class);
    return ret;
  }
  /**
   * @return the token value
   * @return 
   */
  public String value() {
    def ret = delegate.value();
    return ret;
  }
  /**
   * @return the raw token value, that may contain unescaped chars, for instance 
   * @return 
   */
  public String raw() {
    def ret = delegate.raw();
    return ret;
  }
  /**
   * @return true when it's a text token
   * @return 
   */
  public boolean isText() {
    def ret = delegate.isText();
    return ret;
  }
  /**
   * @return true when it's a blank token
   * @return 
   */
  public boolean isBlank() {
    def ret = delegate.isBlank();
    return ret;
  }
  /**
   * Tokenize the string argument and return a list of tokens.
   * @param s the tokenized string
   * @return the tokens
   */
  public static List<CliToken> tokenize(String s) {
    def ret = (List)io.vertx.ext.shell.cli.CliToken.tokenize(s)?.collect({InternalHelper.safeCreate(it, io.vertx.groovy.ext.shell.cli.CliToken.class)});
    return ret;
  }
}
