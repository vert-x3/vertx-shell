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
import io.termd.core.util.Helper;
import io.vertx.core.Handler;
import io.vertx.ext.shell.io.Stream;
import io.vertx.ext.shell.term.Term;

import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class TermConnectionHandler implements Consumer<TtyConnection> {

  final Handler<Term> handler;

  public TermConnectionHandler(Handler<Term> handler) {
    this.handler = handler;
  }

  @Override
  public void accept(TtyConnection conn) {
    handler.handle(new Term() {

      @Override
      public String type() {
        return conn.terminalType();
      }

      @Override
      public Term setStdin(Stream stdin) {
        if (stdin == null) {
          conn.setStdinHandler(null);
        } else {
          conn.setStdinHandler(keys -> {
            stdin.handle(Helper.fromCodePoints(keys));
          });
        }
        return this;
      }

      @Override
      public int width() {
        return conn.size().x();
      }

      @Override
      public int height() {
        return conn.size().y();
      }

      @Override
      public Stream stdout() {
        return conn::write;
      }

      @Override
      public Term resizehandler(Handler<Void> handler) {
        if (handler != null) {
          conn.setSizeHandler(v -> {
            handler.handle(null);
          });
        } else {
          conn.setSizeHandler(null);
        }
        return null;
      }

      @Override
      public Term closeHandler(Handler<Void> handler) {
        if (handler == null) {
          conn.setCloseHandler(null);
        } else {
          conn.setCloseHandler(handler::handle);
        }
        return this;
      }

      @Override
      public void close() {
        conn.close();
      }
    });
  }
}
