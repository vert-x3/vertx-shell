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
 * A Vert.x Shell command, it can be created from any language using the {@link io.vertx.groovy.ext.shell.command.CommandBuilder#command} or from a
 * Java class using {@link io.vertx.groovy.ext.shell.command.Command#create}
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
  /**
   * Process the command, when the command is done processing it should call the {@link io.vertx.groovy.ext.shell.command.CommandProcess#end} method.
   * @param process the command process
   */
  public void process(CommandProcess process) {
    this.delegate.process((io.vertx.ext.shell.command.CommandProcess)process.getDelegate());
  }
  /**
   * Perform command completion, when the command is done completing it should call 
   * or  )} method to signal completion is done.
   * @param completion the completion object
   */
  public void complete(Completion completion) {
    this.delegate.complete((io.vertx.ext.shell.cli.Completion)completion.getDelegate());
  }
}
