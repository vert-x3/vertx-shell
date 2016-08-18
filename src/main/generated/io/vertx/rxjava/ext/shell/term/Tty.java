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

package io.vertx.rxjava.ext.shell.term;

import java.util.Map;
import rx.Observable;
import io.vertx.core.Handler;

/**
 * Provide interactions with the Shell TTY.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.term.Tty original} non RX-ified interface using Vert.x codegen.
 */

public class Tty {

  final io.vertx.ext.shell.term.Tty delegate;

  public Tty(io.vertx.ext.shell.term.Tty delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * @return the declared tty type, for instance , , etc... it can be null when the tty does not have declared its type.
   */
  public String type() { 
    String ret = delegate.type();
    return ret;
  }

  /**
   * @return the current width, i.e the number of rows or  if unknown
   */
  public int width() { 
    int ret = delegate.width();
    return ret;
  }

  /**
   * @return the current height, i.e the number of columns or  if unknown
   */
  public int height() { 
    int ret = delegate.height();
    return ret;
  }

  /**
   * Set a stream handler on the standard input to read the data.
   * @param handler the standard input
   * @return this object
   */
  public Tty stdinHandler(Handler<String> handler) { 
    delegate.stdinHandler(handler);
    return this;
  }

  /**
   * Write data to the standard output.
   * @param data the data to write
   * @return this object
   */
  public Tty write(String data) { 
    delegate.write(data);
    return this;
  }

  /**
   * Set a resize handler, the handler is called when the tty size changes.
   * @param handler the resize handler
   * @return this object
   */
  public Tty resizehandler(Handler<Void> handler) { 
    delegate.resizehandler(handler);
    return this;
  }


  public static Tty newInstance(io.vertx.ext.shell.term.Tty arg) {
    return arg != null ? new Tty(arg) : null;
  }
}
