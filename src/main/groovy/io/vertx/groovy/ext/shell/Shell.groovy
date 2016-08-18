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

package io.vertx.groovy.ext.shell;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import java.util.List
import io.vertx.groovy.ext.shell.system.Job
import io.vertx.groovy.ext.shell.system.JobController
import io.vertx.groovy.ext.shell.cli.CliToken
import io.vertx.groovy.ext.shell.session.Session
/**
 * An interactive session between a consumer and a shell.
*/
@CompileStatic
public class Shell {
  private final def io.vertx.ext.shell.Shell delegate;
  public Shell(Object delegate) {
    this.delegate = (io.vertx.ext.shell.Shell) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Create a job, the created job should then be executed with the {@link io.vertx.groovy.ext.shell.system.Job#run} method.
   * @param line the command line creating this job
   * @return the created job
   */
  public Job createJob(List<CliToken> line) {
    def ret = InternalHelper.safeCreate(delegate.createJob(line != null ? (List)line.collect({(io.vertx.ext.shell.cli.CliToken)it.getDelegate()}) : null), io.vertx.groovy.ext.shell.system.Job.class);
    return ret;
  }
  /**
   * See {@link io.vertx.groovy.ext.shell.Shell#createJob}
   * @param line 
   * @return 
   */
  public Job createJob(String line) {
    def ret = InternalHelper.safeCreate(delegate.createJob(line), io.vertx.groovy.ext.shell.system.Job.class);
    return ret;
  }
  /**
   * @return the shell's job controller
   */
  public JobController jobController() {
    if (cached_0 != null) {
      return cached_0;
    }
    def ret = InternalHelper.safeCreate(delegate.jobController(), io.vertx.groovy.ext.shell.system.JobController.class);
    cached_0 = ret;
    return ret;
  }
  /**
   * @return the current shell session
   */
  public Session session() {
    if (cached_1 != null) {
      return cached_1;
    }
    def ret = InternalHelper.safeCreate(delegate.session(), io.vertx.groovy.ext.shell.session.Session.class);
    cached_1 = ret;
    return ret;
  }
  /**
   * Close the shell.
   */
  public void close() {
    delegate.close();
  }
  private JobController cached_0;
  private Session cached_1;
}
