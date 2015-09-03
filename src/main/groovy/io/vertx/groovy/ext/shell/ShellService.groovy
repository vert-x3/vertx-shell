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
import io.vertx.ext.shell.ShellServiceOptions
import io.vertx.groovy.core.Vertx
/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
@CompileStatic
public class ShellService {
  final def io.vertx.ext.shell.ShellService delegate;
  public ShellService(io.vertx.ext.shell.ShellService delegate) {
    this.delegate = delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public static ShellService create(Vertx vertx, Map<String, Object> options) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.ShellService.create((io.vertx.core.Vertx)vertx.getDelegate(), options != null ? new io.vertx.ext.shell.ShellServiceOptions(new io.vertx.core.json.JsonObject(options)) : null), io.vertx.ext.shell.ShellService.class, io.vertx.groovy.ext.shell.ShellService.class);
    return ret;
  }
  public void start() {
    this.delegate.start();
  }
}
