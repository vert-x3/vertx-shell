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
import io.vertx.core.json.JsonObject
import io.vertx.groovy.ext.shell.cli.Completion
import io.vertx.core.Handler
/**
 * A shell command.
*/
@CompileStatic
public class CommandBuilder {
  private final def io.vertx.ext.shell.command.CommandBuilder delegate;
  public CommandBuilder(Object delegate) {
    this.delegate = (io.vertx.ext.shell.command.CommandBuilder) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Set a command process handler on the command, the process handler is called when the command is executed.
   * @param handler the process handler
   * @return this command object
   */
  public CommandBuilder processHandler(Handler<CommandProcess> handler) {
    this.delegate.processHandler(new Handler<io.vertx.ext.shell.command.CommandProcess>() {
      public void handle(io.vertx.ext.shell.command.CommandProcess event) {
        handler.handle(new io.vertx.groovy.ext.shell.command.CommandProcess(event));
      }
    });
    return this;
  }
  /**
   * Set the command completion handler, the completion handler when the user asks for contextual command line
   * completion, usually hitting the <i>tab</i> key.
   * @param handler the completion handler
   * @return this command object
   */
  public CommandBuilder completionHandler(Handler<Completion> handler) {
    this.delegate.completionHandler(new Handler<io.vertx.ext.shell.cli.Completion>() {
      public void handle(io.vertx.ext.shell.cli.Completion event) {
        handler.handle(new io.vertx.groovy.ext.shell.cli.Completion(event));
      }
    });
    return this;
  }
  public Command build() {
    def ret= InternalHelper.safeCreate(this.delegate.build(), io.vertx.groovy.ext.shell.command.Command.class);
    return ret;
  }
}
