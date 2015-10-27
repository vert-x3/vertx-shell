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
import io.vertx.ext.shell.ShellServiceOptions
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.shell.registry.CommandRegistry
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
/**
 * The shell service, provides a remotely accessible shell available via Telnet or SSH according to the
 * <a href="../../../../../../../cheatsheet/ShellServiceOptions.html">ShellServiceOptions</a> configuration.
*/
@CompileStatic
public class ShellService {
  private final def io.vertx.ext.shell.ShellService delegate;
  public ShellService(Object delegate) {
    this.delegate = (io.vertx.ext.shell.ShellService) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public static ShellService create(Vertx vertx) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.ShellService.create((io.vertx.core.Vertx)vertx.getDelegate()), io.vertx.groovy.ext.shell.ShellService.class);
    return ret;
  }
  public static ShellService create(Vertx vertx, Map<String, Object> options) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.ShellService.create((io.vertx.core.Vertx)vertx.getDelegate(), options != null ? new io.vertx.ext.shell.ShellServiceOptions(new io.vertx.core.json.JsonObject(options)) : null), io.vertx.groovy.ext.shell.ShellService.class);
    return ret;
  }
  /**
   * @return the command registry for this service
   * @return 
   */
  public CommandRegistry getCommandRegistry() {
    def ret= InternalHelper.safeCreate(this.delegate.getCommandRegistry(), io.vertx.groovy.ext.shell.registry.CommandRegistry.class);
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
  public Shell createShell() {
    def ret= InternalHelper.safeCreate(this.delegate.createShell(), io.vertx.groovy.ext.shell.system.Shell.class);
    return ret;
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
}
