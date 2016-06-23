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
    CommandRegistry ret = CommandRegistry.newInstance(io.vertx.ext.shell.command.CommandRegistry.getShared((io.vertx.core.Vertx)vertx.getDelegate()));
    return ret;
  }

  /**
   * Create a new registry.
   * @param vertx the vertx instance
   * @return the created registry
   */
  public static CommandRegistry create(Vertx vertx) { 
    CommandRegistry ret = CommandRegistry.newInstance(io.vertx.ext.shell.command.CommandRegistry.create((io.vertx.core.Vertx)vertx.getDelegate()));
    return ret;
  }

  /**
   * Like {@link io.vertx.rxjava.ext.shell.command.CommandRegistry#registerCommand}, without a completion handler.
   * @param command 
   * @return 
   */
  public CommandRegistry registerCommand(Command command) { 
    delegate.registerCommand((io.vertx.ext.shell.command.Command)command.getDelegate());
    return this;
  }

  /**
   * Register a command
   * @param command the command to register
   * @param completionHandler notified when the command is registered
   * @return a reference to this, so the API can be used fluently
   */
  public CommandRegistry registerCommand(Command command, Handler<AsyncResult<Command>> completionHandler) { 
    delegate.registerCommand((io.vertx.ext.shell.command.Command)command.getDelegate(), new Handler<AsyncResult<io.vertx.ext.shell.command.Command>>() {
      public void handle(AsyncResult<io.vertx.ext.shell.command.Command> ar) {
        if (ar.succeeded()) {
          completionHandler.handle(io.vertx.core.Future.succeededFuture(Command.newInstance(ar.result())));
        } else {
          completionHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    });
    return this;
  }

  /**
   * Register a command
   * @param command the command to register
   * @return 
   */
  public Observable<Command> registerCommandObservable(Command command) { 
    io.vertx.rx.java.ObservableFuture<Command> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    registerCommand(command, completionHandler.toHandler());
    return completionHandler;
  }

  /**
   * Like {@link io.vertx.rxjava.ext.shell.command.CommandRegistry#registerCommands}, without a completion handler.
   * @param commands 
   * @return 
   */
  public CommandRegistry registerCommands(List<Command> commands) { 
    delegate.registerCommands(commands.stream().map(elt -> (io.vertx.ext.shell.command.Command)elt.getDelegate()).collect(java.util.stream.Collectors.toList()));
    return this;
  }

  /**
   * Register a list of commands.
   * @param commands the commands to register
   * @param completionHandler notified when the command is registered
   * @return a reference to this, so the API can be used fluently
   */
  public CommandRegistry registerCommands(List<Command> commands, Handler<AsyncResult<List<Command>>> completionHandler) { 
    delegate.registerCommands(commands.stream().map(elt -> (io.vertx.ext.shell.command.Command)elt.getDelegate()).collect(java.util.stream.Collectors.toList()), new Handler<AsyncResult<java.util.List<io.vertx.ext.shell.command.Command>>>() {
      public void handle(AsyncResult<java.util.List<io.vertx.ext.shell.command.Command>> ar) {
        if (ar.succeeded()) {
          completionHandler.handle(io.vertx.core.Future.succeededFuture(ar.result().stream().map(elt -> Command.newInstance(elt)).collect(java.util.stream.Collectors.toList())));
        } else {
          completionHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    });
    return this;
  }

  /**
   * Register a list of commands.
   * @param commands the commands to register
   * @return 
   */
  public Observable<List<Command>> registerCommandsObservable(List<Command> commands) { 
    io.vertx.rx.java.ObservableFuture<List<Command>> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    registerCommands(commands, completionHandler.toHandler());
    return completionHandler;
  }

  /**
   * Like {@link io.vertx.rxjava.ext.shell.command.CommandRegistry#unregisterCommand}, without a completion handler.
   * @param commandName 
   * @return 
   */
  public CommandRegistry unregisterCommand(String commandName) { 
    delegate.unregisterCommand(commandName);
    return this;
  }

  /**
   * Unregister a command.
   * @param commandName the command name
   * @param completionHandler notified when the command is unregistered
   * @return a reference to this, so the API can be used fluently
   */
  public CommandRegistry unregisterCommand(String commandName, Handler<AsyncResult<Void>> completionHandler) { 
    delegate.unregisterCommand(commandName, completionHandler);
    return this;
  }

  /**
   * Unregister a command.
   * @param commandName the command name
   * @return 
   */
  public Observable<Void> unregisterCommandObservable(String commandName) { 
    io.vertx.rx.java.ObservableFuture<Void> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    unregisterCommand(commandName, completionHandler.toHandler());
    return completionHandler;
  }


  public static CommandRegistry newInstance(io.vertx.ext.shell.command.CommandRegistry arg) {
    return arg != null ? new CommandRegistry(arg) : null;
  }
}
