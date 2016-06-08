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
import io.vertx.ext.shell.system.ExecStatus
import io.vertx.core.Handler
import io.vertx.groovy.ext.shell.term.Tty
import io.vertx.groovy.ext.shell.session.Session
/**
 * A job executed in a {@link io.vertx.groovy.ext.shell.system.JobController}, grouping one or several process.<p/>
 *
 * The job life cycle can be controlled with the {@link io.vertx.groovy.ext.shell.system.Job#run}, {@link io.vertx.groovy.ext.shell.system.Job#resume} and {@link io.vertx.groovy.ext.shell.system.Job#suspend} and {@link io.vertx.groovy.ext.shell.system.Job#interrupt}
 * methods.
*/
@CompileStatic
public class Job {
  private final def io.vertx.ext.shell.system.Job delegate;
  public Job(Object delegate) {
    this.delegate = (io.vertx.ext.shell.system.Job) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * @return the job id
   * @return 
   */
  public int id() {
    def ret = delegate.id();
    return ret;
  }
  /**
   * @return the job exec status
   * @return 
   */
  public ExecStatus status() {
    def ret = delegate.status();
    return ret;
  }
  /**
   * @return when the job was last stopped
   * @return 
   */
  public long lastStopped() {
    def ret = delegate.lastStopped();
    return ret;
  }
  /**
   * @return the execution line of the job, i.e the shell command line that launched this job
   * @return 
   */
  public String line() {
    def ret = delegate.line();
    return ret;
  }
  /**
   * Set a tty on the job.
   * @param tty the tty to use
   * @return this object
   */
  public Job setTty(Tty tty) {
    delegate.setTty(tty != null ? (io.vertx.ext.shell.term.Tty)tty.getDelegate() : null);
    return this;
  }
  /**
   * Set a session on the job.
   * @param session the session to use
   * @return this object
   */
  public Job setSession(Session session) {
    delegate.setSession(session != null ? (io.vertx.ext.shell.session.Session)session.getDelegate() : null);
    return this;
  }
  /**
   * Set an handler called when the job terminates.
   * @param handler the terminate handler
   * @return this object
   */
  public Job statusUpdateHandler(Handler<ExecStatus> handler) {
    delegate.statusUpdateHandler(handler);
    return this;
  }
  /**
   * Run the job, before running the job a  must be set.
   * @return this object
   */
  public Job run() {
    delegate.run();
    return this;
  }
  /**
   * Attempt to interrupt the job.
   * @return true if the job is actually interrupted
   */
  public boolean interrupt() {
    def ret = delegate.interrupt();
    return ret;
  }
  /**
   * Resume the job to foreground.
   * @return 
   */
  public Job resume() {
    def ret = InternalHelper.safeCreate(delegate.resume(), io.vertx.groovy.ext.shell.system.Job.class);
    return ret;
  }
  /**
   * Send the job to background.
   * @return this object
   */
  public Job toBackground() {
    delegate.toBackground();
    return this;
  }
  /**
   * Send the job to foreground.
   * @return this object
   */
  public Job toForeground() {
    delegate.toForeground();
    return this;
  }
  /**
   * Resume the job.
   * @param foreground true when the job is resumed in foreground
   * @return 
   */
  public Job resume(boolean foreground) {
    delegate.resume(foreground);
    return this;
  }
  /**
   * Resume the job.
   * @return this object
   */
  public Job suspend() {
    delegate.suspend();
    return this;
  }
  /**
   * Terminate the job.
   */
  public void terminate() {
    delegate.terminate();
  }
  /**
   * @return the first process in the job
   * @return 
   */
  public Process process() {
    if (cached_0 != null) {
      return cached_0;
    }
    def ret = InternalHelper.safeCreate(delegate.process(), io.vertx.groovy.ext.shell.system.Process.class);
    cached_0 = ret;
    return ret;
  }
  private Process cached_0;
}
