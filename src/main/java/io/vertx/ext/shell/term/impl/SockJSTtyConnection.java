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

import io.termd.core.http.HttpTtyConnection;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.shell.term.Term;
import io.vertx.ext.web.handler.sockjs.SockJSSocket;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SockJSTtyConnection extends HttpTtyConnection {

  private final Context context;
  private final SockJSSocket socket;

  public SockJSTtyConnection(Context context, SockJSSocket socket) {
    this.context = context;
    this.socket = socket;
  }

  @Override
  protected void write(byte[] bytes) {
    socket.write(Buffer.buffer(bytes));
  }

  @Override
  public void close() {
    socket.close();
  }

  @Override
  public void execute(Runnable runnable) {
    context.runOnContext(v -> {
      runnable.run();
    });
  }

  @Override
  public void schedule(Runnable runnable, long l, TimeUnit timeUnit) {
    context.owner().setTimer(timeUnit.toMillis(l), id -> {
      runnable.run();
    });
  }
}
