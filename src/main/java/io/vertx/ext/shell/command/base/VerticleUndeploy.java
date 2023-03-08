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

import io.vertx.core.cli.annotations.Argument;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.ext.shell.command.AnnotatedCommand;
import io.vertx.ext.shell.command.CommandProcess;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("verticle-undeploy")
@Summary("Undeploy a verticle")
public class VerticleUndeploy extends AnnotatedCommand {

  private String id;

  @Argument(index = 0, argName = "id")
  @Description("the verticle id")
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public void process(CommandProcess process) {
    process.vertx().undeploy(id)
      .onComplete(ar -> {
        if (ar.succeeded()) {
          process.write("Undeployed " + id + "\n").end();
        } else {
          process.write("Could not undeploy " + id + "\n");
          StringWriter buffer = new StringWriter();
          PrintWriter writer = new PrintWriter(buffer);
          ar.cause().printStackTrace(writer);
          process.write(buffer.toString()).end();
        }
      });
  }
}
