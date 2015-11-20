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
import io.vertx.rxjava.ext.shell.system.Shell;
import io.vertx.rxjava.ext.shell.term.TermServer;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.shell.registry.CommandRegistry;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

/**
 * The shell server.
 *
 * A shell server is associated with a collection of : the {@link io.vertx.rxjava.ext.shell.ShellServer#registerTermServer}
 * method registers a term server. Term servers life cycle are managed by this server.<p/>
 *
 * When a  receives an incoming connection, a  instance is created and
 * associated with this connection.<p/>
 *
 * The {@link io.vertx.rxjava.ext.shell.ShellServer#createShell} method can be used to create  instance for testing purposes.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.ShellServer original} non RX-ified interface using Vert.x codegen.
 */

public class ShellServer {

  final io.vertx.ext.shell.ShellServer delegate;

  public ShellServer(io.vertx.ext.shell.ShellServer delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Create a new shell server.
   * @param vertx the vertx
   * @return the created shell server
   */
  public static ShellServer create(Vertx vertx) { 
    ShellServer ret= ShellServer.newInstance(io.vertx.ext.shell.ShellServer.create((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
  }

  /**
   * @return the command registry for this server
   * @return 
   */
  public CommandRegistry commandRegistry() { 
    CommandRegistry ret= CommandRegistry.newInstance(this.delegate.commandRegistry());
    return ret;
  }

  /**
   * Set the shell welcome message.
   * @param msg the welcome message
   * @return a reference to this, so the API can be used fluently
   */
  public ShellServer welcomeMessage(String msg) { 
    this.delegate.welcomeMessage(msg);
    return this;
  }

  /**
   * Register a term server to this shell server, the term server lifecycle methods are managed by this shell server.
   * @param termServer the term server to add
   * @return a reference to this, so the API can be used fluently
   */
  public ShellServer registerTermServer(TermServer termServer) { 
    this.delegate.registerTermServer((io.vertx.ext.shell.term.TermServer) termServer.getDelegate());
    return this;
  }

  /**
   * Create a new shell, the returned shell should be closed explicitely.
   * @return the created shell
   */
  public Shell createShell() { 
    Shell ret= Shell.newInstance(this.delegate.createShell());
    return ret;
  }

  /**
   * Start the shell service, this is an asynchronous start.
   */
  public void listen() { 
    this.delegate.listen();
  }

  /**
   * Start the shell service, this is an asynchronous start.
   * @param listenHandler handler for getting notified when service is started
   */
  public void listen(Handler<AsyncResult<Void>> listenHandler) { 
    this.delegate.listen(listenHandler);
  }

  /**
   * Start the shell service, this is an asynchronous start.
   * @return 
   */
  public Observable<Void> listenObservable() { 
    io.vertx.rx.java.ObservableFuture<Void> listenHandler = io.vertx.rx.java.RxHelper.observableFuture();
    listen(listenHandler.toHandler());
    return listenHandler;
  }

  /**
   * Close the shell server, this is an asynchronous close.
   */
  public void close() { 
    this.delegate.close();
  }

  /**
   * Close the shell server, this is an asynchronous close.
   * @param completionHandler handler for getting notified when service is stopped
   */
  public void close(Handler<AsyncResult<Void>> completionHandler) { 
    this.delegate.close(completionHandler);
  }

  /**
   * Close the shell server, this is an asynchronous close.
   * @return 
   */
  public Observable<Void> closeObservable() { 
    io.vertx.rx.java.ObservableFuture<Void> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    close(completionHandler.toHandler());
    return completionHandler;
  }


  public static ShellServer newInstance(io.vertx.ext.shell.ShellServer arg) {
    return arg != null ? new ShellServer(arg) : null;
  }
}
