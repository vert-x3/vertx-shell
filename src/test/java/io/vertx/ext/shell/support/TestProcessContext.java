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
import io.vertx.ext.shell.Session;
import io.vertx.ext.shell.io.EventType;
import io.vertx.ext.shell.io.Stream;
import io.vertx.ext.shell.impl.SessionImpl;
import io.vertx.ext.shell.process.ProcessContext;
import io.vertx.ext.shell.io.Tty;

import java.util.HashMap;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
public class TestProcessContext implements ProcessContext, Tty {

  private final Session session = new SessionImpl();
  private Handler<Integer> endHandler;
  final Context context = Vertx.currentContext();
  final HashMap<EventType, Handler<Void>> eventHandlers = new HashMap<>();
  int width, height;
  private Handler<String> stdin;
  private Stream stdout;

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
    sendEvent(EventType.SIGWINCH);
  }

  @Override
  public TestProcessContext setStdin(Handler<String> stdin) {
    this.stdin = stdin;
    return this;
  }

  @Override
  public Tty setStdin(Stream stdin) {
    return setStdin((Handler<String>)stdin::write);
  }

  @Override
  public Stream stdout() {
    return stdout;
  }

  public TestProcessContext setStdout(Stream stream) {
    stdout = context != null ? Stream.ofObject(txt -> context.runOnContext(v -> stream.write(txt))) : stream;
    return this;
  }

  @Override
  public TestProcessContext eventHandler(EventType eventType, Handler<Void> handler) {
    if (handler != null) {
      eventHandlers.put(eventType, handler);
    } else {
      eventHandlers.remove(eventType);
    }
    return this;
  }

  @Override
  public Session session() {
    return session;
  }

  public boolean sendEvent(EventType eventType) {
    Handler<Void> handler = eventHandlers.get(eventType);
    if (handler != null) {
      handler.handle(null);
      return true;
    } else {
      return false;
    }
  }

  public Handler<String> stdin() {
    return stdin;
  }
}
