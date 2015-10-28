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

package io.vertx.groovy.ext.shell.registry;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.groovy.ext.shell.command.Command
import java.util.List
import io.vertx.groovy.ext.shell.cli.Completion
import io.vertx.core.AsyncResult
import io.vertx.groovy.ext.shell.cli.CliToken
import io.vertx.core.Handler
import io.vertx.groovy.ext.shell.system.Process
/**
 * A registration of a command in the {@link io.vertx.groovy.ext.shell.registry.CommandRegistry}
*/
@CompileStatic
public class CommandRegistration {
  private final def io.vertx.ext.shell.registry.CommandRegistration delegate;
  public CommandRegistration(Object delegate) {
    this.delegate = (io.vertx.ext.shell.registry.CommandRegistration) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * @return the registered command.
   * @return 
   */
  public Command command() {
    def ret= InternalHelper.safeCreate(this.delegate.command(), io.vertx.groovy.ext.shell.command.Command.class);
    return ret;
  }
  /**
   * Complete the command for the given completion.
   * @param completion the completion
   */
  public void complete(Completion completion) {
    this.delegate.complete((io.vertx.ext.shell.cli.Completion)completion.getDelegate());
  }
  /**
   * Create a new process with the passed arguments.
   * @param args the process arguments
   * @return the process
   */
  public Process createProcess(List<CliToken> args) {
    def ret= InternalHelper.safeCreate(this.delegate.createProcess((List<io.vertx.ext.shell.cli.CliToken>)(args.collect({underpants -> underpants.getDelegate()}))), io.vertx.groovy.ext.shell.system.Process.class);
    return ret;
  }
  /**
   * Unregister the current command
   */
  public void unregister() {
    this.delegate.unregister();
  }
  /**
   * Unregister the current command
   * @param handler 
   */
  public void unregister(Handler<AsyncResult<Void>> handler) {
    this.delegate.unregister(handler);
  }
}
