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

package io.vertx.rxjava.ext.shell.command;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.ext.shell.Stream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.command.Execution original} non RX-ified interface using Vert.x codegen.
 */

public class Execution {

  final io.vertx.ext.shell.command.Execution delegate;

  public Execution(io.vertx.ext.shell.command.Execution delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public Execution setStdin(Stream stdin) { 
    this.delegate.setStdin((io.vertx.ext.shell.Stream) stdin.getDelegate());
    return this;
  }

  public Stream stdout() { 
    Stream ret= Stream.newInstance(this.delegate.stdout());
    return ret;
  }

  public Execution write(String text) { 
    this.delegate.write(text);
    return this;
  }

  public void end(int code) { 
    this.delegate.end(code);
  }


  public static Execution newInstance(io.vertx.ext.shell.command.Execution arg) {
    return arg != null ? new Execution(arg) : null;
  }
}
