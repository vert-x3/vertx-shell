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
import io.vertx.rxjava.ext.shell.cli.Completion;
import io.vertx.rxjava.core.cli.CLI;
import io.vertx.rxjava.ext.shell.cli.CliToken;
import io.vertx.rxjava.ext.shell.system.Process;

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
   * @return the command name
   */
  public String name() { 
    String ret = delegate.name();
    return ret;
  }

  /**
   * @return the command line interface, can be null
   */
  public CLI cli() { 
    CLI ret = CLI.newInstance(delegate.cli());
    return ret;
  }

  /**
   * Create a new process with empty arguments.
   * @return the process
   */
  public Process createProcess() { 
    Process ret = Process.newInstance(delegate.createProcess());
    return ret;
  }

  /**
   * Create a new process with the passed arguments.
   * @param args the process arguments
   * @return the process
   */
  public Process createProcess(List<CliToken> args) { 
    Process ret = Process.newInstance(delegate.createProcess(args.stream().map(elt -> (io.vertx.ext.shell.cli.CliToken)elt.getDelegate()).collect(java.util.stream.Collectors.toList())));
    return ret;
  }

  /**
   * Perform command completion, when the command is done completing it should call 
   * or  )} method to signal completion is done.
   * @param completion the completion object
   */
  public void complete(Completion completion) { 
    delegate.complete((io.vertx.ext.shell.cli.Completion)completion.getDelegate());
  }


  public static Command newInstance(io.vertx.ext.shell.command.Command arg) {
    return arg != null ? new Command(arg) : null;
  }
}
