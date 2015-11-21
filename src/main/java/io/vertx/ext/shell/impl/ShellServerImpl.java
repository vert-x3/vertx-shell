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

package io.vertx.ext.shell.impl;

import io.termd.core.tty.TtyConnection;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.ShellServer;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.registry.CommandRegistry;
import io.vertx.ext.shell.system.Shell;
import io.vertx.ext.shell.system.impl.ShellImpl;
import io.vertx.ext.shell.term.TermServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ShellServerImpl implements ShellServer {

  private final Vertx vertx;
  private final CommandRegistry registry;
  private final List<TermServer> termServers;
  private String welcomeMessage;

  public ShellServerImpl(Vertx vertx) {
    this.vertx = vertx;
    this.registry = CommandRegistry.get(vertx);
    this.termServers = new ArrayList<>();

    // Register builtin commands so they are listed in help
    registry.registerCommand(CommandBuilder.command("exit").processHandler(process -> {}).build());
    registry.registerCommand(CommandBuilder.command("logout").processHandler(process -> {}).build());
    registry.registerCommand(CommandBuilder.command("jobs").processHandler(process -> {}).build());
    registry.registerCommand(CommandBuilder.command("fg").processHandler(process -> {}).build());
    registry.registerCommand(CommandBuilder.command("bg").processHandler(process -> {}).build());
  }

  @Override
  public CommandRegistry commandRegistry() {
    return registry;
  }

  @Override
  public ShellServer registerTermServer(TermServer termServer) {
    termServers.add(termServer);
    return this;
  }

  @Override
  public ShellServer welcomeMessage(String msg) {
    welcomeMessage = msg;
    return this;
  }

  @Override
  public void listen(Handler<AsyncResult<Void>> listenHandler) {
    Handler<TtyConnection> boostrapHandler = conn -> {
      Shell shell = createShell();
      TtyAdapter adapter = new TtyAdapter(vertx, conn, shell, registry);
      adapter.setWelcome(welcomeMessage);
      adapter.init();
    };
    AtomicInteger count = new AtomicInteger(termServers.size());
    if (count.get() == 0) {
      listenHandler.handle(Future.succeededFuture());
      return;
    }
    Handler<AsyncResult<TermServer>> handler = ar -> {
      if (ar.succeeded()) {
        if (count.decrementAndGet() == 0) {
          listenHandler.handle(Future.succeededFuture());
        }
      } else {
        count.set(0);
        listenHandler.handle(Future.failedFuture(ar.cause()));
      }
    };
    termServers.forEach(termServer -> {
      termServer.connectionHandler(boostrapHandler);
      termServer.listen(handler);
    });
  }

  @Override
  public Shell createShell() {
    return new ShellImpl(registry);
  }

  @Override
  public void close(Handler<AsyncResult<Void>> completionHandler) {
    AtomicInteger count = new AtomicInteger(termServers.size());
    if (count.get() == 0) {
      completionHandler.handle(Future.succeededFuture());
      return;
    }
    Handler<AsyncResult<Void>> handler = ar -> {
      if (ar.succeeded()) {
        if (count.decrementAndGet() == 0) {
          completionHandler.handle(Future.succeededFuture());
        }
      } else {
        count.set(0);
        completionHandler.handle(Future.failedFuture(ar.cause()));
      }
    };
    termServers.forEach(termServer ->  {
      termServer.close(handler);
    });
  }
}
