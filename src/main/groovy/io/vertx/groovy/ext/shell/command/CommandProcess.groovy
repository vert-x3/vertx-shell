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

package io.vertx.groovy.ext.shell.command;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import java.util.List
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.shell.cli.CliToken
import io.vertx.core.Handler
import io.vertx.groovy.ext.shell.Stream
/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
@CompileStatic
public class CommandProcess {
  final def io.vertx.ext.shell.command.CommandProcess delegate;
  public CommandProcess(io.vertx.ext.shell.command.CommandProcess delegate) {
    this.delegate = delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * @return the current Vert.x instance
   * @return 
   */
  public Vertx vertx() {
    def ret= InternalHelper.safeCreate(this.delegate.vertx(), io.vertx.core.Vertx.class, io.vertx.groovy.core.Vertx.class);
    return ret;
  }
  public List<CliToken> args() {
    def ret = this.delegate.args()?.collect({underpants -> new io.vertx.groovy.ext.shell.cli.CliToken(underpants)});
      return ret;
  }
  public int width() {
    def ret = this.delegate.width();
    return ret;
  }
  public int height() {
    def ret = this.delegate.height();
    return ret;
  }
  public CommandProcess setStdin(Stream stdin) {
    this.delegate.setStdin((io.vertx.ext.shell.Stream)stdin.getDelegate());
    return this;
  }
  public CommandProcess eventHandler(String event, Handler<Void> handler) {
    this.delegate.eventHandler(event, handler);
    return this;
  }
  public Stream stdout() {
    def ret= InternalHelper.safeCreate(this.delegate.stdout(), io.vertx.ext.shell.Stream.class, io.vertx.groovy.ext.shell.Stream.class);
    return ret;
  }
  public CommandProcess write(String text) {
    this.delegate.write(text);
    return this;
  }
  /**
   * End the process with the exit status 
   */
  public void end() {
    this.delegate.end();
  }
  /**
   * End the process.
   * @param status the exit status.
   */
  public void end(int status) {
    this.delegate.end(status);
  }
}
