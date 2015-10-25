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

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.io.Stream;
import io.vertx.ext.shell.system.impl.SessionImpl;
import io.vertx.ext.shell.process.ProcessContext;
import io.vertx.ext.shell.io.Tty;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
public class TestProcessContext implements ProcessContext, Tty {

  private final Session session = new SessionImpl();
  private Handler<Integer> endHandler;
  final Context context = Vertx.currentContext();
  int width, height;
  private Stream stdin;
  private Stream stdout;
  private Handler<Void> resizeHandler;
  private Handler<Void> suspendHandler;

  @Override
  public String type() {
    return null;
  }

  @Override
  public Tty tty() {
    return this;
  }

  @Override
  public void end(int status) {
    if (endHandler != null) {
      endHandler.handle(status);
    }
  }

  public TestProcessContext endHandler(Handler<Integer> handler) {
    endHandler = context != null ? status -> context.runOnContext(v -> handler.handle(status) ) : handler;
    return this;
  }

  @Override
  public int width() {
    return width;
  }

  @Override
  public int height() {
    return height;
  }

  public void setWindowSize(int width, int height) {
    this.width = width;
    this.height = height;
    if (resizeHandler != null) {
      resizeHandler.handle(null);
    }
  }

  @Override
  public Tty resizehandler(Handler<Void> handler) {
    resizeHandler = handler;
    return this;
  }

  @Override
  public Tty setStdin(Stream stdin) {
    this.stdin = stdin;
    return this;
  }

  @Override
  public Stream stdout() {
    return stdout;
  }

  public TestProcessContext setStdout(Stream stream) {
    stdout = context != null ? txt -> context.runOnContext(v -> stream.write(txt)) : stream;
    return this;
  }

  @Override
  public Session session() {
    return session;
  }

  public boolean suspend() {
    if (suspendHandler != null) {
      suspendHandler.handle(null);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public void interruptHandler(Handler<Void> handler) {

  }

  @Override
  public void suspendHandler(Handler<Void> handler) {
    suspendHandler = handler;
  }

  @Override
  public void resumeHandler(Handler<Void> handler) {
  }

  public Stream stdin() {
    return stdin;
  }
}
