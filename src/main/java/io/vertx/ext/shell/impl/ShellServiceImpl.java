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
import io.vertx.ext.shell.term.SSHTermOptions;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.term.TelnetTermOptions;
import io.vertx.ext.shell.term.TermServer;
import io.vertx.ext.shell.term.WebTermOptions;
import io.vertx.ext.shell.term.impl.SSHTermServer;
import io.vertx.ext.shell.term.impl.TelnetTermServer;
import io.vertx.ext.shell.registry.CommandRegistry;
import io.vertx.ext.shell.system.Shell;
import io.vertx.ext.shell.system.impl.ShellImpl;
import io.vertx.ext.shell.term.impl.WebTermServer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ShellServiceImpl implements ShellService {

  private final Vertx vertx;
  private final ShellServiceOptions options;
  private final CommandRegistry registry;
  private TelnetTermServer telnet;
  private SSHTermServer ssh;
  private WebTermServer webServer;

  public ShellServiceImpl(Vertx vertx, ShellServiceOptions options, CommandRegistry registry) {
    this.vertx = vertx;
    this.options = options;
    this.registry = registry;
  }

  @Override
  public CommandRegistry getCommandRegistry() {
    return registry;
  }

  @Override
  public void start(Handler<AsyncResult<Void>> startHandler) {

    Handler<TtyConnection> shellBoostrap = conn -> {
      Shell shell = createShell();
      TtyAdapter adapter = new TtyAdapter(vertx, conn, shell, registry);
      adapter.setWelcome(options.getWelcomeMessage());
      adapter.init();
    };

    TelnetTermOptions telnetOptions = options.getTelnetOptions();
    SSHTermOptions sshOptions = options.getSSHOptions();
    WebTermOptions webOptions = options.getWebOptions();
    AtomicInteger count = new AtomicInteger();
    count.addAndGet(telnetOptions != null ? 1 : 0);
    count.addAndGet(sshOptions != null ? 1 : 0);
    count.addAndGet(webOptions != null ? 1 : 0);
    if (count.get() == 0) {
      startHandler.handle(Future.succeededFuture());
      return;
    }
    Handler<AsyncResult<TermServer>> listenHandler = ar -> {
      if (ar.succeeded()) {
        if (count.decrementAndGet() == 0) {
          startHandler.handle(Future.succeededFuture());
        }
      } else {
        count.set(0);
        startHandler.handle(Future.failedFuture(ar.cause()));
      }
    };
    if (telnetOptions != null) {
      telnet = new TelnetTermServer(vertx, telnetOptions);
      telnet.connectionHandler(shellBoostrap);
      telnet.listen(listenHandler);
    }
    if (sshOptions != null) {
      ssh = new SSHTermServer(vertx, sshOptions);
      ssh.connectionHandler(shellBoostrap);
      ssh.listen(ar -> {
        if (ar.succeeded()) {
          listenHandler.handle(Future.succeededFuture());
        } else {
          listenHandler.handle(Future.failedFuture(ar.cause()));
        }
      });
    }
    if (webOptions != null) {
      webServer = new WebTermServer(vertx, webOptions);
      webServer.connectionHandler(shellBoostrap);
      webServer.listen(ar -> {
        if (ar.succeeded()) {
          listenHandler.handle(Future.succeededFuture());
        } else {
          listenHandler.handle(Future.failedFuture(ar.cause()));
        }
      });
    }
  }

  @Override
  public Shell createShell() {
    return new ShellImpl(registry);
  }

  @Override
  public void stop(Handler<AsyncResult<Void>> stopHandler) {
    AtomicInteger count = new AtomicInteger();
    count.addAndGet(telnet != null ? 1 : 0);
    count.addAndGet(ssh != null ? 1 : 0);
    if (count.get() == 0) {
      stopHandler.handle(Future.succeededFuture());
      return;
    }
    Handler<AsyncResult<Void>> listenHandler = ar -> {
      if (ar.succeeded()) {
        if (count.decrementAndGet() == 0) {
          stopHandler.handle(Future.succeededFuture());
        }
      } else {
        count.set(0);
        stopHandler.handle(Future.failedFuture(ar.cause()));
      }
    };
    if (telnet != null) {
      telnet.close(listenHandler);
    }
    if (ssh != null) {
      ssh.close(listenHandler);
    }
  }

}
