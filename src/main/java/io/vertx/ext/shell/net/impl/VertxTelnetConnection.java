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

package io.vertx.ext.shell.net.impl;

import io.termd.core.telnet.TelnetConnection;
import io.termd.core.telnet.TelnetHandler;
import io.vertx.core.Context;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxTelnetConnection extends TelnetConnection {

  final NetSocket socket;
  final Context context;
  private Buffer pending;

  public VertxTelnetConnection(TelnetHandler handler, Context context, NetSocket socket) {
    super(handler);
    this.context = context;
    this.socket = socket;
  }

  @Override
  protected void execute(Runnable task) {
    context.runOnContext(event -> task.run());
  }

  @Override
  protected void schedule(Runnable task, long delay, TimeUnit unit) {
    long millis = unit.toMillis(delay);
    context.owner().setTimer(millis, event -> task.run());
  }

  // Not properly synchronized, but ok for now
  @Override
  protected void send(byte[] data) {
    if (pending == null) {
      pending = Buffer.buffer();
      pending.appendBytes(data);
      context.runOnContext(event -> {
        Buffer buf = pending;
        pending = null;
        socket.write(buf);
      });
    } else {
      pending.appendBytes(data);
    }
  }

  @Override
  public void onClose() {
    super.onClose();
  }

  @Override
  public void close() {
    // Check there is no
    socket.close();
  }
}
