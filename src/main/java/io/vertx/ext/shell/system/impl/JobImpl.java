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

import io.vertx.core.Handler;
import io.vertx.ext.shell.session.Session;

import io.vertx.ext.shell.process.*;
import io.vertx.ext.shell.process.Process;
import io.vertx.ext.shell.io.Tty;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.shell.system.JobStatus;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JobImpl implements Job {

  final int id;
  final ShellSessionImpl shell;
  final io.vertx.ext.shell.process.Process process;
  final String line;
  private volatile JobStatus status;
  volatile long lastStopped; // When the job was last stopped
  volatile Tty tty;
  volatile Handler<Void> suspendHandler;
  volatile Handler<Void> interruptHandler;
  volatile Handler<Void> resumeHandler;

  public JobImpl(int id, ShellSessionImpl shell, Process process, String line) {
    this.id = id;
    this.shell = shell;
    this.process = process;
    this.line = line;
  }

  @Override
  public boolean interrupt() {
    if (interruptHandler != null) {
      interruptHandler.handle(null);
    }
    return interruptHandler != null;
  }

  @Override
  public void resume() {
    status = JobStatus.RUNNING;
    if (resumeHandler != null) {
      resumeHandler.handle(null);
    }
  }

  @Override
  public void suspend() {
    status = JobStatus.STOPPED;
    if (suspendHandler != null) {
      suspendHandler.handle(null);
    }
  }

  public long lastStopped() {
    return lastStopped;
  }

  public JobStatus status() {
    return status;
  }

  public String line() {
    return line;
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
  public void run(Handler<Integer> endHandler) {
    status = JobStatus.RUNNING;
    ProcessContext processContext = new ProcessContext() {

      @Override
      public Tty tty() {
        return tty;
      }

      @Override
      public void interruptHandler(Handler<Void> handler) {
        interruptHandler = handler;
      }

      @Override
      public void suspendHandler(Handler<Void> handler) {
        suspendHandler = handler;
      }

      @Override
      public void resumeHandler(Handler<Void> handler) {
        resumeHandler = handler;
      }

      @Override
      public Session session() {
        return shell.session;
      }

      @Override
      public void end(int status) {
        JobImpl.this.status = JobStatus.TERMINATED;
        shell.removeJob(JobImpl.this.id);
        endHandler.handle(status);
      }
    };
    process.execute(processContext);
  }
}
