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
import io.vertx.core.json.JsonObject
import java.util.List
import io.vertx.groovy.core.Vertx
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
/**
 * A command pack is a set of commands, for instance the base command pack, the metrics command pack, etc...
*/
@CompileStatic
public class CommandPack {
  private final def io.vertx.ext.shell.command.CommandPack delegate;
  public CommandPack(Object delegate) {
    this.delegate = (io.vertx.ext.shell.command.CommandPack) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Lookup commands.
   * @param vertx the vertx instance
   * @param commandsHandler the handler that will receive the lookup callback
   */
  public void lookupCommands(Vertx vertx, Handler<AsyncResult<List<Command>>> commandsHandler) {
    this.delegate.lookupCommands((io.vertx.core.Vertx)vertx.getDelegate(), new Handler<AsyncResult<List<io.vertx.ext.shell.command.Command>>>() {
      public void handle(AsyncResult<List<io.vertx.ext.shell.command.Command>> event) {
        AsyncResult<List<Command>> f
        if (event.succeeded()) {
          f = InternalHelper.<List<Command>>result(event.result().collect({
            io.vertx.ext.shell.command.Command element ->
            new io.vertx.groovy.ext.shell.command.Command(element)
          }) as List)
        } else {
          f = InternalHelper.<List<Command>>failure(event.cause())
        }
        commandsHandler.handle(f)
      }
    });
  }
}
