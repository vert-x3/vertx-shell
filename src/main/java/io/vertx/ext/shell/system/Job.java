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

package io.vertx.ext.shell.system;

import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.term.Tty;

import java.util.List;

/**
 * A job executed in a {@link JobController}, grouping one or several process.<p/>
 *
 * The job life cycle can be controlled with the {@link #run}, {@link #resume} and {@link #suspend} and {@link #interrupt}
 * methods.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Job {

  /**
   * @return the job id
   */
  int id();

  /**
   * @return the job exec status
   */
  ExecStatus status();

  /**
   * @return when the job was last stopped
   */
  long lastStopped();

  /**
   * @return the execution line of the job, i.e the shell command line that launched this job
   */
  String line();

  /**
   * Set a tty on the job.
   *
   * @param tty the tty to use
   * @return this object
   */
  @Fluent
  Job setTty(Tty tty);

  /**
   * Set a session on the job.
   *
   * @param session the session to use
   * @return this object
   */
  @Fluent
  Job setSession(Session session);

  /**
   * Set an handler called when the job terminates.
   *
   * @param handler the terminate handler
   * @return this object
   */
  @Fluent
  Job statusUpdateHandler(Handler<ExecStatus> handler);

  /**
   * Run the job, before running the job a {@link Tty} must be set.
   *
   * @return this object
   */
  @Fluent
  Job run();

  /**
   * Attempt to interrupt the job.
   *
   * @return true if the job is actually interrupted
   */
  boolean interrupt();

  /**
   * Resume the job to foreground.
   */
  default Job resume() {
    return resume(true);
  }

  /**
   * Send the job to background.
   *
   * @return this object
   */
  @Fluent
  Job toBackground();

  /**
   * Send the job to foreground.
   *
   * @return this object
   */
  @Fluent
  Job toForeground();

  /**
   * Resume the job.
   *
   * @param foreground true when the job is resumed in foreground
   */
  @Fluent
  Job resume(boolean foreground);

  /**
   * Resume the job.
   *
   * @return this object
   */
  @Fluent
  Job suspend();

  /**
   * Terminate the job.
   */
  void terminate();

  /**
   * @return the first process in the job
   */
  @CacheReturn
  Process process();
}
