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

package io.vertx.ext.shell.term.impl;

import io.termd.core.telnet.TelnetHandler;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;

import java.util.function.Supplier;

/**
 * Telnet server integration with Vert.x {@link io.vertx.core.net.NetServer}.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TelnetSocketHandler implements Handler<NetSocket> {

  final Vertx vertx;
  final Supplier<TelnetHandler> factory;

  public TelnetSocketHandler(Vertx vertx, Supplier<TelnetHandler> factory) {
    this.vertx = vertx;
    this.factory = factory;
  }

  @Override
  public void handle(final NetSocket socket) {
    TelnetHandler handler = factory.get();
    final VertxTelnetConnection connection = new VertxTelnetConnection(handler, Vertx.currentContext(), socket);
    socket.handler(event -> connection.receive(event.getBytes()));
    socket.closeHandler(event -> connection.onClose());
    connection.onInit();
  }
}
