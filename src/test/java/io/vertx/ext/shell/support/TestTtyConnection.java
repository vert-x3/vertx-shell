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
import io.termd.core.tty.TtyEvent;
import io.termd.core.util.Helper;
import io.termd.core.util.Vector;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestTtyConnection implements TtyConnection {

  private Consumer<String> termHandler;
  private Consumer<Vector> sizeHandler;
  private BiConsumer<TtyEvent, Integer> eventHandler;
  private Consumer<int[]> stdinHandler;
  private Consumer<Void> closeHandler;
  private final StringBuilder out = new StringBuilder();
  private boolean closed;

  @Override
  public String term() {
    return "xterm";
  }

  @Override
  public Vector size() {
    return new Vector(40, 20);
  }

  @Override
  public Consumer<String> getTermHandler() {
    return termHandler;
  }

  @Override
  public void setTermHandler(Consumer<String> handler) {
    termHandler = handler;
  }

  @Override
  public Consumer<Vector> getSizeHandler() {
    return sizeHandler;
  }

  @Override
  public void setSizeHandler(Consumer<Vector> handler) {
    sizeHandler = handler;
  }

  @Override
  public BiConsumer<TtyEvent, Integer> getEventHandler() {
    return eventHandler;
  }

  @Override
  public void setEventHandler(BiConsumer<TtyEvent, Integer> handler) {
    eventHandler = handler;
  }

  @Override
  public Consumer<int[]> getStdinHandler() {
    return stdinHandler;
  }

  @Override
  public void setStdinHandler(Consumer<int[]> consumer) {
    this.stdinHandler = consumer;
  }


  @Override
  public Consumer<int[]> stdoutHandler() {
    return codePoints -> {
      synchronized (TestTtyConnection.this) {
        Helper.appendCodePoints(codePoints, out());
        notify();
      }
    };
  }

  @Override
  public Consumer<Void> getCloseHandler() {
    return closeHandler;
  }

  @Override
  public void setCloseHandler(Consumer<Void> handler) {
    closeHandler = handler;
  }

  @Override
  public void close() {
    closed = true;
  }

  private boolean reading = false;

  @Override
  public void execute(Runnable task) {
    if (reading) {
      throw new AssertionError();
    }
    task.run();
  }

  @Override
  public void schedule(Runnable task, long delay, TimeUnit unit) {
    throw new UnsupportedOperationException();
  }

  public void sendEvent(TtyEvent event) {
    int c;
    switch (event) {
      case INTR:
        c = 'C' - 64;
        break;
      case SUSP:
        c = 'Z' - 64;
        break;
      case EOF:
        c = 'D' - 64;
        break;
      default:
        throw new AssertionError();
    }
    eventHandler.accept(event, c);
  }

  public void read(String s) {
    reading = true;
    stdinHandler.accept(Helper.toCodePoints(s));
    reading = false;
  }

  public synchronized String checkWritten(String s) {
    while (true) {
      int l = Math.min(s.length(), out.length());
      String actual = out.substring(0, l);
      String expected = s.substring(0, l);
      if (actual.equals(expected)) {
        out.replace(0, l, "");
        s = s.substring(l);
      } else {
        return "Was expecting <" + actual + "> to be equals to <" + expected + ">";
      }
      if (s.length() == 0) {
        break;
      } else {
        try {
          wait(5000);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new AssertionError(e);
        }
      }
    }
    return null;
  }

  public synchronized void assertWritten(String s) {
    String report = checkWritten(s);
    if (report != null) {
      throw new AssertionError(report);
    }
  }

  public StringBuilder out() {
    return out;
  }

  public boolean isClosed() {
    return closed;
  }
}
