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
import io.vertx.rxjava.core.Vertx;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.command.CommandManager original} non RX-ified interface using Vert.x codegen.
 */

public class CommandManager {

  final io.vertx.ext.shell.command.CommandManager delegate;

  public CommandManager(io.vertx.ext.shell.command.CommandManager delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static CommandManager create(Vertx vertx) { 
    CommandManager ret= CommandManager.newInstance(io.vertx.ext.shell.command.CommandManager.create((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
  }

  public void addCommand(Command command, Handler<AsyncResult<Void>> handler) { 
    this.delegate.addCommand((io.vertx.ext.shell.command.Command) command.getDelegate(), handler);
  }

  public Observable<Void> addCommandObservable(Command command) { 
    io.vertx.rx.java.ObservableFuture<Void> handler = io.vertx.rx.java.RxHelper.observableFuture();
    addCommand(command, handler.toHandler());
    return handler;
  }

  public void close() { 
    this.delegate.close();
  }


  public static CommandManager newInstance(io.vertx.ext.shell.command.CommandManager arg) {
    return arg != null ? new CommandManager(arg) : null;
  }
}
