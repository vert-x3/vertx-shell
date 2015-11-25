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

package io.vertx.rxjava.ext.shell.registry;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import java.util.List;
import io.vertx.rxjava.ext.shell.command.Command;
import io.vertx.rxjava.ext.shell.cli.Completion;
import io.vertx.rxjava.core.Vertx;
import io.vertx.core.AsyncResult;
import io.vertx.rxjava.ext.shell.cli.CliToken;
import io.vertx.core.Handler;
import io.vertx.rxjava.ext.shell.system.Process;

/**
 * A registry that contains the commands known by a shell.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.registry.CommandRegistry original} non RX-ified interface using Vert.x codegen.
 */

public class CommandRegistry {

  final io.vertx.ext.shell.registry.CommandRegistry delegate;

  public CommandRegistry(io.vertx.ext.shell.registry.CommandRegistry delegate) {
    this.delegate = delegate;
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
    CommandRegistry ret= CommandRegistry.newInstance(io.vertx.ext.shell.registry.CommandRegistry.get((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
  }

  /**
   * @return the current command registrations
   * @return 
   */
  public List<CommandRegistration> registrations() { 
    List<CommandRegistration> ret = this.delegate.registrations().stream().map(CommandRegistration::newInstance).collect(java.util.stream.Collectors.toList());
    return ret;
  }

  /**
   * Parses a command line and try to create a process.
   * @param line the command line to parse
   * @return the created process
   */
  public Process createProcess(String line) { 
    Process ret= Process.newInstance(this.delegate.createProcess(line));
    return ret;
  }

  /**
   * Try to create a process from the command line tokens.
   * @param line the command line tokens
   * @return the created process
   */
  public Process createProcess(List<CliToken> line) { 
    Process ret= Process.newInstance(this.delegate.createProcess(line.stream().map(element -> (io.vertx.ext.shell.cli.CliToken)element.getDelegate()).collect(java.util.stream.Collectors.toList())));
    return ret;
  }

  /**
   * Perform completion, the completion argument will be notified of the completion progress.
   * @param completion the completion object
   */
  public void complete(Completion completion) { 
    this.delegate.complete((io.vertx.ext.shell.cli.Completion) completion.getDelegate());
  }

  /**
   * Register a command
   * @param command the command to register
   * @return a reference to this, so the API can be used fluently
   */
  public CommandRegistry registerCommand(Command command) { 
    this.delegate.registerCommand((io.vertx.ext.shell.command.Command) command.getDelegate());
    return this;
  }

  public CommandRegistry registerCommand(Command command, Handler<AsyncResult<CommandRegistration>> doneHandler) { 
    this.delegate.registerCommand((io.vertx.ext.shell.command.Command) command.getDelegate(), new Handler<AsyncResult<io.vertx.ext.shell.registry.CommandRegistration>>() {
      public void handle(AsyncResult<io.vertx.ext.shell.registry.CommandRegistration> event) {
        AsyncResult<CommandRegistration> f;
        if (event.succeeded()) {
          f = InternalHelper.<CommandRegistration>result(new CommandRegistration(event.result()));
        } else {
          f = InternalHelper.<CommandRegistration>failure(event.cause());
        }
        doneHandler.handle(f);
      }
    });
    return this;
  }

  public Observable<CommandRegistration> registerCommandObservable(Command command) { 
    io.vertx.rx.java.ObservableFuture<CommandRegistration> doneHandler = io.vertx.rx.java.RxHelper.observableFuture();
    registerCommand(command, doneHandler.toHandler());
    return doneHandler;
  }

  /**
   * Register a list of commands.
   * @param commands the commands to register
   * @return a reference to this, so the API can be used fluently
   */
  public CommandRegistry registerCommands(List<Command> commands) { 
    this.delegate.registerCommands(commands.stream().map(element -> (io.vertx.ext.shell.command.Command)element.getDelegate()).collect(java.util.stream.Collectors.toList()));
    return this;
  }

  public CommandRegistry registerCommands(List<Command> commands, Handler<AsyncResult<CommandRegistration>> doneHandler) { 
    this.delegate.registerCommands(commands.stream().map(element -> (io.vertx.ext.shell.command.Command)element.getDelegate()).collect(java.util.stream.Collectors.toList()), new Handler<AsyncResult<io.vertx.ext.shell.registry.CommandRegistration>>() {
      public void handle(AsyncResult<io.vertx.ext.shell.registry.CommandRegistration> event) {
        AsyncResult<CommandRegistration> f;
        if (event.succeeded()) {
          f = InternalHelper.<CommandRegistration>result(new CommandRegistration(event.result()));
        } else {
          f = InternalHelper.<CommandRegistration>failure(event.cause());
        }
        doneHandler.handle(f);
      }
    });
    return this;
  }

  public Observable<CommandRegistration> registerCommandsObservable(List<Command> commands) { 
    io.vertx.rx.java.ObservableFuture<CommandRegistration> doneHandler = io.vertx.rx.java.RxHelper.observableFuture();
    registerCommands(commands, doneHandler.toHandler());
    return doneHandler;
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

  public Observable<Void> unregisterCommandObservable(String commandName) { 
    io.vertx.rx.java.ObservableFuture<Void> doneHandler = io.vertx.rx.java.RxHelper.observableFuture();
    unregisterCommand(commandName, doneHandler.toHandler());
    return doneHandler;
  }


  public static CommandRegistry newInstance(io.vertx.ext.shell.registry.CommandRegistry arg) {
    return arg != null ? new CommandRegistry(arg) : null;
  }
}
