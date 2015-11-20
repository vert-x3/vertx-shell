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

package io.vertx.groovy.ext.shell.term;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.ext.shell.term.TelnetTermOptions
import io.vertx.ext.shell.term.SSHTermOptions
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.web.Router
import io.vertx.ext.shell.term.WebTermOptions
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
/**
 * A server for terminal based applications.
*/
@CompileStatic
public class TermServer {
  private final def io.vertx.ext.shell.term.TermServer delegate;
  public TermServer(Object delegate) {
    this.delegate = (io.vertx.ext.shell.term.TermServer) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Create a term server for the SSH protocol.
   * @param vertx the vertx instance
   * @param options the ssh options (see <a href="../../../../../../../../cheatsheet/SSHTermOptions.html">SSHTermOptions</a>)
   * @return the term server
   */
  public static TermServer createSSHTermServer(Vertx vertx, Map<String, Object> options) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.term.TermServer.createSSHTermServer((io.vertx.core.Vertx)vertx.getDelegate(), options != null ? new io.vertx.ext.shell.term.SSHTermOptions(new io.vertx.core.json.JsonObject(options)) : null), io.vertx.groovy.ext.shell.term.TermServer.class);
    return ret;
  }
  /**
   * Create a term server for the Telnet protocol.
   * @param vertx the vertx instance
   * @param options the telnet options (see <a href="../../../../../../../../cheatsheet/TelnetTermOptions.html">TelnetTermOptions</a>)
   * @return the term server
   */
  public static TermServer createTelnetTermServer(Vertx vertx, Map<String, Object> options) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.term.TermServer.createTelnetTermServer((io.vertx.core.Vertx)vertx.getDelegate(), options != null ? new io.vertx.ext.shell.term.TelnetTermOptions(new io.vertx.core.json.JsonObject(options)) : null), io.vertx.groovy.ext.shell.term.TermServer.class);
    return ret;
  }
  public static TermServer createWebTermServer(Vertx vertx, Map<String, Object> options) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.term.TermServer.createWebTermServer((io.vertx.core.Vertx)vertx.getDelegate(), options != null ? new io.vertx.ext.shell.term.WebTermOptions(new io.vertx.core.json.JsonObject(options)) : null), io.vertx.groovy.ext.shell.term.TermServer.class);
    return ret;
  }
  public static TermServer createWebTermServer(Vertx vertx, Router router, Map<String, Object> options) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.term.TermServer.createWebTermServer((io.vertx.core.Vertx)vertx.getDelegate(), (io.vertx.ext.web.Router)router.getDelegate(), options != null ? new io.vertx.ext.shell.term.WebTermOptions(new io.vertx.core.json.JsonObject(options)) : null), io.vertx.groovy.ext.shell.term.TermServer.class);
    return ret;
  }
  /**
   * Set the term handler that will receive incoming client connections. When a remote terminal connects
   * the <code>handler</code> will be called with the {@link io.vertx.groovy.ext.shell.term.Term} which can be used to interact with the remote
   * terminal.
   * @param handler the term handler
   * @return this object
   */
  public TermServer termHandler(Handler<Term> handler) {
    this.delegate.termHandler(new Handler<io.vertx.ext.shell.term.Term>() {
      public void handle(io.vertx.ext.shell.term.Term event) {
        handler.handle(new io.vertx.groovy.ext.shell.term.Term(event));
      }
    });
    return this;
  }
  /**
   * Bind the term server, the {@link io.vertx.groovy.ext.shell.term.TermServer#termHandler} must be set before.
   * @return this object
   */
  public TermServer listen() {
    this.delegate.listen();
    return this;
  }
  /**
   * Bind the term server, the {@link io.vertx.groovy.ext.shell.term.TermServer#termHandler} must be set before.
   * @param listenHandler the listen handler
   * @return this object
   */
  public TermServer listen(Handler<AsyncResult<TermServer>> listenHandler) {
    this.delegate.listen(new Handler<AsyncResult<io.vertx.ext.shell.term.TermServer>>() {
      public void handle(AsyncResult<io.vertx.ext.shell.term.TermServer> event) {
        AsyncResult<TermServer> f
        if (event.succeeded()) {
          f = InternalHelper.<TermServer>result(new TermServer(event.result()))
        } else {
          f = InternalHelper.<TermServer>failure(event.cause())
        }
        listenHandler.handle(f)
      }
    });
    return this;
  }
  /**
   * The actual port the server is listening on. This is useful if you bound the server specifying 0 as port number
   * signifying an ephemeral port
   * @return the actual port the server is listening on.
   */
  public int actualPort() {
    def ret = this.delegate.actualPort();
    return ret;
  }
  /**
   * Close the server. This will close any currently open connections. The close may not complete until after this
   * method has returned.
   */
  public void close() {
    this.delegate.close();
  }
  /**
   * Like {@link io.vertx.groovy.ext.shell.term.TermServer#close} but supplying a handler that will be notified when close is complete.
   * @param completionHandler the handler to be notified when the term server is closed
   */
  public void close(Handler<AsyncResult<Void>> completionHandler) {
    this.delegate.close(completionHandler);
  }
}
