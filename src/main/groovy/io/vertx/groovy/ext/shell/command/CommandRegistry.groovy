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
import java.util.List
import io.vertx.groovy.core.Vertx
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
/**
 * A registry that contains the commands known by a shell.<p/>
 *
 * It is a mutable command resolver.
*/
@CompileStatic
public class CommandRegistry extends CommandResolver {
  private final def io.vertx.ext.shell.command.CommandRegistry delegate;
  public CommandRegistry(Object delegate) {
    super((io.vertx.ext.shell.command.CommandRegistry) delegate);
    this.delegate = (io.vertx.ext.shell.command.CommandRegistry) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Get the shared registry for the Vert.x instance.
   * @param vertx the vertx instance
   * @return the shared registry
   */
  public static CommandRegistry getShared(Vertx vertx) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.command.CommandRegistry.getShared((io.vertx.core.Vertx)vertx.getDelegate()), io.vertx.groovy.ext.shell.command.CommandRegistry.class);
    return ret;
  }
  /**
   * Create a new registry.
   * @param vertx the vertx instance
   * @return the created registry
   */
  public static CommandRegistry create(Vertx vertx) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.command.CommandRegistry.create((io.vertx.core.Vertx)vertx.getDelegate()), io.vertx.groovy.ext.shell.command.CommandRegistry.class);
    return ret;
  }
  /**
   * Like {@link io.vertx.groovy.ext.shell.command.CommandRegistry#registerCommand}, without a completion handler.
   * @param command 
   * @return 
   */
  public CommandRegistry registerCommand(Command command) {
    this.delegate.registerCommand((io.vertx.ext.shell.command.Command)command.getDelegate());
    return this;
  }
  /**
   * Register a command
   * @param command the command to register
   * @param completionHandler notified when the command is registered
   * @return a reference to this, so the API can be used fluently
   */
  public CommandRegistry registerCommand(Command command, Handler<AsyncResult<Command>> completionHandler) {
    this.delegate.registerCommand((io.vertx.ext.shell.command.Command)command.getDelegate(), new Handler<AsyncResult<io.vertx.ext.shell.command.Command>>() {
      public void handle(AsyncResult<io.vertx.ext.shell.command.Command> event) {
        AsyncResult<Command> f
        if (event.succeeded()) {
          f = InternalHelper.<Command>result(new Command(event.result()))
        } else {
          f = InternalHelper.<Command>failure(event.cause())
        }
        completionHandler.handle(f)
      }
    });
    return this;
  }
  /**
   * Like {@link io.vertx.groovy.ext.shell.command.CommandRegistry#registerCommands}, without a completion handler.
   * @param commands 
   * @return 
   */
  public CommandRegistry registerCommands(List<Command> commands) {
    this.delegate.registerCommands((List<io.vertx.ext.shell.command.Command>)(commands.collect({underpants -> underpants.getDelegate()})));
    return this;
  }
  /**
   * Register a list of commands.
   * @param commands the commands to register
   * @param completionHandler notified when the command is registered
   * @return a reference to this, so the API can be used fluently
   */
  public CommandRegistry registerCommands(List<Command> commands, Handler<AsyncResult<List<Command>>> completionHandler) {
    this.delegate.registerCommands((List<io.vertx.ext.shell.command.Command>)(commands.collect({underpants -> underpants.getDelegate()})), new Handler<AsyncResult<List<io.vertx.ext.shell.command.Command>>>() {
      public void handle(AsyncResult<List<io.vertx.ext.shell.command.Command>> event) {
        AsyncResult<List<Command>> f
        if (event.succeeded()) {
          f = InternalHelper.<List<Command>>result(event.result().collect({
            io.vertx.ext.shell.command.Command element ->
            new io.vertx.groovy.ext.shell.command.Command(element)
          }) as List)
        } else {
          f = InternalHelper.<List<Command>>failure(event.cause())
        }
        completionHandler.handle(f)
      }
    });
    return this;
  }
  /**
   * Like {@link io.vertx.groovy.ext.shell.command.CommandRegistry#unregisterCommand}, without a completion handler.
   * @param commandName 
   * @return 
   */
  public CommandRegistry unregisterCommand(String commandName) {
    this.delegate.unregisterCommand(commandName);
    return this;
  }
  /**
   * Unregister a command.
   * @param commandName the command name
   * @param completionHandler notified when the command is unregistered
   * @return a reference to this, so the API can be used fluently
   */
  public CommandRegistry unregisterCommand(String commandName, Handler<AsyncResult<Void>> completionHandler) {
    this.delegate.unregisterCommand(commandName, completionHandler);
    return this;
  }
}
