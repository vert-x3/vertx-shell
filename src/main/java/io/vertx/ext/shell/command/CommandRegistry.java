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

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.internal.VertxInternal;
import io.vertx.ext.shell.command.impl.CommandRegistryImpl;

import java.util.List;

/**
 * A registry that contains the commands known by a shell.<p/>
 *
 * It is a mutable command resolver.
 *
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CommandRegistry extends CommandResolver {

  /**
   * Get the shared registry for the Vert.x instance.
   *
   * @param vertx the vertx instance
   * @return the shared registry
   */
  static CommandRegistry getShared(Vertx vertx) {
    return CommandRegistryImpl.get(vertx);
  }

  /**
   * Create a new registry.
   *
   * @param vertx the vertx instance
   * @return the created registry
   */
  static CommandRegistry create(Vertx vertx) {
    return new CommandRegistryImpl((VertxInternal) vertx);
  }

  /**
   * Register a single command.
   *
   * @param command the class of the command to register
   * @return a future notified when the command is registered
   */
  @GenIgnore
  Future<Command> registerCommand(Class<? extends AnnotatedCommand> command);

  /**
   * Register a command
   *
   * @param command the command to register
   * @return a future notified when the command is registered
   */
  Future<Command> registerCommand(Command command);

  /**
   * Register a list of commands.
   *
   * @param commands the commands to register
   * @return a future notified when the command is registered
   */
  Future<List<Command>> registerCommands(List<Command> commands);

  /**
   * Unregister a command.
   *
   * @param commandName the command name
   * @return a future notified when the command is unregistered
   */
  Future<Void> unregisterCommand(String commandName);

}
