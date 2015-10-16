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

package io.vertx.groovy.ext.shell.net;
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
public class Terminal extends Tty {
  private final def io.vertx.ext.shell.net.Terminal delegate;
  public Terminal(Object delegate) {
    super((io.vertx.ext.shell.net.Terminal) delegate);
    this.delegate = (io.vertx.ext.shell.net.Terminal) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public Terminal setStdin(Stream stdin) {
    ( /* Work around for https://jira.codehaus.org/browse/GROOVY-6970 */ (io.vertx.ext.shell.io.Tty) this.delegate).setStdin((io.vertx.ext.shell.io.Stream)stdin.getDelegate());
    return this;
  }
  public Terminal setStdin(Handler<String> stdin) {
    ( /* Work around for https://jira.codehaus.org/browse/GROOVY-6970 */ (io.vertx.ext.shell.io.Tty) this.delegate).setStdin(stdin);
    return this;
  }
}
