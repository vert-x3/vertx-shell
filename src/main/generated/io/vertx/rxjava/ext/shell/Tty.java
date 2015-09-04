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
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.Tty original} non RX-ified interface using Vert.x codegen.
 */

public class Tty {

  final io.vertx.ext.shell.Tty delegate;

  public Tty(io.vertx.ext.shell.Tty delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * @return the current width, the number of rows
   * @return 
   */
  public int width() { 
    int ret = this.delegate.width();
    return ret;
  }

  /**
   * @return the current height, the number of columns
   * @return 
   */
  public int height() { 
    int ret = this.delegate.height();
    return ret;
  }

  public Tty setStdin(Handler<String> stdin) { 
    this.delegate.setStdin(stdin);
    return this;
  }

  public Stream stdout() { 
    Stream ret= Stream.newInstance(this.delegate.stdout());
    return ret;
  }

  public Tty eventHandler(String event, Handler<Void> handler) { 
    this.delegate.eventHandler(event, handler);
    return this;
  }


  public static Tty newInstance(io.vertx.ext.shell.Tty arg) {
    return arg != null ? new Tty(arg) : null;
  }
}
