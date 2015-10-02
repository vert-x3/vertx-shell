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
/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
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
  public static CommandBuilder builder(String name) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.command.Command.builder(name), io.vertx.groovy.ext.shell.command.CommandBuilder.class);
    return ret;
  }
  /**
   * Create a new commmand with its {@link io.vertx.groovy.core.cli.CLI} descriptor. This command can then retrieve the parsed
   * {@link io.vertx.groovy.ext.shell.command.CommandProcess#commandLine} when it executes to know get the command arguments and options.
   * @param cli the cli to use
   * @return the command
   */
  public static CommandBuilder builder(CLI cli) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.command.Command.builder((io.vertx.core.cli.CLI)cli.getDelegate()), io.vertx.groovy.ext.shell.command.CommandBuilder.class);
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
   * @return the command line interface, can be null
   * @return 
   */
  public CLI cli() {
    def ret= InternalHelper.safeCreate(this.delegate.cli(), io.vertx.groovy.core.cli.CLI.class);
    return ret;
  }
  public void process(CommandProcess process) {
    this.delegate.process((io.vertx.ext.shell.command.CommandProcess)process.getDelegate());
  }
  public void complete(Completion completion) {
    this.delegate.complete((io.vertx.ext.shell.cli.Completion)completion.getDelegate());
  }
}
