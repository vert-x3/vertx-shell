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

package io.vertx.ext.shell.registry.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.registry.CommandRegistration;
import io.vertx.ext.shell.system.Process;

import java.util.Collections;
import java.util.List;

/**
 * The command registered with a manager.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CommandRegistrationImpl implements CommandRegistration {

  final CommandRegistryImpl registry;
  final Command command;
  final Context context;
  final String deploymendID;

  public CommandRegistrationImpl(CommandRegistryImpl registry, Command command, Context context, String deploymentID) {
    this.registry = registry;
    this.command = command;
    this.deploymendID = deploymentID;
    this.context = context;
  }

  public Command command() {
    return command;
  }

  public Process createProcess() {
    return createProcess(Collections.emptyList());
  }

  public void complete(Completion completion) {
    context.runOnContext(v -> {
      try {
        command.complete(completion);
      } catch (Throwable t) {
        completion.complete(Collections.emptyList());
        throw t;
      }
    });
  }

  public Process createProcess(List<CliToken> args) {
    return new ProcessImpl(this, args);
  }

  @Override
  public void unregister() {
    unregister(null);
  }

  @Override
  public void unregister(Handler<AsyncResult<Void>> handler) {
    if (handler != null) {
      registry.vertx.undeploy(deploymendID, handler);
    } else {
      registry.vertx.undeploy(deploymendID);
    }
  }

}
