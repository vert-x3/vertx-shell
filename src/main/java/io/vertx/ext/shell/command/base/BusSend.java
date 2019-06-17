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

import io.vertx.core.MultiMap;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Option;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.shell.command.CommandProcess;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("bus-send")
@Summary("Send a message to the event bus")
public class BusSend extends BusPublish {

  private boolean reply;

  @Option(longName = "timeout")
  @Description("the send timeout")
  public void setTimeout(long timeout) {
    options.setSendTimeout(timeout);
  }

  @Option(longName = "reply", flag = true)
  @Description("wait for a reply and print it on the console")
  public void setReply(boolean reply) {
    this.reply = reply;
  }

  @Override
  public void process(CommandProcess process) {
    Object body = parseBody();
    if (reply) {
      process.vertx().eventBus().request(address, body, options, ar -> {
        if (ar.succeeded()) {
          Message<Object> reply = ar.result();
          if (verbose) {
            process.write("Reply address: " + reply.replyAddress() + "\n");
            MultiMap headers = reply.headers();
            for (String header : headers.names()) {
              process.write("Reply header " + header + ":" + headers.getAll(header) + "\n");
            }
          }
          process.write("Reply: <");
          process.write(String.valueOf(reply.body())).write(">\n");
        } else {
          process.write("Error: " + ar.cause().getMessage() + "\n");
        }
        process.end();
      });
    } else {
      process.vertx().eventBus().send(address, body, options);
      process.end();
    }
  }
}
