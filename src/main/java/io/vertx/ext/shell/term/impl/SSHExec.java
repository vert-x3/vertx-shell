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
import io.vertx.ext.shell.term.Tty;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SSHExec implements Tty {

  private final String command;
  private final TtyConnection conn;

  SSHExec(String command, TtyConnection conn) {
    this.command = command;
    this.conn = conn;
  }

  public String command() {
    return command;
  }

  public void end(int exit) {
    conn.close(exit);
  }

  @Override
  public String type() {
    return conn.terminalType();
  }

  @Override
  public int width() {
    return conn.size() != null ? conn.size().x() : -1;
  }

  @Override
  public int height() {
    return conn.size() != null ? conn.size().y() : -1;
  }

  @Override
  public Tty stdinHandler(Handler<String> handler) {
    if (handler != null) {
      conn.setStdinHandler(codePoints -> handler.handle(io.termd.core.util.Helper.fromCodePoints(codePoints)));
    } else {
      conn.setStdinHandler(null);
    }
    return this;
  }

  @Override
  public Tty write(String data) {
    conn.write(data);
    return this;
  }

  @Override
  public Tty resizehandler(Handler<Void> handler) {
    if (handler != null) {
      conn.setSizeHandler(resize -> handler.handle(null));
    } else {
      conn.setSizeHandler(null);
    }
    return this;
  }
}
