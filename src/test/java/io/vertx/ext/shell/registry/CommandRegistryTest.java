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

package io.vertx.ext.shell.registry;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.registry.impl.CommandRegistryImpl;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class CommandRegistryTest {

  Vertx vertx = Vertx.vertx();

  @Test
  public void testRegister(TestContext context) {
    CommandRegistry registry = CommandRegistry.get(vertx);
    CommandBuilder command = CommandBuilder.command("hello");
    registry.registerCommand(command.build(), context.asyncAssertSuccess(reg -> {
      registry.unregisterCommand("hello", context.asyncAssertSuccess(done -> {
        context.assertEquals(Collections.emptyList(), registry.registrations());
      }));
    }));
  }

  @Test
  public void testDuplicateRegistration(TestContext context) {
    CommandRegistry registry = CommandRegistry.get(vertx);
    CommandBuilder command = CommandBuilder.command("hello");
    registry.registerCommand(command.build(), context.asyncAssertSuccess(reg -> {
      registry.registerCommand(command.build(), context.asyncAssertFailure(err -> {
      }));
    }));
  }

  @Test
  public void testCloseRegistryOnVertxClose(TestContext context) {
    Vertx vertx = Vertx.vertx();
    int size = vertx.deploymentIDs().size();
    CommandRegistryImpl registry = (CommandRegistryImpl) CommandRegistry.get(vertx);
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
    CommandRegistry registry = CommandRegistry.get(vertx);
    Async async = context.async();
    AtomicReference<String> ref = new AtomicReference<>();
    vertx.deployVerticle(new AbstractVerticle() {
      @Override
      public void start(Future<Void> startFuture) throws Exception {
        CommandBuilder command = CommandBuilder.command("hello");
        command.processHandler(process -> {
        });
        registry.registerCommand(command.build(), ar -> {
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
      context.assertEquals(Collections.emptyList(), registry.registrations());
    }));
  }
}
