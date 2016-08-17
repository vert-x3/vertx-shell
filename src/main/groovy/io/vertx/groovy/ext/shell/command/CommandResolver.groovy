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
/**
 * A resolver for commands, so the shell can discover commands.
*/
@CompileStatic
public class CommandResolver {
  private final def io.vertx.ext.shell.command.CommandResolver delegate;
  public CommandResolver(Object delegate) {
    this.delegate = (io.vertx.ext.shell.command.CommandResolver) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * @param vertx 
   * @return the base commands of Vert.x Shell.
   */
  public static CommandResolver baseCommands(Vertx vertx) {
    def ret = InternalHelper.safeCreate(io.vertx.ext.shell.command.CommandResolver.baseCommands(vertx != null ? (io.vertx.core.Vertx)vertx.getDelegate() : null), io.vertx.groovy.ext.shell.command.CommandResolver.class);
    return ret;
  }
  /**
   * @return the current commands
   */
  public List<Command> commands() {
    def ret = (List)delegate.commands()?.collect({InternalHelper.safeCreate(it, io.vertx.groovy.ext.shell.command.Command.class)});
    return ret;
  }
  /**
   * Returns a single command by its name.
   * @param name the command name
   * @return the commad or null
   */
  public Command getCommand(String name) {
    def ret = InternalHelper.safeCreate(delegate.getCommand(name), io.vertx.groovy.ext.shell.command.Command.class);
    return ret;
  }
}
