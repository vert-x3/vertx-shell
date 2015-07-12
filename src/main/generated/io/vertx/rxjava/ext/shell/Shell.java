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
import io.vertx.rxjava.ext.shell.cli.Completion;
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

  public void createJob(String s, Handler<AsyncResult<Job>> handler) { 
    this.delegate.createJob(s, new Handler<AsyncResult<io.vertx.ext.shell.Job>>() {
      public void handle(AsyncResult<io.vertx.ext.shell.Job> event) {
        AsyncResult<Job> f;
        if (event.succeeded()) {
          f = InternalHelper.<Job>result(new Job(event.result()));
        } else {
          f = InternalHelper.<Job>failure(event.cause());
        }
        handler.handle(f);
      }
    });
  }

  public Observable<Job> createJobObservable(String s) { 
    io.vertx.rx.java.ObservableFuture<Job> handler = io.vertx.rx.java.RxHelper.observableFuture();
    createJob(s, handler.toHandler());
    return handler;
  }

  public void complete(Completion completion) { 
    this.delegate.complete((io.vertx.ext.shell.cli.Completion) completion.getDelegate());
  }


  public static Shell newInstance(io.vertx.ext.shell.Shell arg) {
    return arg != null ? new Shell(arg) : null;
  }
}
