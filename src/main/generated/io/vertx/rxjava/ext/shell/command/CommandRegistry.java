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

package io.vertx.rxjava.ext.shell.command;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import java.util.List;
import io.vertx.rxjava.core.Vertx;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * A registry that contains the commands known by a shell.<p/>
 *
 * It is a mutable command resolver.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.command.CommandRegistry original} non RX-ified interface using Vert.x codegen.
 */

public class CommandRegistry extends CommandResolver {

  final io.vertx.ext.shell.command.CommandRegistry delegate;

  public CommandRegistry(io.vertx.ext.shell.command.CommandRegistry delegate) {
    super(delegate);
    this.delegate = delegate;
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
    CommandRegistry ret= CommandRegistry.newInstance(io.vertx.ext.shell.command.CommandRegistry.getShared((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
  }

  /**
   * Create a new registry.
   * @param vertx the vertx instance
   * @return the created registry
   */
  public static CommandRegistry create(Vertx vertx) { 
    CommandRegistry ret= CommandRegistry.newInstance(io.vertx.ext.shell.command.CommandRegistry.create((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
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

  public CommandRegistry registerCommand(Command command, Handler<AsyncResult<Command>> completionHandler) { 
    this.delegate.registerCommand((io.vertx.ext.shell.command.Command) command.getDelegate(), new Handler<AsyncResult<io.vertx.ext.shell.command.Command>>() {
      public void handle(AsyncResult<io.vertx.ext.shell.command.Command> event) {
        AsyncResult<Command> f;
        if (event.succeeded()) {
          f = InternalHelper.<Command>result(new Command(event.result()));
        } else {
          f = InternalHelper.<Command>failure(event.cause());
        }
        completionHandler.handle(f);
      }
    });
    return this;
  }

  public Observable<Command> registerCommandObservable(Command command) { 
    io.vertx.rx.java.ObservableFuture<Command> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    registerCommand(command, completionHandler.toHandler());
    return completionHandler;
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

  public CommandRegistry registerCommands(List<Command> commands, Handler<AsyncResult<List<Command>>> completionHandler) { 
    this.delegate.registerCommands(commands.stream().map(element -> (io.vertx.ext.shell.command.Command)element.getDelegate()).collect(java.util.stream.Collectors.toList()), new Handler<AsyncResult<List<io.vertx.ext.shell.command.Command>>>() {
      public void handle(AsyncResult<List<io.vertx.ext.shell.command.Command>> event) {
        AsyncResult<List<Command>> f;
        if (event.succeeded()) {
          f = InternalHelper.<List<Command>>result(event.result().stream().map(Command::newInstance).collect(java.util.stream.Collectors.toList()));
        } else {
          f = InternalHelper.<List<Command>>failure(event.cause());
        }
        completionHandler.handle(f);
      }
    });
    return this;
  }

  public Observable<List<Command>> registerCommandsObservable(List<Command> commands) { 
    io.vertx.rx.java.ObservableFuture<List<Command>> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    registerCommands(commands, completionHandler.toHandler());
    return completionHandler;
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

  public CommandRegistry unregisterCommand(String commandName, Handler<AsyncResult<Void>> completionHandler) { 
    this.delegate.unregisterCommand(commandName, completionHandler);
    return this;
  }

  public Observable<Void> unregisterCommandObservable(String commandName) { 
    io.vertx.rx.java.ObservableFuture<Void> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    unregisterCommand(commandName, completionHandler.toHandler());
    return completionHandler;
  }


  public static CommandRegistry newInstance(io.vertx.ext.shell.command.CommandRegistry arg) {
    return arg != null ? new CommandRegistry(arg) : null;
  }
}
