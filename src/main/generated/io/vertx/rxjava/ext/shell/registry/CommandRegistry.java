/*
 * Copyright 2015 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 *
 *
 * Copyright (c) 2015 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 *
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
import io.vertx.core.Handler;
import io.vertx.rxjava.ext.shell.cli.CliToken;
import io.vertx.rxjava.ext.shell.process.Process;

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
   * @param handler the handler to be notified about process creation
   */
  public void createProcess(String line, Handler<AsyncResult<Process>> handler) { 
    this.delegate.createProcess(line, new Handler<AsyncResult<io.vertx.ext.shell.process.Process>>() {
      public void handle(AsyncResult<io.vertx.ext.shell.process.Process> event) {
        AsyncResult<Process> f;
        if (event.succeeded()) {
          f = InternalHelper.<Process>result(new Process(event.result()));
        } else {
          f = InternalHelper.<Process>failure(event.cause());
        }
        handler.handle(f);
      }
    });
  }

  /**
   * Parses a command line and try to create a process.
   * @param line the command line to parse
   * @return 
   */
  public Observable<Process> createProcessObservable(String line) { 
    io.vertx.rx.java.ObservableFuture<Process> handler = io.vertx.rx.java.RxHelper.observableFuture();
    createProcess(line, handler.toHandler());
    return handler;
  }

  /**
   * Try to create a process from the command line tokens.
   * @param line the command line tokens
   * @param handler the handler to be notified about process creation
   */
  public void createProcess(List<CliToken> line, Handler<AsyncResult<Process>> handler) { 
    this.delegate.createProcess(line.stream().map(element -> (io.vertx.ext.shell.cli.CliToken)element.getDelegate()).collect(java.util.stream.Collectors.toList()), new Handler<AsyncResult<io.vertx.ext.shell.process.Process>>() {
      public void handle(AsyncResult<io.vertx.ext.shell.process.Process> event) {
        AsyncResult<Process> f;
        if (event.succeeded()) {
          f = InternalHelper.<Process>result(new Process(event.result()));
        } else {
          f = InternalHelper.<Process>failure(event.cause());
        }
        handler.handle(f);
      }
    });
  }

  /**
   * Try to create a process from the command line tokens.
   * @param line the command line tokens
   * @return 
   */
  public Observable<Process> createProcessObservable(List<CliToken> line) { 
    io.vertx.rx.java.ObservableFuture<Process> handler = io.vertx.rx.java.RxHelper.observableFuture();
    createProcess(line, handler.toHandler());
    return handler;
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
   */
  public void registerCommand(Command command) { 
    this.delegate.registerCommand((io.vertx.ext.shell.command.Command) command.getDelegate());
  }

  public void registerCommand(Command command, Handler<AsyncResult<CommandRegistration>> doneHandler) { 
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
  }

  public Observable<CommandRegistration> registerCommandObservable(Command command) { 
    io.vertx.rx.java.ObservableFuture<CommandRegistration> doneHandler = io.vertx.rx.java.RxHelper.observableFuture();
    registerCommand(command, doneHandler.toHandler());
    return doneHandler;
  }

  /**
   * Unregister a command.
   * @param commandName the command name
   */
  public void unregisterCommand(String commandName) { 
    this.delegate.unregisterCommand(commandName);
  }

  public void unregisterCommand(String commandName, Handler<AsyncResult<Void>> doneHandler) { 
    this.delegate.unregisterCommand(commandName, doneHandler);
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
