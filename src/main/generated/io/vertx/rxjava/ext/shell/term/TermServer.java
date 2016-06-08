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

package io.vertx.rxjava.ext.shell.term;

import java.util.Map;
import rx.Observable;
import io.vertx.ext.shell.term.TelnetTermOptions;
import io.vertx.ext.shell.term.HttpTermOptions;
import io.vertx.ext.shell.term.SSHTermOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.rxjava.ext.auth.AuthProvider;

/**
 * A server for terminal based applications.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.term.TermServer original} non RX-ified interface using Vert.x codegen.
 */

public class TermServer {

  final io.vertx.ext.shell.term.TermServer delegate;

  public TermServer(io.vertx.ext.shell.term.TermServer delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Create a term server for the SSH protocol.
   * @param vertx the vertx instance
   * @return the term server
   */
  public static TermServer createSSHTermServer(Vertx vertx) { 
    TermServer ret = TermServer.newInstance(io.vertx.ext.shell.term.TermServer.createSSHTermServer((io.vertx.core.Vertx)vertx.getDelegate()));
    return ret;
  }

  /**
   * Create a term server for the SSH protocol.
   * @param vertx the vertx instance
   * @param options the ssh options
   * @return the term server
   */
  public static TermServer createSSHTermServer(Vertx vertx, SSHTermOptions options) { 
    TermServer ret = TermServer.newInstance(io.vertx.ext.shell.term.TermServer.createSSHTermServer((io.vertx.core.Vertx)vertx.getDelegate(), options));
    return ret;
  }

  /**
   * Create a term server for the Telnet protocol.
   * @param vertx the vertx instance
   * @return the term server
   */
  public static TermServer createTelnetTermServer(Vertx vertx) { 
    TermServer ret = TermServer.newInstance(io.vertx.ext.shell.term.TermServer.createTelnetTermServer((io.vertx.core.Vertx)vertx.getDelegate()));
    return ret;
  }

  /**
   * Create a term server for the Telnet protocol.
   * @param vertx the vertx instance
   * @param options the term options
   * @return the term server
   */
  public static TermServer createTelnetTermServer(Vertx vertx, TelnetTermOptions options) { 
    TermServer ret = TermServer.newInstance(io.vertx.ext.shell.term.TermServer.createTelnetTermServer((io.vertx.core.Vertx)vertx.getDelegate(), options));
    return ret;
  }

  /**
   * Create a term server for the HTTP protocol.
   * @param vertx the vertx instance
   * @return the term server
   */
  public static TermServer createHttpTermServer(Vertx vertx) { 
    TermServer ret = TermServer.newInstance(io.vertx.ext.shell.term.TermServer.createHttpTermServer((io.vertx.core.Vertx)vertx.getDelegate()));
    return ret;
  }

  /**
   * Create a term server for the HTTP protocol.
   * @param vertx the vertx instance
   * @param options the term options
   * @return the term server
   */
  public static TermServer createHttpTermServer(Vertx vertx, HttpTermOptions options) { 
    TermServer ret = TermServer.newInstance(io.vertx.ext.shell.term.TermServer.createHttpTermServer((io.vertx.core.Vertx)vertx.getDelegate(), options));
    return ret;
  }

  /**
   * Create a term server for the HTTP protocol, using an existing router.
   * @param vertx the vertx instance
   * @param router the router
   * @return the term server
   */
  public static TermServer createHttpTermServer(Vertx vertx, Router router) { 
    TermServer ret = TermServer.newInstance(io.vertx.ext.shell.term.TermServer.createHttpTermServer((io.vertx.core.Vertx)vertx.getDelegate(), (io.vertx.ext.web.Router)router.getDelegate()));
    return ret;
  }

  /**
   * Create a term server for the HTTP protocol, using an existing router.
   * @param vertx the vertx instance
   * @param router the router
   * @param options the term options
   * @return the term server
   */
  public static TermServer createHttpTermServer(Vertx vertx, Router router, HttpTermOptions options) { 
    TermServer ret = TermServer.newInstance(io.vertx.ext.shell.term.TermServer.createHttpTermServer((io.vertx.core.Vertx)vertx.getDelegate(), (io.vertx.ext.web.Router)router.getDelegate(), options));
    return ret;
  }

  /**
   * Set the term handler that will receive incoming client connections. When a remote terminal connects
   * the <code>handler</code> will be called with the {@link io.vertx.rxjava.ext.shell.term.Term} which can be used to interact with the remote
   * terminal.
   * @param handler the term handler
   * @return this object
   */
  public TermServer termHandler(Handler<Term> handler) { 
    delegate.termHandler(new Handler<io.vertx.ext.shell.term.Term>() {
      public void handle(io.vertx.ext.shell.term.Term event) {
        handler.handle(Term.newInstance(event));
      }
    });
    return this;
  }

  /**
   * Set an auth provider to use, any provider configured in options will override this provider. This should be used
   * when a custom auth provider should be used.
   * @param provider the auth to use
   * @return this object
   */
  public TermServer authProvider(AuthProvider provider) { 
    delegate.authProvider((io.vertx.ext.auth.AuthProvider)provider.getDelegate());
    return this;
  }

  /**
   * Bind the term server, the {@link io.vertx.rxjava.ext.shell.term.TermServer#termHandler} must be set before.
   * @return this object
   */
  public TermServer listen() { 
    delegate.listen();
    return this;
  }

  /**
   * Bind the term server, the {@link io.vertx.rxjava.ext.shell.term.TermServer#termHandler} must be set before.
   * @param listenHandler the listen handler
   * @return this object
   */
  public TermServer listen(Handler<AsyncResult<TermServer>> listenHandler) { 
    delegate.listen(new Handler<AsyncResult<io.vertx.ext.shell.term.TermServer>>() {
      public void handle(AsyncResult<io.vertx.ext.shell.term.TermServer> ar) {
        if (ar.succeeded()) {
          listenHandler.handle(io.vertx.core.Future.succeededFuture(TermServer.newInstance(ar.result())));
        } else {
          listenHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    });
    return this;
  }

  /**
   * Bind the term server, the {@link io.vertx.rxjava.ext.shell.term.TermServer#termHandler} must be set before.
   * @return 
   */
  public Observable<TermServer> listenObservable() { 
    io.vertx.rx.java.ObservableFuture<TermServer> listenHandler = io.vertx.rx.java.RxHelper.observableFuture();
    listen(listenHandler.toHandler());
    return listenHandler;
  }

  /**
   * The actual port the server is listening on. This is useful if you bound the server specifying 0 as port number
   * signifying an ephemeral port
   * @return the actual port the server is listening on.
   */
  public int actualPort() { 
    int ret = delegate.actualPort();
    return ret;
  }

  /**
   * Close the server. This will close any currently open connections. The close may not complete until after this
   * method has returned.
   */
  public void close() { 
    delegate.close();
  }

  /**
   * Like {@link io.vertx.rxjava.ext.shell.term.TermServer#close} but supplying a handler that will be notified when close is complete.
   * @param completionHandler the handler to be notified when the term server is closed
   */
  public void close(Handler<AsyncResult<Void>> completionHandler) { 
    delegate.close(new Handler<AsyncResult<java.lang.Void>>() {
      public void handle(AsyncResult<java.lang.Void> ar) {
        if (ar.succeeded()) {
          completionHandler.handle(io.vertx.core.Future.succeededFuture(ar.result()));
        } else {
          completionHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
        }
      }
    });
  }

  /**
   * Like {@link io.vertx.rxjava.ext.shell.term.TermServer#close} but supplying a handler that will be notified when close is complete.
   * @return 
   */
  public Observable<Void> closeObservable() { 
    io.vertx.rx.java.ObservableFuture<Void> completionHandler = io.vertx.rx.java.RxHelper.observableFuture();
    close(completionHandler.toHandler());
    return completionHandler;
  }


  public static TermServer newInstance(io.vertx.ext.shell.term.TermServer arg) {
    return arg != null ? new TermServer(arg) : null;
  }
}
