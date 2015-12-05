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

package io.vertx.groovy.ext.shell.system;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import java.util.List
import java.util.Set
import io.vertx.groovy.ext.shell.cli.CliToken
import io.vertx.core.Handler
import io.vertx.groovy.ext.shell.session.Session
/**
 * An interactive session between a consumer and a shell.<p/>
*/
@CompileStatic
public class Shell {
  private final def io.vertx.ext.shell.system.Shell delegate;
  public Shell(Object delegate) {
    this.delegate = (io.vertx.ext.shell.system.Shell) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * @return the shell session
   * @return 
   */
  public Session session() {
    if (cached_0 != null) {
      return cached_0;
    }
    def ret= InternalHelper.safeCreate(this.delegate.session(), io.vertx.groovy.ext.shell.session.Session.class);
    cached_0 = ret;
    return ret;
  }
  /**
   * @return the jobs active in this session
   * @return 
   */
  public Set<Job> jobs() {
    def ret = this.delegate.jobs()?.collect({underpants -> new io.vertx.groovy.ext.shell.system.Job(underpants)}) as Set;
    return ret;
  }
  /**
   * Returns an active job in this session by its .
   * @param id the job id
   * @return the job of  when not found
   */
  public Job getJob(int id) {
    def ret= InternalHelper.safeCreate(this.delegate.getJob(id), io.vertx.groovy.ext.shell.system.Job.class);
    return ret;
  }
  /**
   * Create a job, the created job should then be executed with the {@link io.vertx.groovy.ext.shell.system.Job#run} method.
   * @param line the command line creating this job
   * @return the created job
   */
  public Job createJob(List<CliToken> line) {
    def ret= InternalHelper.safeCreate(this.delegate.createJob((List<io.vertx.ext.shell.cli.CliToken>)(line.collect({underpants -> underpants.getDelegate()}))), io.vertx.groovy.ext.shell.system.Job.class);
    return ret;
  }
  /**
   * See {@link io.vertx.groovy.ext.shell.system.Shell#createJob}
   * @param line 
   * @return 
   */
  public Job createJob(String line) {
    def ret= InternalHelper.safeCreate(this.delegate.createJob(line), io.vertx.groovy.ext.shell.system.Job.class);
    return ret;
  }
  /**
   * Close the shell session and terminate all the underlying jobs.
   */
  public void close() {
    this.delegate.close();
  }
  /**
   * Close the shell session and terminate all the underlying jobs.
   * @param completionHandler 
   */
  public void close(Handler<Void> completionHandler) {
    this.delegate.close(completionHandler);
  }
  private Session cached_0;
}
