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

import io.vertx.core.Vertx;
import io.vertx.core.cli.annotations.Argument;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.ext.shell.command.AnnotatedCommand;
import io.vertx.ext.shell.command.CommandProcess;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("sleep")
@Summary("Suspend execution for an interval of time")
public class Sleep extends AnnotatedCommand {

  private String seconds;

  @Description("the number of seconds to wait")
  @Argument(index = 0, argName = "seconds")
  public void setSeconds(String seconds) {
    this.seconds = seconds;
  }

  @Override
  public void process(CommandProcess process) {
    int timeout = -1;
    try {
      timeout = Integer.parseInt(seconds);
    } catch (NumberFormatException ignore) {
    }
    scheduleSleep(process, timeout * 1000L);
  }

  private void scheduleSleep(CommandProcess process, long millis) {
    Vertx vertx = process.vertx();
    if (millis > 0) {
      long now = System.currentTimeMillis();
      AtomicLong remaining = new AtomicLong(-1);
      long id = process.vertx().setTimer(millis, v -> process.end());
      process.suspendHandler(v -> {
        vertx.cancelTimer(id);
        remaining.set(millis - (System.currentTimeMillis() - now));
      });
      process.resumeHandler(v -> scheduleSleep(process, remaining.get()));
      process.interruptHandler(v -> process.end());
      process.endHandler(v -> vertx.cancelTimer(id));
    } else {
      process.end();
    }
  }
}
