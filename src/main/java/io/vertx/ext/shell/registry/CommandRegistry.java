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

package io.vertx.ext.shell.registry;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.registry.impl.CommandRegistryImpl;
import io.vertx.ext.shell.system.Process;

import java.util.List;

/**
 * A registry that contains the commands known by a shell.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CommandRegistry {

  /**
   * Get the registry for the Vert.x instance
   *
   * @param vertx the vertx instance
   * @return the registry
   */
  static CommandRegistry get(Vertx vertx) {
    return CommandRegistryImpl.get(vertx);
  }

  /**
   * @return the current command registrations
   */
  List<CommandRegistration> registrations();

  /**
   * Parses a command line and try to create a process.
   *
   * @param line the command line to parse
   * @return the created process
   */
  Process createProcess(String line);

  /**
   * Try to create a process from the command line tokens.
   *
   * @param line the command line tokens
   * @return the created process
   */
  Process createProcess(List<CliToken> line);

  /**
   * Perform completion, the completion argument will be notified of the completion progress.
   *
   * @param completion the completion object
   */
  void complete(Completion completion);

  /**
   * Register a command
   *
   * @param command the class of the command to register
   * @return a reference to this, so the API can be used fluently
   */
  @GenIgnore
  CommandRegistry registerCommand(Class<? extends Command> command);

  @GenIgnore
  CommandRegistry registerCommand(Class<? extends Command> command, Handler<AsyncResult<CommandRegistration>> doneHandler);

  /**
   * Register a command
   *
   * @param command the command to register
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  CommandRegistry registerCommand(Command command);

  @Fluent
  CommandRegistry registerCommand(Command command, Handler<AsyncResult<CommandRegistration>> doneHandler);

  /**
   * Register a list of commands.
   *
   * @param commands the commands to register
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  CommandRegistry registerCommands(List<Command> commands);

  @Fluent
  CommandRegistry registerCommands(List<Command> commands, Handler<AsyncResult<CommandRegistration>> doneHandler);

  /**
   * Unregister a command.
   *
   * @param commandName the command name
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  CommandRegistry unregisterCommand(String commandName);

  @Fluent
  CommandRegistry unregisterCommand(String commandName, Handler<AsyncResult<Void>> doneHandler);

}
