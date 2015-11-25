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
import io.vertx.ext.shell.command.base.BusSend;
import io.vertx.ext.shell.command.base.BusTail;
import io.vertx.ext.shell.command.base.Echo;
import io.vertx.ext.shell.command.base.FileSystemCd;
import io.vertx.ext.shell.command.base.FileSystemLs;
import io.vertx.ext.shell.command.base.FileSystemPwd;
import io.vertx.ext.shell.command.base.Help;
import io.vertx.ext.shell.command.base.LocalMapGet;
import io.vertx.ext.shell.command.base.LocalMapPut;
import io.vertx.ext.shell.command.base.LocalMapRm;
import io.vertx.ext.shell.command.base.NetCommandLs;
import io.vertx.ext.shell.command.base.Sleep;
import io.vertx.ext.shell.command.base.VerticleDeploy;
import io.vertx.ext.shell.command.base.VerticleFactories;
import io.vertx.ext.shell.command.base.VerticleLs;
import io.vertx.ext.shell.command.base.VerticleUndeploy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A Vert.x Shell command, it can be created from any language using the {@link CommandBuilder#command} or from a
 * Java class using {@link Command#create}
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Command {

  /**
   * @return the list of base command classes
   */
  @GenIgnore
  static List<Class<? extends Command>> baseCommandClasses() {
    List<Class<? extends Command>> list = new ArrayList<>();
    list.add(Echo.class);
    list.add(Sleep.class);
    list.add(Help.class);
    list.add(FileSystemCd.class);
    list.add(FileSystemPwd.class);
    list.add(FileSystemLs.class);
    list.add(NetCommandLs.class);
    list.add(LocalMapGet.class);
    list.add(LocalMapPut.class);
    list.add(LocalMapRm.class);
    list.add(BusSend.class);
    list.add(BusTail.class);
    list.add(VerticleLs.class);
    list.add(VerticleDeploy.class);
    list.add(VerticleUndeploy.class);
    list.add(VerticleFactories.class);
    return list;
  }

  /**
   * @return the list of base commands
   */
  static List<Command> baseCommands() {
    return baseCommandClasses().stream().map(Command::create).collect(Collectors.toList());
  }

  /**
   * Create a command from a Java class, annotated with Vert.x Core CLI annotations.
   *
   * @param clazz the class of the command
   * @return the command object
   */
  @GenIgnore
  static Command create(Class<? extends Command> clazz) {
    CLI cli = CLIConfigurator.define(clazz);
    cli.addOption(new Option().setArgName("help").setFlag(true).setShortName("h").setLongName("help").setDescription("this help").setHelp(true));

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
