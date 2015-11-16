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

import io.termd.core.tty.TtyConnection;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.term.SockJSTermHandler;
import io.vertx.ext.shell.term.Term;
import io.vertx.ext.web.handler.sockjs.SockJSSocket;

import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SockJSTermHandlerImpl implements SockJSTermHandler {

  final Vertx vertx;
  private Consumer<TtyConnection> handler;

  public SockJSTermHandlerImpl(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  public SockJSTermHandler termHandler(Handler<Term> handler) {
    if (handler != null) {
      this.handler = new TermConnectionHandler(handler);
    } else {
      this.handler = null;
    }
    return this;
  }

  public SockJSTermHandler handler(Consumer<TtyConnection> handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public void handle(SockJSSocket socket) {
    if (handler != null) {
      SockJSTtyConnection conn = new SockJSTtyConnection(vertx.getOrCreateContext(), socket);
      socket.handler(buf -> conn.writeToDecoder(buf.toString()));
      socket.endHandler(v -> {
        Consumer<Void> closeHandler = conn.getCloseHandler();
        if (closeHandler != null) {
          closeHandler.accept(null);
        }
      });
      handler.accept(conn);
    } else {
      socket.close();
    }
  }
}
