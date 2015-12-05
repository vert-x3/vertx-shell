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

package io.vertx.ext.shell.system.impl;

import io.vertx.core.Future;
import io.vertx.core.Handler;

import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.system.Process;
import io.vertx.ext.shell.system.ProcessStatus;
import io.vertx.ext.shell.term.Tty;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.shell.system.ExecStatus;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JobImpl implements Job {

  final int id;
  final ShellImpl shell;
  final Process process;
  final String line;
  private volatile ExecStatus actualStatus; // Used internally for testing only
  volatile long lastStopped; // When the job was last stopped
  volatile Tty tty;
  volatile Session session;
  volatile Handler<ProcessStatus> statusUpdateHandler;
  final Future<Void> terminateFuture;

  JobImpl(int id, ShellImpl shell, Process process, String line) {
    this.id = id;
    this.shell = shell;
    this.process = process;
    this.line = line;
    this.terminateFuture = Future.future();

    process.statusUpdateHandler(status -> {
      if (status.getExecStatus() == ExecStatus.TERMINATED) {
        shell.removeJob(JobImpl.this.id);
      }
      if (statusUpdateHandler != null) {
        statusUpdateHandler.handle(status);
      }
      if (status.getExecStatus() == ExecStatus.TERMINATED) {
        terminateFuture.complete();
      }
    });
  }

  @Override
  public Session getSession() {
    return session;
  }

  @Override
  public Job setSession(Session session) {
    this.session = session;
    return this;
  }

  public ExecStatus actualStatus() {
    return actualStatus;
  }

  @Override
  public Job statusUpdateHandler(Handler<ProcessStatus> handler) {
    statusUpdateHandler = handler;
    return this;
  }

  @Override
  public boolean interrupt() {
    return process.interrupt();
  }

  @Override
  public void resume(boolean foreground) {
    try {
      process.resume(foreground, v -> {
        actualStatus = ExecStatus.RUNNING;
      });
    } catch (IllegalStateException ignore) {
    }
  }

  @Override
  public void suspend() {
    try {
      process.suspend(v -> {
        actualStatus = ExecStatus.STOPPED;
      });
    } catch (IllegalStateException ignore) {
    }
  }

  @Override
  public void terminate() {
    try {
      process.terminate();
    } catch (IllegalStateException ignore) {
      // Process already terminated, likely by itself
    }
  }

  public long lastStopped() {
    return lastStopped;
  }

  public ExecStatus status() {
    return process.status();
  }

  public String line() {
    return line;
  }

  @Override
  public void toBackground() {
    process.toBackground();
  }

  @Override
  public void toForeground() {
    process.toForeground();
  }

  @Override
  public int id() {
    return id;
  }

  @Override
  public Tty getTty() {
    return tty;
  }

  @Override
  public Job setTty(Tty tty) {
    this.tty = tty;
    return this;
  }

  @Override
  public void run() {
    process.setTty(tty);
    process.setSession(session);
    process.run(v -> {
      actualStatus = ExecStatus.RUNNING;
    });
  }
}
