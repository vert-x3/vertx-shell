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

package io.vertx.groovy.ext.shell;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.Handler
/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
@CompileStatic
public class Tty {
  final def io.vertx.ext.shell.Tty delegate;
  public Tty(io.vertx.ext.shell.Tty delegate) {
    this.delegate = delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public int width() {
    def ret = this.delegate.width();
    return ret;
  }
  public int height() {
    def ret = this.delegate.height();
    return ret;
  }
  public void setStdin(Stream stdin) {
    this.delegate.setStdin((io.vertx.ext.shell.Stream)stdin.getDelegate());
  }
  public Stream stdout() {
    def ret= InternalHelper.safeCreate(this.delegate.stdout(), io.vertx.ext.shell.Stream.class, io.vertx.groovy.ext.shell.Stream.class);
    return ret;
  }
  public void eventHandler(String event, Handler<Void> handler) {
    this.delegate.eventHandler(event, handler);
  }
}
