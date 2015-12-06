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
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.core.Handler;

/**
 * A pseudo terminal used for controlling a {@link io.vertx.rxjava.ext.shell.term.Tty}. This interface acts as a pseudo
 * terminal master, {@link io.vertx.rxjava.ext.shell.term.Pty#slave} returns the assocated slave pseudo terminal.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.term.Pty original} non RX-ified interface using Vert.x codegen.
 */

public class Pty {

  final io.vertx.ext.shell.term.Pty delegate;

  public Pty(io.vertx.ext.shell.term.Pty delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Create a new pseudo terminal with no terminal type.
   * @return 
   */
  public static Pty create() { 
    Pty ret= Pty.newInstance(io.vertx.ext.shell.term.Pty.create());
    return ret;
  }

  /**
   * Create a new pseudo terminal.
   * @param terminalType the terminal type, for instance 
   * @return the created pseudo terminal
   */
  public static Pty create(String terminalType) { 
    Pty ret= Pty.newInstance(io.vertx.ext.shell.term.Pty.create(terminalType));
    return ret;
  }

  /**
   * Set the standard out handler of the pseudo terminal.
   * @param handler the standard output
   * @return this current object
   */
  public Pty stdoutHandler(Handler<String> handler) { 
    this.delegate.stdoutHandler(handler);
    return this;
  }

  /**
   * Write data to the slave standard input of the pseudo terminal.
   * @param data the data to write
   * @return this current object
   */
  public Pty write(String data) { 
    this.delegate.write(data);
    return this;
  }

  /**
   * Resize the terminal.
   * @param width 
   * @param height 
   * @return this current object
   */
  public Pty setSize(int width, int height) { 
    this.delegate.setSize(width, height);
    return this;
  }

  /**
   * @return the pseudo terminal slave
   * @return 
   */
  public Tty slave() { 
    Tty ret= Tty.newInstance(this.delegate.slave());
    return ret;
  }


  public static Pty newInstance(io.vertx.ext.shell.term.Pty arg) {
    return arg != null ? new Pty(arg) : null;
  }
}
