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
import io.vertx.ext.shell.system.impl.JobControllerImpl;

import java.util.Set;

/**
 * The job controller.<p/>
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface JobController {

  static JobController create() {
    return new JobControllerImpl();
  }

  /**
   * @return the current foreground job
   */
  Job foregroundJob();

  /**
   * @return the active jobs
   */
  Set<Job> jobs();

  /**
   * Returns an active job in this session by its {@literal id}.
   *
   * @param id the job id
   * @return the job of {@literal null} when not found
   */
  Job getJob(int id);

  /**
   * Create a job wrapping a process.
   *
   * @param process the process
   * @param line the line
   * @return the created job
   */
  Job createJob(Process process, String line);

  /**
   * Close the controller and terminate all the underlying jobs, a closed controller does not accept anymore jobs.
   */
  void close(Handler<Void> completionHandler);

  /**
   * Close the shell session and terminate all the underlying jobs.
   */
  void close();

}
