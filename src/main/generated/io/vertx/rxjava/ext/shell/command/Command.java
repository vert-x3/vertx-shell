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
import io.vertx.rxjava.ext.shell.cli.Completion;
import io.vertx.rxjava.core.cli.CLI;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
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
   * Create a new commmand, the command is responsible for managing the options and arguments via the
   * {@link io.vertx.rxjava.ext.shell.command.CommandProcess #args() arguments}.
   * @param name the command name
   * @return the command
   */
  public static CommandBuilder builder(String name) { 
    CommandBuilder ret= CommandBuilder.newInstance(io.vertx.ext.shell.command.Command.builder(name));
    return ret;
  }

  /**
   * Create a new commmand with its {@link io.vertx.rxjava.core.cli.CLI} descriptor. This command can then retrieve the parsed
   * {@link io.vertx.rxjava.ext.shell.command.CommandProcess#commandLine} when it executes to know get the command arguments and options.
   * @param cli the cli to use
   * @return the command
   */
  public static CommandBuilder builder(CLI cli) { 
    CommandBuilder ret= CommandBuilder.newInstance(io.vertx.ext.shell.command.Command.builder((io.vertx.core.cli.CLI) cli.getDelegate()));
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

  public void process(CommandProcess process) { 
    this.delegate.process((io.vertx.ext.shell.command.CommandProcess) process.getDelegate());
  }

  public void complete(Completion completion) { 
    this.delegate.complete((io.vertx.ext.shell.cli.Completion) completion.getDelegate());
  }


  public static Command newInstance(io.vertx.ext.shell.command.Command arg) {
    return arg != null ? new Command(arg) : null;
  }
}
