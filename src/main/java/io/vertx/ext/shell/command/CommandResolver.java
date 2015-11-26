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

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.base.BaseCommandPack;

import java.util.List;

/**
 * A resolver for commands, so the shell can discover commands.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface CommandResolver {

  /**
   * @return the base commands of Vert.x Shell.
   */
  static CommandResolver baseCommands(Vertx vertx) {
    return new BaseCommandPack(vertx);
  }

  /**
   * @return the current commands
   */
  List<Command> commands();

  /**
   * Returns a single command by its name.
   *
   * @param name the command name
   * @return the commad or null
   */
  default Command getCommand(String name) {
    return commands().stream().filter(cmd -> cmd.name().equals(name)).findFirst().orElse(null);
  }
}
