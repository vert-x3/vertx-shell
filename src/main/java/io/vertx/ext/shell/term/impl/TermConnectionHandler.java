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

import io.termd.core.readline.Keymap;
import io.termd.core.tty.TtyConnection;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.ContextInternal;
import io.vertx.ext.shell.term.Term;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class TermConnectionHandler implements Handler<TtyConnection> {

  final ContextInternal context;
  final Vertx vertx;
  final Handler<Term> handler;
  final Keymap keymap;


  public TermConnectionHandler(Vertx vertx, Keymap keymap, Handler<Term> handler, ContextInternal context) {
    this.vertx = vertx;
    this.handler = handler;
    this.keymap = keymap;
    this.context = context;
  }

  @Override
  public void handle(TtyConnection conn) {
    TermImpl term = new TermImpl(vertx, keymap, conn, context);
    if (context != null) {
      context.dispatch(term, handler);
    } else {
      handler.handle(term);
    }
  }
}
