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

import io.netty.handler.codec.http.QueryStringDecoder;
import io.termd.core.http.HttpTtyConnection;
import io.termd.core.util.Vector;
import io.vertx.core.Context;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.handler.sockjs.SockJSSocket;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SockJSTtyConnection extends HttpTtyConnection {

  private final Context context;
  private final SockJSSocket socket;

  public SockJSTtyConnection(Charset charset, Context context, SockJSSocket socket) {
    super(charset, getInitialSize(socket));
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
    context.runOnContext(v -> runnable.run());
  }

  @Override
  public void schedule(Runnable runnable, long l, TimeUnit timeUnit) {
    context.owner().setTimer(timeUnit.toMillis(l), id -> runnable.run());
  }

  private static Vector getInitialSize(SockJSSocket socket) {
    QueryStringDecoder decoder = new QueryStringDecoder(socket.uri());
    Map<String, List<String>> params = decoder.parameters();
    try {
      int cols = getParamValue(params, "cols", 80);
      int rows = getParamValue(params, "rows", 24);
      return new Vector(cols, rows);
    } catch (Exception e) {
      return new Vector(80, 24);
    }
  }

  private static int getParamValue(Map<String, List<String>> params, String paramName, int def) throws Exception {
    List<String> param = params.get(paramName);
    if (param != null && param.size() > 0 ) {
      int val = Integer.parseInt(param.get(0));
      if (val > 0) {
        return val;
      } else {
        throw new Exception("Negative size");
      }
    }
    return def;
  }
}
