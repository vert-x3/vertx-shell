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
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
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
  public static CliToken createText(String s) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.cli.CliToken.createText(s), io.vertx.groovy.ext.shell.cli.CliToken.class);
    return ret;
  }
  public static CliToken createBlank(String s) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.cli.CliToken.createBlank(s), io.vertx.groovy.ext.shell.cli.CliToken.class);
    return ret;
  }
  public String raw() {
    def ret = this.delegate.raw();
    return ret;
  }
  public String value() {
    def ret = this.delegate.value();
    return ret;
  }
  public boolean isText() {
    def ret = this.delegate.isText();
    return ret;
  }
  public boolean isBlank() {
    def ret = this.delegate.isBlank();
    return ret;
  }
  public static List<CliToken> tokenize(String s) {
    def ret = io.vertx.ext.shell.cli.CliToken.tokenize(s)?.collect({underpants -> new io.vertx.groovy.ext.shell.cli.CliToken(underpants)});
      return ret;
  }
}
