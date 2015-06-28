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
import io.vertx.core.Handler
/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
@CompileStatic
public class Command {
  final def io.vertx.ext.shell.command.Command delegate;
  public Command(io.vertx.ext.shell.command.Command delegate) {
    this.delegate = delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public static Command create(String name) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.command.Command.create(name), io.vertx.ext.shell.command.Command.class, io.vertx.groovy.ext.shell.command.Command.class);
    return ret;
  }
  public Command addOption(Option option) {
    this.delegate.addOption((io.vertx.ext.shell.command.Option)option.getDelegate());
    return this;
  }
  public Option getOption(String name) {
    def ret= InternalHelper.safeCreate(this.delegate.getOption(name), io.vertx.ext.shell.command.Option.class, io.vertx.groovy.ext.shell.command.Option.class);
    return ret;
  }
  public String name() {
    def ret = this.delegate.name();
    return ret;
  }
  public void setExecuteHandler(Handler<Execution> handler) {
    this.delegate.setExecuteHandler(new Handler<io.vertx.ext.shell.command.Execution>() {
      public void handle(io.vertx.ext.shell.command.Execution event) {
        handler.handle(new io.vertx.groovy.ext.shell.command.Execution(event));
      }
    });
  }
  public void unregister() {
    this.delegate.unregister();
  }
}
