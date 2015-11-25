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
 * A command pack is a set of commands, for instance the base command pack, the metrics command pack, etc...
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.command.CommandPack original} non RX-ified interface using Vert.x codegen.
 */

public class CommandPack {

  final io.vertx.ext.shell.command.CommandPack delegate;

  public CommandPack(io.vertx.ext.shell.command.CommandPack delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Lookup commands.
   * @param vertx the vertx instance
   * @param commandsHandler the handler that will receive the lookup callback
   */
  public void lookupCommands(Vertx vertx, Handler<AsyncResult<List<Command>>> commandsHandler) { 
    this.delegate.lookupCommands((io.vertx.core.Vertx) vertx.getDelegate(), new Handler<AsyncResult<List<io.vertx.ext.shell.command.Command>>>() {
      public void handle(AsyncResult<List<io.vertx.ext.shell.command.Command>> event) {
        AsyncResult<List<Command>> f;
        if (event.succeeded()) {
          f = InternalHelper.<List<Command>>result(event.result().stream().map(Command::newInstance).collect(java.util.stream.Collectors.toList()));
        } else {
          f = InternalHelper.<List<Command>>failure(event.cause());
        }
        commandsHandler.handle(f);
      }
    });
  }

  /**
   * Lookup commands.
   * @param vertx the vertx instance
   * @return 
   */
  public Observable<List<Command>> lookupCommandsObservable(Vertx vertx) { 
    io.vertx.rx.java.ObservableFuture<List<Command>> commandsHandler = io.vertx.rx.java.RxHelper.observableFuture();
    lookupCommands(vertx, commandsHandler.toHandler());
    return commandsHandler;
  }


  public static CommandPack newInstance(io.vertx.ext.shell.command.CommandPack arg) {
    return arg != null ? new CommandPack(arg) : null;
  }
}
