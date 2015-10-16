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
import io.vertx.groovy.ext.shell.io.Tty
import io.vertx.ext.shell.system.JobStatus
import io.vertx.core.Handler
/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
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
  public int id() {
    def ret = this.delegate.id();
    return ret;
  }
  public JobStatus status() {
    def ret = this.delegate.status();
    return ret;
  }
  public long lastStopped() {
    def ret = this.delegate.lastStopped();
    return ret;
  }
  public String line() {
    def ret = this.delegate.line();
    return ret;
  }
  public Tty getTty() {
    def ret= InternalHelper.safeCreate(this.delegate.getTty(), io.vertx.groovy.ext.shell.io.Tty.class);
    return ret;
  }
  public Job setTty(Tty tty) {
    this.delegate.setTty((io.vertx.ext.shell.io.Tty)tty.getDelegate());
    return this;
  }
  public Job resize() {
    this.delegate.resize();
    return this;
  }
  public boolean interrupt() {
    def ret = this.delegate.interrupt();
    return ret;
  }
  public void run(Handler<Integer> endHandler) {
    this.delegate.run(endHandler);
  }
  public Job resume() {
    this.delegate.resume();
    return this;
  }
  public Job suspend() {
    this.delegate.suspend();
    return this;
  }
}