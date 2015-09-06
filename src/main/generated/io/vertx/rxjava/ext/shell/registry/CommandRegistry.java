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
import io.vertx.core.Handler;
import io.vertx.rxjava.ext.shell.cli.CliToken;
import io.vertx.rxjava.ext.shell.process.Process;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
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

  public void createProcess(String s, Handler<AsyncResult<Process>> handler) { 
    this.delegate.createProcess(s, new Handler<AsyncResult<io.vertx.ext.shell.process.Process>>() {
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

  public Observable<Process> createProcessObservable(String s) { 
    io.vertx.rx.java.ObservableFuture<Process> handler = io.vertx.rx.java.RxHelper.observableFuture();
    createProcess(s, handler.toHandler());
    return handler;
  }

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

  public Observable<Process> createProcessObservable(List<CliToken> line) { 
    io.vertx.rx.java.ObservableFuture<Process> handler = io.vertx.rx.java.RxHelper.observableFuture();
    createProcess(line, handler.toHandler());
    return handler;
  }

  public void complete(Completion completion) { 
    this.delegate.complete((io.vertx.ext.shell.cli.Completion) completion.getDelegate());
  }

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

  public void release() { 
    this.delegate.release();
  }


  public static CommandRegistry newInstance(io.vertx.ext.shell.registry.CommandRegistry arg) {
    return arg != null ? new CommandRegistry(arg) : null;
  }
}
