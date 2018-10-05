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

import io.netty.util.internal.StringUtil;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.cli.annotations.Argument;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Option;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.ext.shell.command.AnnotatedCommand;
import io.vertx.ext.shell.command.CommandProcess;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("bus-tail")
@Summary("Subscribe to one or several event bus address and logs received messages on the console")
public class BusTail extends AnnotatedCommand {

  private List<String> addresses;
  private boolean verbose;
  private boolean local;

  @Argument(index =  0, argName = "address")
  @Description("the bus address destination")
  public void setAddresses(List<String> addresses) {
    this.addresses = addresses;
  }

  @Option(longName = "verbose", flag = true)
  @Description("verbose output")
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  @Option(longName = "local", flag = true)
  @Description("subscribe to a local address")
  public void setLocal(boolean local) {
    this.local = local;
  }

  @Override
  public void process(CommandProcess process) {
    EventBus eb = process.vertx().eventBus();
    List<MessageConsumer<Object>> consumers = addresses.stream().map(address -> {
      Handler<Message<Object>> handler = msg -> {
        Object body = msg.body();
        String bodyString;
        if (body instanceof Buffer) {
          bodyString = StringUtil.toHexString(((Buffer)body).getBytes());
        } else {
          bodyString = String.valueOf(body);
        }
        if (verbose) {
          process.write(address + ":" + "\n");
          process.write("Reply address: " + msg.replyAddress() + "\n");
          MultiMap headers = msg.headers();
          for (String header : headers.names()) {
            process.write("Header " + header + ":" + headers.getAll(header) + "\n");
          }
          process.write(bodyString + "\n");
        } else {
          process.write(address + ":" + bodyString + "\n");
        }
      };
      return local ? eb.localConsumer(address, handler) : eb.consumer(address, handler);
    }).collect(Collectors.toList());
    process.interruptHandler(done -> {
      process.end();
    });
    process.endHandler(done -> {
      consumers.forEach(MessageConsumer::unregister);
    });
  }
}
