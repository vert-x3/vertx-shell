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
/**
 * A process managed by the shell.
*/
@CompileStatic
public class Process {
  private final def io.vertx.ext.shell.process.Process delegate;
  public Process(Object delegate) {
    this.delegate = (io.vertx.ext.shell.process.Process) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Execute the process in the given context.
   * @param context the context
   */
  public void execute(ProcessContext context) {
    this.delegate.execute((io.vertx.ext.shell.process.ProcessContext)context.getDelegate());
  }
}
