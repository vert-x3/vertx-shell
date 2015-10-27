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

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.shell.io.Tty;

/**
 * A job executed in a {@link io.vertx.ext.shell.system.ShellSession}, grouping one or several process.<p/>
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
   * @return the job status
   */
  JobStatus status();

  /**
   * @return when the job was last stopped
   */
  long lastStopped();

  /**
   * @return the execution line of the job, i.e the shell command line that launched this job
   */
  String line();

  /**
   * @return the current tty this job uses
   */
  Tty getTty();

  /**
   * Set a tty on the job.
   *
   * @param tty the tty to use
   */
  void setTty(Tty tty);

  /**
   * Run the job, before running the job a {@link io.vertx.ext.shell.io.Tty} must be set.
   *
   * @param endHandler to be notified when the job terminates
   */
  void run(Handler<Integer> endHandler);

  /**
   * Attempt to interrupt the job.
   *
   * @return true if the job is actually interrupted
   */
  boolean interrupt();

  /**
   * Suspend the job.
   */
  void resume();

  /**
   * Resume the job.
   */
  void suspend();

}
