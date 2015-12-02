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
/**
 * A job executed in a {@link io.vertx.groovy.ext.shell.system.Shell}, grouping one or several process.<p/>
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
    def ret = this.delegate.id();
    return ret;
  }
  /**
   * @return the job status
   * @return 
   */
  public ExecStatus status() {
    def ret = this.delegate.status();
    return ret;
  }
  /**
   * @return when the job was last stopped
   * @return 
   */
  public long lastStopped() {
    def ret = this.delegate.lastStopped();
    return ret;
  }
  /**
   * @return the execution line of the job, i.e the shell command line that launched this job
   * @return 
   */
  public String line() {
    def ret = this.delegate.line();
    return ret;
  }
  /**
   * @return the current tty this job uses
   * @return 
   */
  public Tty getTty() {
    def ret= InternalHelper.safeCreate(this.delegate.getTty(), io.vertx.groovy.ext.shell.term.Tty.class);
    return ret;
  }
  /**
   * Set a tty on the job.
   * @param tty the tty to use
   * @return 
   */
  public Job setTty(Tty tty) {
    this.delegate.setTty((io.vertx.ext.shell.term.Tty)tty.getDelegate());
    return this;
  }
  /**
   * Set an handler called when the job terminates.
   * @param handler the terminate handler
   * @return this object
   */
  public Job terminateHandler(Handler<Integer> handler) {
    this.delegate.terminateHandler(handler);
    return this;
  }
  /**
   * Run the job, before running the job a  must be set.
   */
  public void run() {
    this.delegate.run();
  }
  /**
   * Attempt to interrupt the job.
   * @return true if the job is actually interrupted
   */
  public boolean interrupt() {
    def ret = this.delegate.interrupt();
    return ret;
  }
  /**
   * Resume the job to foreground.
   */
  public void resume() {
    this.delegate.resume();
  }
  public void toBackground() {
    this.delegate.toBackground();
  }
  public void toForeground() {
    this.delegate.toForeground();
  }
  /**
   * Resume the job.
   * @param foreground true when the job is resumed in foreground
   */
  public void resume(boolean foreground) {
    this.delegate.resume(foreground);
  }
  /**
   * Resume the job.
   */
  public void suspend() {
    this.delegate.suspend();
  }
  /**
   * Terminate the job.
   */
  public void terminate() {
    this.delegate.terminate();
  }
}
