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

package io.vertx.rxjava.ext.shell.system;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;

/**
 * A process managed by the shell.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.system.Process original} non RX-ified interface using Vert.x codegen.
 */

public class Process {

  final io.vertx.ext.shell.system.Process delegate;

  public Process(io.vertx.ext.shell.system.Process delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Execute the process in the given context.
   * @param context the context
   */
  public void execute(ProcessContext context) { 
    this.delegate.execute((io.vertx.ext.shell.system.ProcessContext) context.getDelegate());
  }


  public static Process newInstance(io.vertx.ext.shell.system.Process arg) {
    return arg != null ? new Process(arg) : null;
  }
}
