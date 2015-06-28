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
import io.vertx.groovy.ext.shell.command.CommandManager
import io.vertx.groovy.core.Vertx
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
@CompileStatic
public class Shell {
  final def io.vertx.ext.shell.Shell delegate;
  public Shell(io.vertx.ext.shell.Shell delegate) {
    this.delegate = delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public static Shell create(Vertx vertx, CommandManager manager) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.Shell.create((io.vertx.core.Vertx)vertx.getDelegate(), (io.vertx.ext.shell.command.CommandManager)manager.getDelegate()), io.vertx.ext.shell.Shell.class, io.vertx.groovy.ext.shell.Shell.class);
    return ret;
  }
  public void createJob(String s, Handler<AsyncResult<Job>> handler) {
    this.delegate.createJob(s, new Handler<AsyncResult<io.vertx.ext.shell.Job>>() {
      public void handle(AsyncResult<io.vertx.ext.shell.Job> event) {
        AsyncResult<Job> f
        if (event.succeeded()) {
          f = InternalHelper.<Job>result(new Job(event.result()))
        } else {
          f = InternalHelper.<Job>failure(event.cause())
        }
        handler.handle(f)
      }
    });
  }
}
