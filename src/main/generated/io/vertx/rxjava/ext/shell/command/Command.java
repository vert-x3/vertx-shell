/*
 * Copyright 2015 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 *
 *
 * Copyright (c) 2015 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 *
 */

package io.vertx.rxjava.ext.shell.command;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.ext.shell.cli.Completion;
import io.vertx.rxjava.core.cli.CLI;

/**
 * A Vert.x Shell command, it can be created from any language using the {@link io.vertx.rxjava.ext.shell.command.Command#builder} or from a
 * Java class using {@link io.vertx.rxjava.ext.shell.command.Command#create}.
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
