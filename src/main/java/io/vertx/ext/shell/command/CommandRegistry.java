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
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
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
    return new CommandRegistryImpl(vertx);
  }

  /**
   * Like {@link #registerCommand(Class, Handler)}, without a completion handler.
   */
  @GenIgnore
  CommandRegistry registerCommand(Class<? extends AnnotatedCommand> command);

  /**
   * Register a single command.
   *
   * @param command the class of the command to register
   * @param completionHandler notified when the command is registered
   * @return a reference to this, so the API can be used fluently
   */
  @GenIgnore
  CommandRegistry registerCommand(Class<? extends AnnotatedCommand> command, Handler<AsyncResult<Command>> completionHandler);

  /**
   * Like {@link #registerCommand(Command, Handler)}, without a completion handler.
   */
  @Fluent
  CommandRegistry registerCommand(Command command);

  /**
   * Register a command
   *
   * @param command the command to register
   * @param completionHandler notified when the command is registered
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  CommandRegistry registerCommand(Command command, Handler<AsyncResult<Command>> completionHandler);

  /**
   * Like {@link #registerCommands(List, Handler)}, without a completion handler.
   */
  @Fluent
  CommandRegistry registerCommands(List<Command> commands);

  /**
   * Register a list of commands.
   *
   * @param commands the commands to register
   * @param completionHandler notified when the command is registered
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  CommandRegistry registerCommands(List<Command> commands, Handler<AsyncResult<List<Command>>> completionHandler);

  /**
   * Like {@link #unregisterCommand(String, Handler)}, without a completion handler.
   */
  @Fluent
  CommandRegistry unregisterCommand(String commandName);

  /**
   * Unregister a command.
   *
   * @param commandName the command name
   * @param completionHandler notified when the command is unregistered
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  CommandRegistry unregisterCommand(String commandName, Handler<AsyncResult<Void>> completionHandler);

}
