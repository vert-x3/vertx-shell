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

import io.termd.core.telnet.TelnetTtyConnection;
import io.termd.core.tty.TtyConnection;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.ext.shell.term.TelnetTermOptions;
import io.vertx.ext.shell.term.TermServer;
import io.vertx.ext.shell.term.Term;

/**
 * Encapsulate the Telnet server setup.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TelnetTermServer implements TermServer {

  private final Vertx vertx;
  private final TelnetTermOptions options;
  private Handler<TtyConnection> handler;
  private NetServer server;

  public TelnetTermServer(Vertx vertx, TelnetTermOptions options) {
    this.vertx = vertx;
    this.options = options;
  }

  @Override
  public TermServer connectionHandler(Handler<TtyConnection> handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public TermServer termHandler(Handler<Term> handler) {
    if (handler != null) {
      connectionHandler(new TermConnectionHandler(handler));
    } else {
      connectionHandler(null);
    }
    return this;
  }

  @Override
  public TermServer listen(Handler<AsyncResult<TermServer>> listenHandler) {
    if (server == null) {
      server = vertx.createNetServer(options);
      server.connectHandler(new TelnetSocketHandler(vertx, () -> {
        return new TelnetTtyConnection(true, true, handler::handle);
      }));
      server.listen(ar -> {
        if (ar.succeeded()) {
          listenHandler.handle(Future.succeededFuture(this));
        } else {
          listenHandler.handle(Future.failedFuture(ar.cause()));
        }
      });
    } else {
      listenHandler.handle(Future.failedFuture("Already started"));
    }
    return this;
  }

  @Override
  public void close() {
    close(null);
  }

  public void close(Handler<AsyncResult<Void>> completionHandler) {
    if (server != null) {
      server.close(completionHandler);
    } else {
      completionHandler.handle(Future.failedFuture("No started"));
    }
  }

  public int actualPort() {
    return server.actualPort();
  }
}
