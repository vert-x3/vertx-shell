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

package io.vertx.ext.shell;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.CommandBuilder;
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
import io.vertx.ext.shell.command.metrics.MetricsInfo;
import io.vertx.ext.shell.command.metrics.MetricsLs;
import io.vertx.ext.shell.impl.ShellServiceImpl;
import io.vertx.ext.shell.registry.CommandRegistry;
import io.vertx.ext.shell.system.Shell;

/**
 * The shell service, provides a remotely accessible shell available via Telnet or SSH according to the
 * {@link io.vertx.ext.shell.ShellServiceOptions} configuration.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ShellService {

  static ShellService create(Vertx vertx) {
    return create(vertx, new ShellServiceOptions());
  }

  static ShellService create(Vertx vertx, ShellServiceOptions options) {
    CommandRegistry registry = CommandRegistry.get(vertx);

    // Base commands
    registry.registerCommand(Echo.class);
    registry.registerCommand(Sleep.class);
    registry.registerCommand(Help.class);
    registry.registerCommand(FileSystemCd.class);
    registry.registerCommand(FileSystemPwd.class);
    registry.registerCommand(FileSystemLs.class);
    registry.registerCommand(NetCommandLs.class);
    registry.registerCommand(LocalMapGet.class);
    registry.registerCommand(LocalMapPut.class);
    registry.registerCommand(LocalMapRm.class);
    registry.registerCommand(BusSend.class);
    registry.registerCommand(BusTail.class);
    registry.registerCommand(VerticleLs.class);
    registry.registerCommand(VerticleDeploy.class);
    registry.registerCommand(VerticleUndeploy.class);
    registry.registerCommand(VerticleFactories.class);

    // Metrics commands
    registry.registerCommand(MetricsLs.class);
    registry.registerCommand(MetricsInfo.class);

    // Register builtin commands so they are listed in help
    registry.registerCommand(CommandBuilder.command("exit").processHandler(process -> {}).build());
    registry.registerCommand(CommandBuilder.command("logout").processHandler(process -> {}).build());
    registry.registerCommand(CommandBuilder.command("jobs").processHandler(process -> {}).build());
    registry.registerCommand(CommandBuilder.command("fg").processHandler(process -> {}).build());
    registry.registerCommand(CommandBuilder.command("bg").processHandler(process -> {
    }).build());

    return new ShellServiceImpl(vertx, options, registry);
  }

  /**
   * @return the command registry for this service
   */
  CommandRegistry getCommandRegistry();

  /**
   * Start the shell service, this is an asynchronous start.
   */
  default void start() {
    start(ar -> {});
  }

  /**
   * Start the shell service, this is an asynchronous start.
   *
   * @param startHandler handler for getting notified when service is started
   */
  void start(Handler<AsyncResult<Void>> startHandler);

  Shell createShell();

  /**
   * Stop the shell service, this is an asynchronous stop.
   */
  default void stop() {
    stop(ar -> {
    });
  }

  /**
   * Stop the shell service, this is an asynchronous start.
   *
   * @param stopHandler handler for getting notified when service is stopped
   */
  void stop(Handler<AsyncResult<Void>> stopHandler);

}
