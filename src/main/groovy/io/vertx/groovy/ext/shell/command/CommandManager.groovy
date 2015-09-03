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
import io.vertx.groovy.ext.shell.cli.Completion
import io.vertx.groovy.core.Vertx
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.groovy.ext.shell.cli.CliToken
import io.vertx.groovy.ext.shell.process.Process
/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
@CompileStatic
public class CommandManager {
  final def io.vertx.ext.shell.command.CommandManager delegate;
  public CommandManager(io.vertx.ext.shell.command.CommandManager delegate) {
    this.delegate = delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public static CommandManager get(Vertx vertx) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.command.CommandManager.get((io.vertx.core.Vertx)vertx.getDelegate()), io.vertx.ext.shell.command.CommandManager.class, io.vertx.groovy.ext.shell.command.CommandManager.class);
    return ret;
  }
  public List<ManagedCommand> commands() {
    def ret = this.delegate.commands()?.collect({underpants -> new io.vertx.groovy.ext.shell.command.ManagedCommand(underpants)});
      return ret;
  }
  public void createProcess(String s, Handler<AsyncResult<Process>> handler) {
    this.delegate.createProcess(s, new Handler<AsyncResult<io.vertx.ext.shell.process.Process>>() {
      public void handle(AsyncResult<io.vertx.ext.shell.process.Process> event) {
        AsyncResult<Process> f
        if (event.succeeded()) {
          f = InternalHelper.<Process>result(new Process(event.result()))
        } else {
          f = InternalHelper.<Process>failure(event.cause())
        }
        handler.handle(f)
      }
    });
  }
  public void createProcess(List<CliToken> line, Handler<AsyncResult<Process>> handler) {
    this.delegate.createProcess((List<io.vertx.ext.shell.cli.CliToken>)(line.collect({underpants -> underpants.getDelegate()})), new Handler<AsyncResult<io.vertx.ext.shell.process.Process>>() {
      public void handle(AsyncResult<io.vertx.ext.shell.process.Process> event) {
        AsyncResult<Process> f
        if (event.succeeded()) {
          f = InternalHelper.<Process>result(new Process(event.result()))
        } else {
          f = InternalHelper.<Process>failure(event.cause())
        }
        handler.handle(f)
      }
    });
  }
  public void complete(Completion completion) {
    this.delegate.complete((io.vertx.ext.shell.cli.Completion)completion.getDelegate());
  }
  public void registerCommand(Command command) {
    this.delegate.registerCommand((io.vertx.ext.shell.command.Command)command.getDelegate());
  }
  public void registerCommand(Command command, Handler<AsyncResult<Void>> handler) {
    this.delegate.registerCommand((io.vertx.ext.shell.command.Command)command.getDelegate(), handler);
  }
  public void release() {
    this.delegate.release();
  }
}
