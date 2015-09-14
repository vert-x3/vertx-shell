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
import io.vertx.core.json.JsonObject
import io.vertx.groovy.ext.shell.command.Command
import io.vertx.core.Handler
/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
@CompileStatic
public class GetOptCommand {
  private final def io.vertx.ext.shell.getopt.GetOptCommand delegate;
  public GetOptCommand(Object delegate) {
    this.delegate = (io.vertx.ext.shell.getopt.GetOptCommand) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public static GetOptCommand create(String name) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.getopt.GetOptCommand.create(name), io.vertx.groovy.ext.shell.getopt.GetOptCommand.class);
    return ret;
  }
  public void processHandler(Handler<GetOptCommandProcess> handler) {
    this.delegate.processHandler(new Handler<io.vertx.ext.shell.getopt.GetOptCommandProcess>() {
      public void handle(io.vertx.ext.shell.getopt.GetOptCommandProcess event) {
        handler.handle(new io.vertx.groovy.ext.shell.getopt.GetOptCommandProcess(event));
      }
    });
  }
  public GetOptCommand addOption(Option option) {
    this.delegate.addOption((io.vertx.ext.shell.getopt.Option)option.getDelegate());
    return this;
  }
  public Option getOption(String name) {
    def ret= InternalHelper.safeCreate(this.delegate.getOption(name), io.vertx.groovy.ext.shell.getopt.Option.class);
    return ret;
  }
  public Command build() {
    def ret= InternalHelper.safeCreate(this.delegate.build(), io.vertx.groovy.ext.shell.command.Command.class);
    return ret;
  }
}
