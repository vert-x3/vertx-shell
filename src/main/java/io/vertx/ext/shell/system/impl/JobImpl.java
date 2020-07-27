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

import io.vertx.core.Promise;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.system.Process;
import io.vertx.ext.shell.term.Tty;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.shell.system.ExecStatus;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class JobImpl implements Job {

  final int id;
  final JobControllerImpl controller;
  final Process process;
  final String line;
  private volatile ExecStatus actualStatus; // Used internally for testing only
  volatile long lastStopped; // When the job was last stopped
  volatile Tty tty;
  volatile Session session;
  volatile Handler<ExecStatus> statusUpdateHandler;
  final Promise<Void> terminatePromise;

  JobImpl(int id, JobControllerImpl controller, Process process, String line) {
    this.id = id;
    this.controller = controller;
    this.process = process;
    this.line = line;
    this.terminatePromise = Promise.promise();

    process.terminatedHandler(exitCode -> {
      if (controller.foregroundJob == this) {
        controller.foregroundJob = null;
        if (controller.foregroundUpdatedHandler != null) {
          controller.foregroundUpdatedHandler.handle(null);
        }
      }
      controller.removeJob(JobImpl.this.id);
      if (statusUpdateHandler != null) {
        statusUpdateHandler.handle(ExecStatus.TERMINATED);
      }
      terminatePromise.complete();
    });
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
  public Job statusUpdateHandler(Handler<ExecStatus> handler) {
    statusUpdateHandler = handler;
    return this;
  }

  @Override
  public boolean interrupt() {
    return process.interrupt();
  }

  @Override
  public Job resume(boolean foreground) {
    if (controller.foregroundJob != null) {
      throw new IllegalStateException();
    }
    try {
      process.resume(foreground, v -> actualStatus = ExecStatus.RUNNING);
    } catch (IllegalStateException ignore) {
    }
    if (foreground) {
      controller.foregroundJob = this;
      if (controller.foregroundUpdatedHandler != null) {
        controller.foregroundUpdatedHandler.handle(this);
      }
    }
    if (statusUpdateHandler != null) {
      statusUpdateHandler.handle(process.status());
    }
    return this;
  }

  @Override
  public Job suspend() {
    try {
      process.suspend(v -> actualStatus = ExecStatus.STOPPED);
    } catch (IllegalStateException ignore) {
      return this;
    }
    if (controller.foregroundJob == this) {
      controller.foregroundJob = null;
      if (controller.foregroundUpdatedHandler != null) {
        controller.foregroundUpdatedHandler.handle(null);
      }
    }
    if (statusUpdateHandler != null) {
      statusUpdateHandler.handle(process.status());
    }
    return this;
  }

  @Override
  public void terminate() {
    try {
      process.terminate();
    } catch (IllegalStateException ignore) {
      // Process already terminated, likely by itself
    }
  }

  @Override
  public Process process() {
    return process;
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
  public Job toBackground() {
    if (controller.foregroundJob == this) {
      controller.foregroundJob = null;
      process.toBackground();
      if (statusUpdateHandler != null) {
        statusUpdateHandler.handle(process.status());
      }
    }
    return this;
  }

  @Override
  public Job toForeground() {
    if (controller.foregroundJob != null) {
      throw new IllegalStateException();
    }
    controller.foregroundJob = this;
    if (controller.foregroundUpdatedHandler != null) {
      controller.foregroundUpdatedHandler.handle(this);
    }
    process.toForeground();
    if (statusUpdateHandler != null) {
      statusUpdateHandler.handle(process.status());
    }
    return this;
  }

  @Override
  public int id() {
    return id;
  }

  @Override
  public Job setTty(Tty tty) {
    this.tty = tty;
    return this;
  }

  @Override
  public Job run() {
    controller.foregroundJob = this;
    if (controller.foregroundUpdatedHandler != null) {
      controller.foregroundUpdatedHandler.handle(this);
    }
    actualStatus = ExecStatus.RUNNING;
    if (statusUpdateHandler != null) {
      statusUpdateHandler.handle(ExecStatus.RUNNING);
    }
    process.setTty(tty);
    process.setSession(session);
    process.run();
    return this;
  }
}
