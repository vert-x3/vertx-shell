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

package io.vertx.rxjava.ext.shell.system;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import java.util.Set;
import io.vertx.core.Handler;

/**
 * The job controller.<p/>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.system.JobController original} non RX-ified interface using Vert.x codegen.
 */

public class JobController {

  final io.vertx.ext.shell.system.JobController delegate;

  public JobController(io.vertx.ext.shell.system.JobController delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static JobController create() { 
    JobController ret= JobController.newInstance(io.vertx.ext.shell.system.JobController.create());
    return ret;
  }

  /**
   * @return the current foreground job
   * @return 
   */
  public Job foregroundJob() { 
    Job ret= Job.newInstance(this.delegate.foregroundJob());
    return ret;
  }

  /**
   * @return the active jobs
   * @return 
   */
  public Set<Job> jobs() { 
    Set<Job> ret = this.delegate.jobs().stream().map(Job::newInstance).collect(java.util.stream.Collectors.toSet());
    return ret;
  }

  /**
   * Returns an active job in this session by its .
   * @param id the job id
   * @return the job of  when not found
   */
  public Job getJob(int id) { 
    Job ret= Job.newInstance(this.delegate.getJob(id));
    return ret;
  }

  /**
   * Create a job wrapping a process.
   * @param process the process
   * @param line the line
   * @return the created job
   */
  public Job createJob(Process process, String line) { 
    Job ret= Job.newInstance(this.delegate.createJob((io.vertx.ext.shell.system.Process) process.getDelegate(), line));
    return ret;
  }

  /**
   * Close the controller and terminate all the underlying jobs, a closed controller does not accept anymore jobs.
   * @param completionHandler 
   */
  public void close(Handler<Void> completionHandler) { 
    this.delegate.close(completionHandler);
  }

  /**
   * Close the shell session and terminate all the underlying jobs.
   */
  public void close() { 
    this.delegate.close();
  }


  public static JobController newInstance(io.vertx.ext.shell.system.JobController arg) {
    return arg != null ? new JobController(arg) : null;
  }
}
