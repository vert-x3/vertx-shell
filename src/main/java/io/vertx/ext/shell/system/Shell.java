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
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.session.Session;

import java.util.List;
import java.util.Set;

/**
 * An interactive session between a consumer and a shell.<p/>
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Shell {

  /**
   * @return the shell session
   */
  @CacheReturn
  Session session();

  /**
   * @return the jobs active in this session
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
   * Create a job, the created job should then be executed with the {@link io.vertx.ext.shell.system.Job#run} method.
   *
   * @param line the command line creating this job
   * @return the created job
   */
  Job createJob(List<CliToken> line);

  /**
   * See {@link #createJob(List)}
   */
  Job createJob(String line);

  /**
   * Close the shell session and terminate all the underlying jobs.
   */
  void close();

  /**
   * Close the shell session and terminate all the underlying jobs.
   */
  void close(Handler<Void> completionHandler);
}
