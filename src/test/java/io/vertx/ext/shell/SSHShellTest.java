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

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandRegistry;
import io.vertx.ext.shell.term.SSHTermOptions;
import io.vertx.ext.unit.TestContext;
import org.junit.After;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SSHShellTest extends SSHTestBase {

  ShellService service;

  @After
  public void after() throws Exception {
    if (service != null) {
      CountDownLatch latch = new CountDownLatch(1);
      Handler<AsyncResult<Void>> handler = ar -> latch.countDown();
      service.stop()
        .onComplete(handler);
      assertTrue(latch.await(10, TimeUnit.SECONDS));
    }
    super.after();
  }

  protected void startShell(SSHTermOptions options) throws ExecutionException, InterruptedException, TimeoutException {

    if (service != null) {
      throw new IllegalStateException();
    }

    service = ShellService.create(vertx, new ShellServiceOptions().
        setWelcomeMessage("").
        setSSHOptions(options));

    CompletableFuture<Void> fut = new CompletableFuture<>();
    service.start()
      .onComplete(ar -> {
      if (ar.succeeded()) {
        fut.complete(null);
      } else {
        fut.completeExceptionally(ar.cause());
      }
    });
    fut.get(10, TimeUnit.SECONDS);
  }

  protected static JsonObject config() {
    return new JsonObject()
        .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
        .put("driver_class", "org.hsqldb.jdbcDriver");
  }

  @Override
  public void testExec(TestContext context) throws Exception {
    AtomicReference<Vertx> execCommand = new AtomicReference<>();
    CommandRegistry registry = CommandRegistry.getShared(vertx);
    registry.registerCommand(CommandBuilder.command("the-command").processHandler(process -> {
      context.assertNotNull(Vertx.currentContext());
      context.assertNotNull(process.session());
      execCommand.set(process.vertx());
      context.assertEquals(-1, process.width());
      context.assertEquals(-1, process.height());
      context.assertEquals(Arrays.asList("arg1", "arg2"), process.args());
      context.assertTrue(process.isForeground());
      StringBuilder input = new StringBuilder();
      process.stdinHandler(data -> {
        input.append(data);
        if (input.toString().equals("the_input")) {
          process.end(2);
        }
      });
      process.write("the_output");
    }).build(vertx));
    super.testExec(context);
    assertEquals(execCommand.get(), vertx);
  }
}
