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

package io.vertx.groovy.ext.shell.command;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import java.util.List
import io.vertx.core.Handler
import io.vertx.groovy.ext.shell.Stream
/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
@CompileStatic
public class CommandProcess {
  final def io.vertx.ext.shell.command.CommandProcess delegate;
  public CommandProcess(io.vertx.ext.shell.command.CommandProcess delegate) {
    this.delegate = delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public List<String> arguments() {
    def ret = this.delegate.arguments();
    return ret;
  }
  public List<String> getOption(String name) {
    def ret = this.delegate.getOption(name);
    return ret;
  }
  public CommandProcess setStdin(Stream stdin) {
    this.delegate.setStdin((io.vertx.ext.shell.Stream)stdin.getDelegate());
    return this;
  }
  public CommandProcess signalHandler(String signal, Handler<Void> handler) {
    this.delegate.signalHandler(signal, handler);
    return this;
  }
  public Stream stdout() {
    def ret= InternalHelper.safeCreate(this.delegate.stdout(), io.vertx.ext.shell.Stream.class, io.vertx.groovy.ext.shell.Stream.class);
    return ret;
  }
  public CommandProcess write(String text) {
    this.delegate.write(text);
    return this;
  }
  public void end(int code) {
    this.delegate.end(code);
  }
}
