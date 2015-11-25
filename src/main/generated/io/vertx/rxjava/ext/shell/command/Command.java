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
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import java.util.List;
import io.vertx.rxjava.ext.shell.cli.Completion;
import io.vertx.rxjava.core.cli.CLI;

/**
 * A Vert.x Shell command, it can be created from any language using the {@link io.vertx.rxjava.ext.shell.command.CommandBuilder#command} or from a
 * Java class using {@link io.vertx.rxjava.ext.shell.command.Command#create}
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.command.Command original} non RX-ified interface using Vert.x codegen.
 */

public class Command {

  final io.vertx.ext.shell.command.Command delegate;

  public Command(io.vertx.ext.shell.command.Command delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * @return the list of base commands
   * @return 
   */
  public static List<Command> baseCommands() { 
    List<Command> ret = io.vertx.ext.shell.command.Command.baseCommands().stream().map(Command::newInstance).collect(java.util.stream.Collectors.toList());
    return ret;
  }

  /**
   * @return the command name
   * @return 
   */
  public String name() { 
    String ret = this.delegate.name();
    return ret;
  }

  /**
   * @return the command line interface, can be null
   * @return 
   */
  public CLI cli() { 
    CLI ret= CLI.newInstance(this.delegate.cli());
    return ret;
  }

  /**
   * Process the command, when the command is done processing it should call the {@link io.vertx.rxjava.ext.shell.command.CommandProcess#end} method.
   * @param process the command process
   */
  public void process(CommandProcess process) { 
    this.delegate.process((io.vertx.ext.shell.command.CommandProcess) process.getDelegate());
  }

  /**
   * Perform command completion, when the command is done completing it should call 
   * or  )} method to signal completion is done.
   * @param completion the completion object
   */
  public void complete(Completion completion) { 
    this.delegate.complete((io.vertx.ext.shell.cli.Completion) completion.getDelegate());
  }


  public static Command newInstance(io.vertx.ext.shell.command.Command arg) {
    return arg != null ? new Command(arg) : null;
  }
}
