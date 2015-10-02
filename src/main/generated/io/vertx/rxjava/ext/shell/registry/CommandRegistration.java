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
