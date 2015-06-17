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

package io.vertx.rxjava.ext.shell;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.ext.shell.command.CommandManager;
import io.vertx.rxjava.core.Vertx;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.Shell original} non RX-ified interface using Vert.x codegen.
 */

public class Shell {

  final io.vertx.ext.shell.Shell delegate;

  public Shell(io.vertx.ext.shell.Shell delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static Shell create(Vertx vertx, CommandManager manager) { 
    Shell ret= Shell.newInstance(io.vertx.ext.shell.Shell.create((io.vertx.core.Vertx) vertx.getDelegate(), (io.vertx.ext.shell.command.CommandManager) manager.getDelegate()));
    return ret;
  }

  public void createProcess(String name, Handler<AsyncResult<Process>> handler) { 
    this.delegate.createProcess(name, new Handler<AsyncResult<io.vertx.ext.shell.Process>>() {
      public void handle(AsyncResult<io.vertx.ext.shell.Process> event) {
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

  public Observable<Process> createProcessObservable(String name) { 
    io.vertx.rx.java.ObservableFuture<Process> handler = io.vertx.rx.java.RxHelper.observableFuture();
    createProcess(name, handler.toHandler());
    return handler;
  }


  public static Shell newInstance(io.vertx.ext.shell.Shell arg) {
    return arg != null ? new Shell(arg) : null;
  }
}
