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

package io.vertx.ext.shell.command.impl;

import io.vertx.core.Handler;
import io.vertx.core.cli.CLI;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandProcess;

import java.util.Collections;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CommandBuilderImpl implements CommandBuilder {

  final String name;
  final CLI cli;
  public Handler<CommandProcess> processHandler;
  public Handler<Completion> completeHandler;

  public CommandBuilderImpl(String name, CLI cli) {
    this.name = name;
    this.cli = cli;
  }

  @Override
  public CommandBuilderImpl processHandler(Handler<CommandProcess> handler) {
    processHandler = handler;
    return this;
  }

  @Override
  public CommandBuilderImpl completionHandler(Handler<Completion> handler) {
    completeHandler = handler;
    return this;
  }

  @Override
  public Command build() {
    return new Command() {
      @Override
      public String name() {
        return name;
      }
      @Override
      public CLI cli() {
        return cli;
      }

      @Override
      public void process(CommandProcess process) {
        processHandler.handle(process);
      }

      @Override
      public void complete(Completion completion) {
        if (completeHandler != null) {
          completeHandler.handle(completion);
        } else {
          Command.super.complete(completion);
        }
      }
    };
  }
}
