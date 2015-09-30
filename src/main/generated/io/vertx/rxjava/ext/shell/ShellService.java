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
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.shell.registry.CommandRegistry;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * The shell service, provides a remotely accessible shell available via Telnet or SSH according to the
 * {@link io.vertx.ext.shell.ShellServiceOptions} configuration.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.ShellService original} non RX-ified interface using Vert.x codegen.
 */

public class ShellService {

  final io.vertx.ext.shell.ShellService delegate;

  public ShellService(io.vertx.ext.shell.ShellService delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static ShellService create(Vertx vertx, ShellServiceOptions options) { 
    ShellService ret= ShellService.newInstance(io.vertx.ext.shell.ShellService.create((io.vertx.core.Vertx) vertx.getDelegate(), options));
    return ret;
  }

  /**
   * @return the command registry for this service
   * @return 
   */
  public CommandRegistry getCommandRegistry() { 
    CommandRegistry ret= CommandRegistry.newInstance(this.delegate.getCommandRegistry());
    return ret;
  }

  /**
   * Start the shell service, this is an asynchronous start.
   */
  public void start() { 
    this.delegate.start();
  }

  /**
   * Start the shell service, this is an asynchronous start.
   * @param startHandler handler for getting notified when service is started
   */
  public void start(Handler<AsyncResult<Void>> startHandler) { 
    this.delegate.start(startHandler);
  }

  /**
   * Start the shell service, this is an asynchronous start.
   * @return 
   */
  public Observable<Void> startObservable() { 
    io.vertx.rx.java.ObservableFuture<Void> startHandler = io.vertx.rx.java.RxHelper.observableFuture();
    start(startHandler.toHandler());
    return startHandler;
  }

  /**
   * Stop the shell service, this is an asynchronous stop.
   */
  public void stop() { 
    this.delegate.stop();
  }

  /**
   * Stop the shell service, this is an asynchronous start.
   * @param stopHandler handler for getting notified when service is stopped
   */
  public void stop(Handler<AsyncResult<Void>> stopHandler) { 
    this.delegate.stop(stopHandler);
  }

  /**
   * Stop the shell service, this is an asynchronous start.
   * @return 
   */
  public Observable<Void> stopObservable() { 
    io.vertx.rx.java.ObservableFuture<Void> stopHandler = io.vertx.rx.java.RxHelper.observableFuture();
    stop(stopHandler.toHandler());
    return stopHandler;
  }


  public static ShellService newInstance(io.vertx.ext.shell.ShellService arg) {
    return arg != null ? new ShellService(arg) : null;
  }
}
