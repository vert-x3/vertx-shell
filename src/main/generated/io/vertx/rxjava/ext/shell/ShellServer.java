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
import rx.Observable;
import io.vertx.ext.shell.ShellServerOptions;
import io.vertx.rxjava.ext.shell.term.TermServer;
import io.vertx.rxjava.core.Vertx;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.rxjava.ext.shell.command.CommandResolver;
import io.vertx.rxjava.ext.shell.term.Term;

/**
 * The shell server.<p/>
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
   * Create a new shell server with default options.
   * @param vertx the vertx
   * @param options the options
   * @return the created shell server
   */
  public static ShellServer create(Vertx vertx, ShellServerOptions options) { 
    ShellServer ret = ShellServer.newInstance(io.vertx.ext.shell.ShellServer.create((io.vertx.core.Vertx)vertx.getDelegate(), options));
    return ret;
  }

  /**
   * Create a new shell server with specific options.
   * @param vertx the vertx
   * @return the created shell server
   */
  public static ShellServer create(Vertx vertx) { 
    ShellServer ret = ShellServer.newInstance(io.vertx.ext.shell.ShellServer.create((io.vertx.core.Vertx)vertx.getDelegate()));
    return ret;
  }

  /**
   * Register a command resolver for this server.
   * @param resolver the resolver
   * @return a reference to this, so the API can be used fluently
   */
  public ShellServer registerCommandResolver(CommandResolver resolver) { 
    delegate.registerCommandResolver((io.vertx.ext.shell.command.CommandResolver)resolver.getDelegate());
    return this;
  }

  /**
   * Register a term server to this shell server, the term server lifecycle methods are managed by this shell server.
   * @param termServer the term server to add
   * @return a reference to this, so the API can be used fluently
   */
  public ShellServer registerTermServer(TermServer termServer) { 
    delegate.registerTermServer((io.vertx.ext.shell.term.TermServer)termServer.getDelegate());
    return this;
  }

  /**
   * Create a new shell, the returned shell should be closed explicitely.
   * @param term the shell associated terminal
   * @return the created shell
   */
  public Shell createShell(Term term) { 
    Shell ret = Shell.newInstance(delegate.createShell((io.vertx.ext.shell.term.Term)term.getDelegate()));
    return ret;
  }

  /**
   * Create a new shell, the returned shell should be closed explicitely.
   * @return the created shell
   */
  public Shell createShell() { 
    Shell ret = Shell.newInstance(delegate.createShell());
    return ret;
  }

  /**
   * Start the shell service, this is an asynchronous start.
   * @return 
   */
  public ShellServer listen() { 
    delegate.listen();
    return this;
  }

  /**
   * Start the shell service, this is an asynchronous start.
   * @param listenHandler handler for getting notified when service is started
   * @return 
   */
  public ShellServer listen(Handler<AsyncResult<Void>> listenHandler) { 
    delegate.listen(listenHandler);
    return this;
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
    delegate.close();
  }

  /**
   * Close the shell server, this is an asynchronous close.
   * @param completionHandler handler for getting notified when service is stopped
   */
  public void close(Handler<AsyncResult<Void>> completionHandler) { 
    delegate.close(completionHandler);
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
