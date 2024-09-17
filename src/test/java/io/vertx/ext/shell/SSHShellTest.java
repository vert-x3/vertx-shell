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
import io.vertx.ext.shell.term.SSHTermOptions;
import io.vertx.ext.unit.TestContext;
import org.junit.After;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SSHShellTest extends SSHTestBase {

  ShellService service;

  @After
  public void after() throws Exception {
    ShellService s = service;
    if (s != null) {
      service = null;
      try {
        s.stop().await(20, TimeUnit.SECONDS);
      } catch (Throwable ignore) {
      }
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

    service.start().await();
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
