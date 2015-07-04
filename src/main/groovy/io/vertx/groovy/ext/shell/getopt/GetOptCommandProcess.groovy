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

package io.vertx.groovy.ext.shell.getopt;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import java.util.List
import io.vertx.core.Handler
import io.vertx.groovy.ext.shell.Stream
import io.vertx.groovy.ext.shell.command.CommandProcess
/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
@CompileStatic
public class GetOptCommandProcess extends CommandProcess {
  final def io.vertx.ext.shell.getopt.GetOptCommandProcess delegate;
  public GetOptCommandProcess(io.vertx.ext.shell.getopt.GetOptCommandProcess delegate) {
    super(delegate);
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
  public GetOptCommandProcess setStdin(Stream stdin) {
    this.delegate.setStdin((io.vertx.ext.shell.Stream)stdin.getDelegate());
    return this;
  }
  public GetOptCommandProcess eventHandler(String event, Handler<Void> handler) {
    this.delegate.eventHandler(event, handler);
    return this;
  }
  public GetOptCommandProcess write(String text) {
    this.delegate.write(text);
    return this;
  }
}
