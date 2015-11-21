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
import java.util.List
import io.vertx.groovy.ext.shell.command.Command
import io.vertx.groovy.ext.shell.cli.Completion
import io.vertx.groovy.core.Vertx
import io.vertx.core.AsyncResult
import io.vertx.groovy.ext.shell.cli.CliToken
import io.vertx.core.Handler
import io.vertx.groovy.ext.shell.system.Process
/**
 * A registry that contains the commands known by a shell.
*/
@CompileStatic
public class CommandRegistry {
  private final def io.vertx.ext.shell.registry.CommandRegistry delegate;
  public CommandRegistry(Object delegate) {
    this.delegate = (io.vertx.ext.shell.registry.CommandRegistry) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Get the registry for the Vert.x instance
   * @param vertx the vertx instance
   * @return the registry
   */
  public static CommandRegistry get(Vertx vertx) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.registry.CommandRegistry.get((io.vertx.core.Vertx)vertx.getDelegate()), io.vertx.groovy.ext.shell.registry.CommandRegistry.class);
    return ret;
  }
  /**
   * @return the current command registrations
   * @return 
   */
  public List<CommandRegistration> registrations() {
    def ret = this.delegate.registrations()?.collect({underpants -> new io.vertx.groovy.ext.shell.registry.CommandRegistration(underpants)});
      return ret;
  }
  /**
   * Parses a command line and try to create a process.
   * @param line the command line to parse
   * @return the created process
   */
  public Process createProcess(String line) {
    def ret= InternalHelper.safeCreate(this.delegate.createProcess(line), io.vertx.groovy.ext.shell.system.Process.class);
    return ret;
  }
  /**
   * Try to create a process from the command line tokens.
   * @param line the command line tokens
   * @return the created process
   */
  public Process createProcess(List<CliToken> line) {
    def ret= InternalHelper.safeCreate(this.delegate.createProcess((List<io.vertx.ext.shell.cli.CliToken>)(line.collect({underpants -> underpants.getDelegate()}))), io.vertx.groovy.ext.shell.system.Process.class);
    return ret;
  }
  /**
   * Perform completion, the completion argument will be notified of the completion progress.
   * @param completion the completion object
   */
  public void complete(Completion completion) {
    this.delegate.complete((io.vertx.ext.shell.cli.Completion)completion.getDelegate());
  }
  /**
   * Register a command
   * @param command the command to register
   * @return a reference to this, so the API can be used fluently
   */
  public CommandRegistry registerCommand(Command command) {
    this.delegate.registerCommand((io.vertx.ext.shell.command.Command)command.getDelegate());
    return this;
  }
  public CommandRegistry registerCommand(Command command, Handler<AsyncResult<CommandRegistration>> doneHandler) {
    this.delegate.registerCommand((io.vertx.ext.shell.command.Command)command.getDelegate(), new Handler<AsyncResult<io.vertx.ext.shell.registry.CommandRegistration>>() {
      public void handle(AsyncResult<io.vertx.ext.shell.registry.CommandRegistration> event) {
        AsyncResult<CommandRegistration> f
        if (event.succeeded()) {
          f = InternalHelper.<CommandRegistration>result(new CommandRegistration(event.result()))
        } else {
          f = InternalHelper.<CommandRegistration>failure(event.cause())
        }
        doneHandler.handle(f)
      }
    });
    return this;
  }
  /**
   * Register a list of commands.
   * @param commands the commands to register
   * @return a reference to this, so the API can be used fluently
   */
  public CommandRegistry registerCommands(List<Command> commands) {
    this.delegate.registerCommands((List<io.vertx.ext.shell.command.Command>)(commands.collect({underpants -> underpants.getDelegate()})));
    return this;
  }
  public CommandRegistry registerCommands(List<Command> commands, Handler<AsyncResult<CommandRegistration>> doneHandler) {
    this.delegate.registerCommands((List<io.vertx.ext.shell.command.Command>)(commands.collect({underpants -> underpants.getDelegate()})), new Handler<AsyncResult<io.vertx.ext.shell.registry.CommandRegistration>>() {
      public void handle(AsyncResult<io.vertx.ext.shell.registry.CommandRegistration> event) {
        AsyncResult<CommandRegistration> f
        if (event.succeeded()) {
          f = InternalHelper.<CommandRegistration>result(new CommandRegistration(event.result()))
        } else {
          f = InternalHelper.<CommandRegistration>failure(event.cause())
        }
        doneHandler.handle(f)
      }
    });
    return this;
  }
  /**
   * Unregister a command.
   * @param commandName the command name
   * @return a reference to this, so the API can be used fluently
   */
  public CommandRegistry unregisterCommand(String commandName) {
    this.delegate.unregisterCommand(commandName);
    return this;
  }
  public CommandRegistry unregisterCommand(String commandName, Handler<AsyncResult<Void>> doneHandler) {
    this.delegate.unregisterCommand(commandName, doneHandler);
    return this;
  }
}
