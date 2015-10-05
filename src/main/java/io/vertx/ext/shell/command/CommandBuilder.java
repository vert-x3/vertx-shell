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

package io.vertx.ext.shell.command;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.cli.CLI;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.impl.CommandBuilderImpl;

/**
 * A build for Vert.x Shell command.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CommandBuilder {

  /**
   * Create a new commmand builder, the command is responsible for managing the options and arguments via the
   * {@link CommandProcess#args() arguments}.
   *
   * @param name the command name
   * @return the command
   */
  static CommandBuilder command(String name) {
    return new CommandBuilderImpl(name, null);
  }

  /**
   * Create a new commmand with its {@link io.vertx.core.cli.CLI} descriptor. This command can then retrieve the parsed
   * {@link CommandProcess#commandLine()} when it executes to know get the command arguments and options.
   *
   * @param cli the cli to use
   * @return the command
   */
  static CommandBuilder command(CLI cli) {
    return new CommandBuilderImpl(cli.getName(), cli);
  }

  /**
   * Set the command process handler, the process handler is called when the command is executed.
   *
   * @param handler the process handler
   * @return this command object
   */
  @Fluent
  CommandBuilder processHandler(Handler<CommandProcess> handler);

  /**
   * Set the command completion handler, the completion handler when the user asks for contextual command line
   * completion, usually hitting the <i>tab</i> key.
   *
   * @param handler the completion handler
   * @return this command object
   */
  @Fluent
  CommandBuilder completionHandler(Handler<Completion> handler);

  /**
   * Build the command
   *
   * @return the built command
   */
  Command build();

}
