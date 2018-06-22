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

package io.vertx.ext.shell;

import io.vertx.codegen.annotations.CacheReturn;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.shell.system.JobController;

import java.util.List;
import java.util.function.Function;

/**
 * An interactive session between a consumer and a shell.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Shell {

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
   * @return the shell's job controller
   */
  @CacheReturn
  JobController jobController();

  /**
   * @return the current shell session
   */
  @CacheReturn
  Session session();


  /**
   * Set a new prompt in this session.
   *
   * @param prompt  the new prompt will be calculated when it's needed.
   */
  void setPrompt(Function<Session, String> prompt);


  /**
   * Close the shell.
   */
  void close();

}
