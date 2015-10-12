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

package io.vertx.rxjava.ext.shell.process;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.ext.shell.io.Tty;
import io.vertx.rxjava.ext.shell.session.Session;

/**
 * Allow a process to interact with its context during execution.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.process.ProcessContext original} non RX-ified interface using Vert.x codegen.
 */

public class ProcessContext {

  final io.vertx.ext.shell.process.ProcessContext delegate;

  public ProcessContext(io.vertx.ext.shell.process.ProcessContext delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * @return the tty assocated with this process
   * @return 
   */
  public Tty tty() { 
    Tty ret= Tty.newInstance(this.delegate.tty());
    return ret;
  }

  /**
   * @return the shell session
   * @return 
   */
  public Session session() { 
    Session ret= Session.newInstance(this.delegate.session());
    return ret;
  }

  /**
   * End the process.
   * @param status the termination status
   */
  public void end(int status) { 
    this.delegate.end(status);
  }


  public static ProcessContext newInstance(io.vertx.ext.shell.process.ProcessContext arg) {
    return arg != null ? new ProcessContext(arg) : null;
  }
}
