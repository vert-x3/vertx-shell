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
import io.vertx.groovy.ext.shell.system.Shell
import io.vertx.groovy.ext.shell.term.TermServer
import io.vertx.groovy.core.Vertx
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.groovy.ext.shell.command.CommandResolver
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
   * Create a new shell server.
   * @param vertx the vertx
   * @return the created shell server
   */
  public static ShellServer create(Vertx vertx) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.ShellServer.create((io.vertx.core.Vertx)vertx.getDelegate()), io.vertx.groovy.ext.shell.ShellServer.class);
    return ret;
  }
  /**
   * Set the command resolver for this server.
   * @param resolver the resolver
   * @return a reference to this, so the API can be used fluently
   */
  public ShellServer commandResolver(CommandResolver resolver) {
    this.delegate.commandResolver((io.vertx.ext.shell.command.CommandResolver)resolver.getDelegate());
    return this;
  }
  /**
   * Set the shell welcome message.
   * @param msg the welcome message
   * @return a reference to this, so the API can be used fluently
   */
  public ShellServer setWelcomeMessage(String msg) {
    this.delegate.setWelcomeMessage(msg);
    return this;
  }
  /**
   * Register a term server to this shell server, the term server lifecycle methods are managed by this shell server.
   * @param termServer the term server to add
   * @return a reference to this, so the API can be used fluently
   */
  public ShellServer registerTermServer(TermServer termServer) {
    this.delegate.registerTermServer((io.vertx.ext.shell.term.TermServer)termServer.getDelegate());
    return this;
  }
  /**
   * Create a new shell, the returned shell should be closed explicitely.
   * @return the created shell
   */
  public Shell createShell() {
    def ret= InternalHelper.safeCreate(this.delegate.createShell(), io.vertx.groovy.ext.shell.system.Shell.class);
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
}