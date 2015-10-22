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

package io.vertx.groovy.ext.shell.term;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.groovy.ext.shell.io.Stream
import io.vertx.groovy.ext.shell.io.Tty
import io.vertx.core.Handler
/**
 * The remote terminal.
*/
@CompileStatic
public class Term extends Tty {
  private final def io.vertx.ext.shell.term.Term delegate;
  public Term(Object delegate) {
    super((io.vertx.ext.shell.term.Term) delegate);
    this.delegate = (io.vertx.ext.shell.term.Term) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public Term resizehandler(Handler<Void> handler) {
    ( /* Work around for https://jira.codehaus.org/browse/GROOVY-6970 */ (io.vertx.ext.shell.io.Tty) this.delegate).resizehandler(handler);
    return this;
  }
  public Term setStdin(Stream stdin) {
    ( /* Work around for https://jira.codehaus.org/browse/GROOVY-6970 */ (io.vertx.ext.shell.io.Tty) this.delegate).setStdin((io.vertx.ext.shell.io.Stream)stdin.getDelegate());
    return this;
  }
  public Term closeHandler(Handler<Void> handler) {
    this.delegate.closeHandler(handler);
    return this;
  }
  public void close() {
    this.delegate.close();
  }
}
