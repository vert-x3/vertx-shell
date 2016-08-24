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

import io.vertx.core.DeploymentOptions;
import io.vertx.core.cli.annotations.Argument;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.shell.command.AnnotatedCommand;
import io.vertx.ext.shell.command.CommandProcess;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("verticle-deploy")
@Summary("Deploy a verticle")
public class VerticleDeploy extends AnnotatedCommand {

  private String name;

  private String options;

  @Argument(index = 0, argName = "name")
  @Description("the verticle name")
  public void setName(String name) {
    this.name = name;
  }

  @Argument(index = 1, argName = "options", required = false)
  @Description("the verticle deployment options as JSON object string")
  public void setOptions(String options) {
    this.options = options;
  }
  @Override
  public void process(CommandProcess process) {
    try {
      DeploymentOptions deploymentOptions;
      if(options != null && !options.isEmpty()) {
        deploymentOptions = new DeploymentOptions(new JsonObject(options));
      } else {
        deploymentOptions = new DeploymentOptions();
      }
      process.vertx().deployVerticle(name, deploymentOptions, ar -> {
        if (ar.succeeded()) {
          process.write("Deployed " + ar.result() + "\n").end();
        } else {
          process.write("Could not deploy " + name + "\n");
          StringWriter buffer = new StringWriter();
          PrintWriter writer = new PrintWriter(buffer);
          ar.cause().printStackTrace(writer);
          process.write(buffer.toString()).end();
        }
      });
    } catch (RuntimeException e) {
      process.write("Invalid JSON object as a deployment options for " + name + "\n");
      StringWriter buffer = new StringWriter();
      PrintWriter writer = new PrintWriter(buffer);
      e.printStackTrace(writer);
      process.write(buffer.toString()).end();
    }
  }
}
