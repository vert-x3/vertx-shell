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

package io.vertx.rxjava.ext.shell.io;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.io.Pty original} non RX-ified interface using Vert.x codegen.
 */

public class Pty {

  final io.vertx.ext.shell.io.Pty delegate;

  public Pty(io.vertx.ext.shell.io.Pty delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static Pty create() { 
    Pty ret= Pty.newInstance(io.vertx.ext.shell.io.Pty.create());
    return ret;
  }

  public static Pty create(String term) { 
    Pty ret= Pty.newInstance(io.vertx.ext.shell.io.Pty.create(term));
    return ret;
  }

  public Stream stdin() { 
    Stream ret= Stream.newInstance(this.delegate.stdin());
    return ret;
  }

  public Pty setSize(int width, int height) { 
    this.delegate.setSize(width, height);
    return this;
  }

  public Tty slave() { 
    Tty ret= Tty.newInstance(this.delegate.slave());
    return ret;
  }


  public static Pty newInstance(io.vertx.ext.shell.io.Pty arg) {
    return arg != null ? new Pty(arg) : null;
  }
}
