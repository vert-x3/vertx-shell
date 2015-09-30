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
import io.vertx.groovy.core.cli.CLI
import io.vertx.core.Handler
/**
 * A shell command.
*/
@CompileStatic
public class Command {
  private final def io.vertx.ext.shell.command.Command delegate;
  public Command(Object delegate) {
    this.delegate = (io.vertx.ext.shell.command.Command) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Create a new commmand, the command is responsible for managing the options and arguments via the
   * {@link io.vertx.groovy.ext.shell.command.CommandProcess #args() arguments}.
   * @param name the command name
   * @return the command
   */
  public static Command command(String name) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.command.Command.command(name), io.vertx.groovy.ext.shell.command.Command.class);
    return ret;
  }
  /**
   * Create a new commmand with its {@link io.vertx.groovy.core.cli.CLI} descriptor. This command can then retrieve the parsed
   * {@link io.vertx.groovy.ext.shell.command.CommandProcess#commandLine} when it executes to know get the command arguments and options.
   * @param cli the cli to use
   * @return the command
   */
  public static Command command(CLI cli) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.command.Command.command((io.vertx.core.cli.CLI)cli.getDelegate()), io.vertx.groovy.ext.shell.command.Command.class);
    return ret;
  }
  /**
   * @return the command name
   * @return 
   */
  public String name() {
    def ret = this.delegate.name();
    return ret;
  }
  /**
   * Set a command process handler on the command, the process handler is called when the command is executed.
   * @param handler the process handler
   * @return this command object
   */
  public Command processHandler(Handler<CommandProcess> handler) {
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
  public Command completionHandler(Handler<Completion> handler) {
    this.delegate.completionHandler(new Handler<io.vertx.ext.shell.cli.Completion>() {
      public void handle(io.vertx.ext.shell.cli.Completion event) {
        handler.handle(new io.vertx.groovy.ext.shell.cli.Completion(event));
      }
    });
    return this;
  }
}
