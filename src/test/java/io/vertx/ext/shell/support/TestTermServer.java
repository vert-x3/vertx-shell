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

package io.vertx.ext.shell.support;

import io.termd.core.tty.TtyConnection;
import io.vertx.core.*;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.shell.term.Term;
import io.vertx.ext.shell.term.TermServer;
import io.vertx.ext.shell.term.impl.TermImpl;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestTermServer implements TermServer {

  private final Vertx vertx;
  private Handler<TtyConnection> connectionHandler;

  public TestTermServer(Vertx vertx) {
    this.vertx = vertx;
  }

  public TestTtyConnection openConnection() {
    TestTtyConnection conn = new TestTtyConnection(vertx);
    connectionHandler.handle(conn);
    return conn;
  }

  @Override
  public TermServer termHandler(Handler<Term> handler) {
    connectionHandler = conn -> {
      handler.handle(new TermImpl(vertx, conn));
    };
    return this;
  }

  @Override
  public TermServer authenticationProvider(AuthenticationProvider provider) {
    throw new UnsupportedOperationException();
  }

  public TermServer listen(Completable<Void> listenHandler) {
    listenHandler.succeed();
    return this;
  }

  @Override
  public int actualPort() {
    throw new UnsupportedOperationException();
  }

  public void close(Completable<Void> completionHandler) {
    completionHandler.succeed();
  }

  @Override
  public Future<Void> listen() {
    return Future.future(this::listen);
  }

  @Override
  public Future<Void> close() {
    return Future.future(this::close);
  }
}
