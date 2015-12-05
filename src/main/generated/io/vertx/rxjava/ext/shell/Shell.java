/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.rxjava.ext.shell;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import java.util.List;
import io.vertx.rxjava.ext.shell.system.Job;
import io.vertx.rxjava.ext.shell.system.JobController;
import io.vertx.rxjava.ext.shell.cli.CliToken;
import io.vertx.rxjava.ext.shell.session.Session;

/**
 * An interactive session between a consumer and a shell.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.Shell original} non RX-ified interface using Vert.x codegen.
 */

public class Shell {

  final io.vertx.ext.shell.Shell delegate;

  public Shell(io.vertx.ext.shell.Shell delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Create a job, the created job should then be executed with the {@link io.vertx.rxjava.ext.shell.system.Job#run} method.
   * @param line the command line creating this job
   * @return the created job
   */
  public Job createJob(List<CliToken> line) { 
    Job ret= Job.newInstance(this.delegate.createJob(line.stream().map(element -> (io.vertx.ext.shell.cli.CliToken)element.getDelegate()).collect(java.util.stream.Collectors.toList())));
    return ret;
  }

  /**
   * See {@link io.vertx.rxjava.ext.shell.Shell#createJob}
   * @param line 
   * @return 
   */
  public Job createJob(String line) { 
    Job ret= Job.newInstance(this.delegate.createJob(line));
    return ret;
  }

  /**
   * @return the shell's job controller
   * @return 
   */
  public JobController jobController() { 
    if (cached_0 != null) {
      return cached_0;
    }
    JobController ret= JobController.newInstance(this.delegate.jobController());
    cached_0 = ret;
    return ret;
  }

  /**
   * @return the current shell session
   * @return 
   */
  public Session session() { 
    if (cached_1 != null) {
      return cached_1;
    }
    Session ret= Session.newInstance(this.delegate.session());
    cached_1 = ret;
    return ret;
  }

  /**
   * Close the shell.
   */
  public void close() { 
    this.delegate.close();
  }

  private JobController cached_0;
  private Session cached_1;

  public static Shell newInstance(io.vertx.ext.shell.Shell arg) {
    return arg != null ? new Shell(arg) : null;
  }
}
