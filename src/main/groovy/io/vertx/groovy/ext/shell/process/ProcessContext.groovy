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

package io.vertx.groovy.ext.shell.process;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.groovy.ext.shell.Session
import io.vertx.groovy.ext.shell.io.Tty
/**
 * Allow a process to interact with its context during execution.
*/
@CompileStatic
public class ProcessContext {
  private final def io.vertx.ext.shell.process.ProcessContext delegate;
  public ProcessContext(Object delegate) {
    this.delegate = (io.vertx.ext.shell.process.ProcessContext) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * @return the tty assocated with this process
   * @return 
   */
  public Tty tty() {
    def ret= InternalHelper.safeCreate(this.delegate.tty(), io.vertx.groovy.ext.shell.io.Tty.class);
    return ret;
  }
  /**
   * @return the shell session
   * @return 
   */
  public Session session() {
    def ret= InternalHelper.safeCreate(this.delegate.session(), io.vertx.groovy.ext.shell.Session.class);
    return ret;
  }
  /**
   * End the process.
   * @param status the termination status
   */
  public void end(int status) {
    this.delegate.end(status);
  }
}
