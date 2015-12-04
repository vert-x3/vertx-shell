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

import io.termd.core.tty.ReadBuffer;
import io.termd.core.tty.TtyConnection;
import io.termd.core.tty.TtyEvent;
import io.termd.core.util.Helper;
import io.termd.core.util.Vector;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestTtyConnection implements TtyConnection {

  private final ReadBuffer readBuffer = new ReadBuffer(this::execute);
  private Consumer<String> terminalTypeHandler;
  private Consumer<Vector> sizeHandler;
  private BiConsumer<TtyEvent, Integer> eventHandler;
  private Consumer<int[]> stdinHandler;
  private volatile Consumer<Void> closeHandler;
  private final StringBuilder out = new StringBuilder();
  private volatile boolean closed;
  private final CountDownLatch closeLatch = new CountDownLatch(1);
  private volatile long lastAccessedTime;

  @Override
  public Charset inputCharset() {
    return StandardCharsets.UTF_8;
  }

  @Override
  public Charset outputCharset() {
    return StandardCharsets.UTF_8;
  }

  @Override
  public long lastAccessedTime() {
    return lastAccessedTime;
  }

  @Override
  public String terminalType() {
    return "xterm";
  }

  @Override
  public Vector size() {
    return new Vector(40, 20);
  }

  @Override
  public Consumer<String> getTerminalTypeHandler() {
    return terminalTypeHandler;
  }

  @Override
  public void setTerminalTypeHandler(Consumer<String> handler) {
    terminalTypeHandler = handler;
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
    if (!closed) {
      closed = true;
      if (closeHandler != null) {
        closeHandler.accept(null);
      }
      closeLatch.countDown();
    }
  }

  private final ConcurrentLinkedDeque<Runnable> tasks = new ConcurrentLinkedDeque<>();
  private boolean reading = false;

  @Override
  public void execute(Runnable task) {
    if (reading) {
      tasks.add(task);
    } else {
      task.run();
    }
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
    lastAccessedTime = System.currentTimeMillis();
    stdinHandler.accept(Helper.toCodePoints(s));
    reading = false;
    Runnable task;
    while ((task = tasks.poll()) != null) {
      task.run();
    }
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
      System.out.println("report = " + report);
      throw new AssertionError(report);
    }
  }

  public StringBuilder out() {
    return out;
  }

  public boolean isClosed() {
    return closed;
  }

  public CountDownLatch getCloseLatch() {
    return closeLatch;
  }
}
