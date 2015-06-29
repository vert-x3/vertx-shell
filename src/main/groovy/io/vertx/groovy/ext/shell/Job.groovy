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
import io.vertx.core.Handler
/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
@CompileStatic
public class Job {
  final def io.vertx.ext.shell.Job delegate;
  public Job(io.vertx.ext.shell.Job delegate) {
    this.delegate = delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public Stream stdin() {
    def ret= InternalHelper.safeCreate(this.delegate.stdin(), io.vertx.ext.shell.Stream.class, io.vertx.groovy.ext.shell.Stream.class);
    return ret;
  }
  public void setStdout(Stream stdout) {
    this.delegate.setStdout((io.vertx.ext.shell.Stream)stdout.getDelegate());
  }
  public void run() {
    this.delegate.run();
  }
  public void run(Handler<Void> beginHandler) {
    this.delegate.run(beginHandler);
  }
  public boolean sendSignal(String signal) {
    def ret = this.delegate.sendSignal(signal);
    return ret;
  }
  public void endHandler(Handler<Integer> handler) {
    this.delegate.endHandler(handler);
  }
}
