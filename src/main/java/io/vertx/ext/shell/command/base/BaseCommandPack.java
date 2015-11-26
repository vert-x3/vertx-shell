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

package io.vertx.ext.shell.command.base;

import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.AnnotatedCommand;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.registry.CommandResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class BaseCommandPack implements CommandResolver {

  /**
   * @return the list of base command classes
   */
  @GenIgnore
  static List<Class<? extends AnnotatedCommand>> baseCommandClasses() {
    List<Class<? extends AnnotatedCommand>> list = new ArrayList<>();
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

  @Override
  public void resolveCommands(Vertx vertx, Handler<AsyncResult<List<Command>>> commandsHandler) {
    commandsHandler.handle(Future.succeededFuture(baseCommandClasses().stream().map(Command::create).collect(Collectors.toList())));
  }
}
