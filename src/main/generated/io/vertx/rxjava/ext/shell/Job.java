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

package io.vertx.rxjava.ext.shell;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.Job original} non RX-ified interface using Vert.x codegen.
 */

public class Job {

  final io.vertx.ext.shell.Job delegate;

  public Job(io.vertx.ext.shell.Job delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public Stream stdin() { 
    Stream ret= Stream.newInstance(this.delegate.stdin());
    return ret;
  }

  public void setStdout(Stream stdout) { 
    this.delegate.setStdout((io.vertx.ext.shell.Stream) stdout.getDelegate());
  }

  public void run() { 
    this.delegate.run();
  }

  public void run(Handler<Void> beginHandler) { 
    this.delegate.run(beginHandler);
  }

  public boolean sendEvent(String event) { 
    boolean ret = this.delegate.sendEvent(event);
    return ret;
  }

  public void endHandler(Handler<Integer> handler) { 
    this.delegate.endHandler(handler);
  }

  public void setWindowSize(Dimension size) { 
    this.delegate.setWindowSize((io.vertx.ext.shell.Dimension) size.getDelegate());
  }


  public static Job newInstance(io.vertx.ext.shell.Job arg) {
    return arg != null ? new Job(arg) : null;
  }
}
