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

package io.vertx.rxjava.ext.shell.registry;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.ext.shell.command.Command;
import java.util.List;
import io.vertx.rxjava.ext.shell.cli.Completion;
import io.vertx.rxjava.ext.shell.cli.CliToken;
import io.vertx.rxjava.ext.shell.process.Process;

/**
 * A registration of a command in the {@link io.vertx.rxjava.ext.shell.registry.CommandRegistry}
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.registry.CommandRegistration original} non RX-ified interface using Vert.x codegen.
 */

public class CommandRegistration {

  final io.vertx.ext.shell.registry.CommandRegistration delegate;

  public CommandRegistration(io.vertx.ext.shell.registry.CommandRegistration delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * @return the registered command.
   * @return 
   */
  public Command command() { 
    Command ret= Command.newInstance(this.delegate.command());
    return ret;
  }

  /**
   * Complete the command for the given completion.
   * @param completion the completion
   */
  public void complete(Completion completion) { 
    this.delegate.complete((io.vertx.ext.shell.cli.Completion) completion.getDelegate());
  }

  /**
   * Create a new process with the passed arguments.
   * @param args the process arguments
   * @return the process
   */
  public Process createProcess(List<CliToken> args) { 
    Process ret= Process.newInstance(this.delegate.createProcess(args.stream().map(element -> (io.vertx.ext.shell.cli.CliToken)element.getDelegate()).collect(java.util.stream.Collectors.toList())));
    return ret;
  }


  public static CommandRegistration newInstance(io.vertx.ext.shell.registry.CommandRegistration arg) {
    return arg != null ? new CommandRegistration(arg) : null;
  }
}
