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
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.shell.session.Session
/**
 * The completion object
*/
@CompileStatic
public class Completion {
  private final def io.vertx.ext.shell.cli.Completion delegate;
  public Completion(Object delegate) {
    this.delegate = (io.vertx.ext.shell.cli.Completion) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * @return the current Vert.x instance
   */
  public Vertx vertx() {
    def ret = InternalHelper.safeCreate(delegate.vertx(), io.vertx.groovy.core.Vertx.class);
    return ret;
  }
  /**
   * @return the shell current session, useful for accessing data like the current path for file completion, etc...
   */
  public Session session() {
    def ret = InternalHelper.safeCreate(delegate.session(), io.vertx.groovy.ext.shell.session.Session.class);
    return ret;
  }
  /**
   * @return the current line being completed in raw format, i.e without any char escape performed
   */
  public String rawLine() {
    def ret = delegate.rawLine();
    return ret;
  }
  /**
   * @return the current line being completed as preparsed tokens
   */
  public List<CliToken> lineTokens() {
    def ret = (List)delegate.lineTokens()?.collect({InternalHelper.safeCreate(it, io.vertx.groovy.ext.shell.cli.CliToken.class)});
    return ret;
  }
  /**
   * End the completion with a list of candidates, these candidates will be displayed by the shell on the console.
   * @param candidates the candidates
   */
  public void complete(List<String> candidates) {
    delegate.complete(candidates != null ? (List)candidates.collect({it}) : null);
  }
  /**
   * End the completion with a value that will be inserted to complete the line.
   * @param value the value to complete with
   * @param terminal true if the value is terminal, i.e can be further completed
   */
  public void complete(String value, boolean terminal) {
    delegate.complete(value, terminal);
  }
}
