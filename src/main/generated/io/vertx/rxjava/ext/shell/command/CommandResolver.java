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

package io.vertx.rxjava.ext.shell.command;

import java.util.Map;
import rx.Observable;
import java.util.List;
import io.vertx.rxjava.core.Vertx;

/**
 * A resolver for commands, so the shell can discover commands.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.command.CommandResolver original} non RX-ified interface using Vert.x codegen.
 */

public class CommandResolver {

  final io.vertx.ext.shell.command.CommandResolver delegate;

  public CommandResolver(io.vertx.ext.shell.command.CommandResolver delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * @return the base commands of Vert.x Shell.
   * @param vertx 
   * @return 
   */
  public static CommandResolver baseCommands(Vertx vertx) { 
    CommandResolver ret = CommandResolver.newInstance(io.vertx.ext.shell.command.CommandResolver.baseCommands((io.vertx.core.Vertx)vertx.getDelegate()));
    return ret;
  }

  /**
   * @return the current commands
   * @return 
   */
  public List<Command> commands() { 
    List<Command> ret = delegate.commands().stream().map(elt -> Command.newInstance(elt)).collect(java.util.stream.Collectors.toList());
    return ret;
  }

  /**
   * Returns a single command by its name.
   * @param name the command name
   * @return the commad or null
   */
  public Command getCommand(String name) { 
    Command ret = Command.newInstance(delegate.getCommand(name));
    return ret;
  }


  public static CommandResolver newInstance(io.vertx.ext.shell.command.CommandResolver arg) {
    return arg != null ? new CommandResolver(arg) : null;
  }
}
