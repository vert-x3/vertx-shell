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
import io.vertx.rxjava.ext.shell.io.Tty;
import io.vertx.ext.shell.system.JobStatus;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.system.Job original} non RX-ified interface using Vert.x codegen.
 */

public class Job {

  final io.vertx.ext.shell.system.Job delegate;

  public Job(io.vertx.ext.shell.system.Job delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public int id() { 
    int ret = this.delegate.id();
    return ret;
  }

  public JobStatus status() { 
    JobStatus ret = this.delegate.status();
    return ret;
  }

  public long lastStopped() { 
    long ret = this.delegate.lastStopped();
    return ret;
  }

  public String line() { 
    String ret = this.delegate.line();
    return ret;
  }

  public Tty getTty() { 
    Tty ret= Tty.newInstance(this.delegate.getTty());
    return ret;
  }

  public void setTty(Tty tty) { 
    this.delegate.setTty((io.vertx.ext.shell.io.Tty) tty.getDelegate());
  }

  public boolean interrupt() { 
    boolean ret = this.delegate.interrupt();
    return ret;
  }

  public void run(Handler<Integer> endHandler) { 
    this.delegate.run(endHandler);
  }

  public void resume() { 
    this.delegate.resume();
  }

  public void suspend() { 
    this.delegate.suspend();
  }


  public static Job newInstance(io.vertx.ext.shell.system.Job arg) {
    return arg != null ? new Job(arg) : null;
  }
}
