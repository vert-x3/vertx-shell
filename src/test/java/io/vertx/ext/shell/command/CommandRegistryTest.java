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

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandRegistry;
import io.vertx.ext.shell.command.impl.CommandRegistryImpl;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class CommandRegistryTest {

  Vertx vertx = Vertx.vertx();
  CommandRegistry registry;

  @Before
  public void before(TestContext context) throws Exception {
    vertx = Vertx.vertx();
    registry = CommandRegistry.getShared(vertx);
    long now = System.currentTimeMillis();
    while (vertx.deploymentIDs().size() == 0) {
      context.assertTrue(System.currentTimeMillis() - now < 2000);
      Thread.sleep(1);
    }
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testRegister(TestContext context) {
    CommandRegistry registry = CommandRegistry.getShared(vertx);
    CommandBuilder command = CommandBuilder.command("hello");
    registry.registerCommand(command.build(vertx), context.asyncAssertSuccess(reg -> {
      registry.unregisterCommand("hello", context.asyncAssertSuccess(done -> {
        context.assertEquals(Collections.emptyList(), registry.commands());
      }));
    }));
  }

  @Test
  public void testDuplicateRegistration(TestContext context) {
    CommandRegistry registry = CommandRegistry.getShared(vertx);
    Command a = CommandBuilder.command("a").build(vertx);
    Command b = CommandBuilder.command("b").build(vertx);
    registry.registerCommand(a, context.asyncAssertSuccess(reg -> {
      registry.registerCommands(Arrays.asList(a, b), context.asyncAssertFailure(err -> {
        context.assertEquals(1, registry.commands().size());
        context.assertNotNull(registry.getCommand("a"));
      }));
    }));
  }

  @Test
  public void testCloseRegistryOnVertxClose(TestContext context) {
    Vertx vertx = Vertx.vertx();
    int size = vertx.deploymentIDs().size();
    CommandRegistryImpl registry = (CommandRegistryImpl) CommandRegistry.getShared(vertx);
    while (vertx.deploymentIDs().size() < size + 1) {
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
    context.assertFalse(registry.isClosed());
    vertx.close(context.asyncAssertSuccess(v -> {
      context.assertTrue(registry.isClosed());
    }));
  }

  @Test
  public void testUndeployInVerticleContext(TestContext context) {
    CommandRegistry registry = CommandRegistry.getShared(vertx);
    Async async = context.async();
    AtomicReference<String> ref = new AtomicReference<>();
    vertx.deployVerticle(new AbstractVerticle() {
      @Override
      public void start(Future<Void> startFuture) throws Exception {
        CommandBuilder command = CommandBuilder.command("hello");
        command.processHandler(process -> {
        });
        registry.registerCommand(command.build(vertx), ar -> {
          if (ar.succeeded()) {
            startFuture.complete();
          } else {
            startFuture.fail(ar.cause());
          }
        });
      }
    }, context.asyncAssertSuccess(id -> {
      ref.set(id);
      async.complete();
    }));
    async.awaitSuccess(5000);
    vertx.undeploy(ref.get(), context.asyncAssertSuccess(v -> {
      context.assertEquals(Collections.emptyList(), registry.commands());
    }));
  }

  @Test
  public void testUndeployCommands(TestContext context) throws Exception {
    Async async = context.async();
    Set<String> beforeIds = new HashSet<>(vertx.deploymentIDs());
    registry.registerCommands(
        Arrays.asList(CommandBuilder.command("a").build(vertx), CommandBuilder.command("b").build(vertx)),
        context.asyncAssertSuccess(list -> async.complete()));
    async.awaitSuccess(2000);
    Set<String> afterIds = new HashSet<>(vertx.deploymentIDs());
    context.assertTrue(afterIds.removeAll(beforeIds));
    context.assertEquals(1, afterIds.size());
    String deploymentId = afterIds.iterator().next();
    Async async2 = context.async();
    registry.unregisterCommand("a", context.asyncAssertSuccess(v -> async2.complete()));
    async2.awaitSuccess(2000);
    context.assertTrue(vertx.deploymentIDs().contains(deploymentId));
    Async async3 = context.async();
    registry.unregisterCommand("b", context.asyncAssertSuccess(v -> async3.complete()));
    async3.awaitSuccess(2000);
    context.assertFalse(vertx.deploymentIDs().contains(deploymentId));
  }
}
