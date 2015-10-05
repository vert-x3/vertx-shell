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
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.Option;
import io.vertx.core.cli.annotations.CLIConfigurator;
import io.vertx.ext.shell.cli.Completion;

import java.util.Collections;
import java.util.List;

/**
 * A Vert.x Shell command, it can be created from any language using the {@link CommandBuilder#builder} or from a
 * Java class using {@link Command#create}
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Command {

  /**
   * Create a command from a Java class, annotated with Vert.x Core CLI annotations.
   *
   * @param clazz the class of the command
   * @return the command object
   */
  @GenIgnore
  static Command create(Class<? extends Command> clazz) {
    CLI cli = CLIConfigurator.define(clazz);
    cli.addOption(new Option().setArgName("help").setFlag(true).setShortName("h").setLongName("help"));

    boolean tmp = false;
    try {
      clazz.getDeclaredMethod("name");
      tmp = true;
    } catch (NoSuchMethodException ignore) {
    }
    boolean overridesName = tmp;

    tmp = false;
    try {
      clazz.getDeclaredMethod("cli");
      tmp = true;
    } catch (NoSuchMethodException ignore) {
    }
    boolean overridesCli = tmp;

    return new Command() {

      @Override
      public String name() {
        if (overridesName) {
          try {
            return clazz.newInstance().name();
          } catch (Exception ignore) {
            // Use cli.getName() instead
          }
        }
        return cli.getName();
      }

      @Override
      public CLI cli() {
        if (overridesCli) {
          try {
            return clazz.newInstance().cli();
          } catch (Exception ignore) {
            // Use cli instead
          }
        }
        return cli;
      }

      @Override
      public void process(CommandProcess process) {
        Command instance;
        try {
          instance = clazz.newInstance();
        } catch (Exception e) {
          process.end();
          return;
        }
        CLIConfigurator.inject(process.commandLine(), instance);
        instance.process(process);
      }

      @Override
      public void complete(Completion completion) {
        Command instance;
        try {
          instance = clazz.newInstance();
        } catch (Exception e) {
          Command.super.complete(completion);
          return;
        }
        instance.complete(completion);
      }
    };
  }

  /**
   * @return the command name
   */
  default String name() {
    return null;
  }

  /**
   * @return the command line interface, can be null
   */
  default CLI cli() {
    return null;
  }

  /**
   * Process the command, when the command is done processing it should call the {@link CommandProcess#end()} method.
   *
   * @param process the command process
   */
  void process(CommandProcess process);

  /**
   * Perform command completion, when the command is done completing it should call {@link Completion#complete(List)}
   * or {@link Completion#complete(String, boolean)} )} method to signal completion is done.
   *
   * @param completion the completion object
   */
  default void complete(Completion completion) {
    completion.complete(Collections.emptyList());
  }
}
