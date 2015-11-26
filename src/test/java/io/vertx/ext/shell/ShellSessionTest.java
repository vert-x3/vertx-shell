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

import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandRegistry;
import io.vertx.ext.shell.support.TestTermServer;
import io.vertx.ext.shell.support.TestTtyConnection;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class ShellSessionTest {

  Vertx vertx;
  CommandRegistry registry;
  TestTermServer termServer;
  ShellServer shellServer;

  @Before
  public void before(TestContext context) {
    vertx = Vertx.vertx();
    registry = CommandRegistry.getShared(vertx);
    termServer = new TestTermServer();
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  private void startShellServer(TestContext context, long sessionTimeout, long reaperInterval) {
    if (shellServer != null) {
      throw new IllegalStateException("Already started");
    }
    Async latch = context.async();
    shellServer = ShellServer.create(vertx, new ShellServerOptions().setSessionTimeout(sessionTimeout).setReaperInterval(reaperInterval)).
        registerTermServer(termServer).
        registerCommandResolver(registry).
        listen(context.asyncAssertSuccess(v -> latch.complete()));
    latch.awaitSuccess(2000);
  }

  @Test
  public void testSessionExpires(TestContext context) throws Exception {
    Async ended = context.async();
    Async registered = context.async();
    registry.registerCommand(CommandBuilder.command("cmd").processHandler(process -> {
      process.endHandler(v -> {
        ended.complete();
      });
    }).build(vertx), context.asyncAssertSuccess(v -> registered.complete()));
    registered.awaitSuccess(2000);
    startShellServer(context, 100, 100);
    long now = System.currentTimeMillis();
    TestTtyConnection conn = termServer.openConnection();
    conn.read("cmd\r");
    ended.awaitSuccess(2000);
    context.assertTrue(conn.isClosed());
    long elapsed = System.currentTimeMillis() - now;
    context.assertTrue(elapsed < 1000);
  }

  @Test
  public void testLastAccessed(TestContext context) throws Exception {
    startShellServer(context, 100, 100);
    TestTtyConnection conn = termServer.openConnection();
    for (int i = 0;i < 100;i++) {
      conn.read("" + i);
      Thread.sleep(10);
      context.assertFalse(conn.isClosed());
    }
    context.assertTrue(conn.getCloseLatch().await(2, TimeUnit.SECONDS));
  }

  @Test
  public void testCloseShellServer(TestContext context) throws Exception {
    testClose(context, conn -> {
      Async async = context.async();
      shellServer.close(context.asyncAssertSuccess(v -> async.complete()));
      async.awaitSuccess(2000);
    });
  }

  @Test
  public void testCloseConnection(TestContext context) throws Exception {
    testClose(context, (conn) -> {
      conn.close();
    });
    shellServer.close();
    shellServer = null;
  }

  public void testClose(TestContext context, Consumer<TestTtyConnection> closer) throws Exception {
    Async processEnded = context.async();
    Async processStarted = context.async();
    Async registered = context.async();
    registry.registerCommand(CommandBuilder.command("cmd").processHandler(process -> {
      process.endHandler(v -> {
        processEnded.complete();
      });
      processStarted.complete();
    }).build(vertx), context.asyncAssertSuccess(v -> registered.complete()));
    registered.awaitSuccess(2000);
    startShellServer(context, 30000, 100);
    TestTtyConnection conn = termServer.openConnection();
    conn.read("cmd\r");
    processStarted.awaitSuccess(2000);
    closer.accept(conn);
    processEnded.awaitSuccess(2000);
    context.assertTrue(conn.getCloseLatch().await(2, TimeUnit.SECONDS));
  }
}
