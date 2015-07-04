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

package io.vertx.groovy.ext.shell.getopt;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
@CompileStatic
public class Option {
  final def io.vertx.ext.shell.getopt.Option delegate;
  public Option(io.vertx.ext.shell.getopt.Option delegate) {
    this.delegate = delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public static Option create(String name, int arity) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.getopt.Option.create(name, arity), io.vertx.ext.shell.getopt.Option.class, io.vertx.groovy.ext.shell.getopt.Option.class);
    return ret;
  }
  public String name() {
    def ret = this.delegate.name();
    return ret;
  }
  public int arity() {
    def ret = this.delegate.arity();
    return ret;
  }
}
