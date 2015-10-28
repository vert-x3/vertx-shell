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

import io.vertx.ext.shell.system.Process;
import io.vertx.ext.shell.io.Tty;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.shell.system.JobStatus;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JobImpl implements Job {

  final int id;
  final ShellImpl shell;
  final Process process;
  final String line;
  private volatile JobStatus status;
  volatile long lastStopped; // When the job was last stopped
  volatile Tty tty;

  public JobImpl(int id, ShellImpl shell, Process process, String line) {
    this.id = id;
    this.shell = shell;
    this.process = process;
    this.line = line;
  }

  @Override
  public boolean interrupt() {
    return process.interrupt();
  }

  @Override
  public void resume() {
    status = JobStatus.RUNNING;
    process.resume();
  }

  @Override
  public void suspend() {
    status = JobStatus.STOPPED;
    process.suspend();
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
    process.setTty(tty);
    process.setSession(shell.session);
    process.execute(status -> {
      JobImpl.this.status = JobStatus.TERMINATED;
      shell.removeJob(JobImpl.this.id);
      endHandler.handle(status);
    });
  }
}
