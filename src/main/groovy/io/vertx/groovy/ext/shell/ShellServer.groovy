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

package io.vertx.groovy.ext.shell;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.ext.shell.ShellServerOptions
import io.vertx.groovy.ext.shell.term.TermServer
import io.vertx.groovy.core.Vertx
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.groovy.ext.shell.command.CommandResolver
import io.vertx.groovy.ext.shell.term.Term
/**
 * The shell server.<p/>
 *
 * A shell server is associated with a collection of : the {@link io.vertx.groovy.ext.shell.ShellServer#registerTermServer}
 * method registers a term server. Term servers life cycle are managed by this server.<p/>
 *
 * When a  receives an incoming connection, a  instance is created and
 * associated with this connection.<p/>
 *
 * The {@link io.vertx.groovy.ext.shell.ShellServer#createShell} method can be used to create  instance for testing purposes.
*/
@CompileStatic
public class ShellServer {
  private final def io.vertx.ext.shell.ShellServer delegate;
  public ShellServer(Object delegate) {
    this.delegate = (io.vertx.ext.shell.ShellServer) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Create a new shell server with default options.
   * @param vertx the vertx
   * @param options the options (see <a href="../../../../../../../cheatsheet/ShellServerOptions.html">ShellServerOptions</a>)
   * @return the created shell server
   */
  public static ShellServer create(Vertx vertx, Map<String, Object> options) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.shell.ShellServer.create(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null, options != null ? new io.vertx.ext.shell.ShellServerOptions(new io.vertx.core.json.JsonObject(options)) : null), io.vertx.groovy.ext.shell.ShellServer.class);
    return ret;
  }
  /**
   * Create a new shell server with specific options.
   * @param vertx the vertx
   * @return the created shell server
   */
  public static ShellServer create(Vertx vertx) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.shell.ShellServer.create(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null), io.vertx.groovy.ext.shell.ShellServer.class);
    return ret;
  }
  /**
   * Register a command resolver for this server.
   * @param resolver the resolver
   * @return a reference to this, so the API can be used fluently
   */
  public ShellServer registerCommandResolver(CommandResolver resolver) {
    delegate.registerCommandResolver(resolver != null ? (io.vertx.ext.shell.command.CommandResolver)resolver.getDelegate() : null);
    return this;
  }
  /**
   * Register a term server to this shell server, the term server lifecycle methods are managed by this shell server.
   * @param termServer the term server to add
   * @return a reference to this, so the API can be used fluently
   */
  public ShellServer registerTermServer(TermServer termServer) {
    delegate.registerTermServer(termServer != null ? (io.vertx.ext.shell.term.TermServer)termServer.getDelegate() : null);
    return this;
  }
  /**
   * Create a new shell, the returned shell should be closed explicitely.
   * @param term the shell associated terminal
   * @return the created shell
   */
  public Shell createShell(Term term) {
    def ret = InternalHelper.safeCreate(delegate.createShell(term != null ? (io.vertx.ext.shell.term.Term)term.getDelegate() : null), io.vertx.groovy.ext.shell.Shell.class);
    return ret;
  }
  /**
   * Create a new shell, the returned shell should be closed explicitely.
   * @return the created shell
   */
  public Shell createShell() {
    def ret = InternalHelper.safeCreate(delegate.createShell(), io.vertx.groovy.ext.shell.Shell.class);
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
}
