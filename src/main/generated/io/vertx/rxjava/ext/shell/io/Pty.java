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
 * A pseudo terminal used for controlling a {@link io.vertx.rxjava.ext.shell.io.Tty}. This interface acts as a pseudo
 * terminal master, {@link io.vertx.rxjava.ext.shell.io.Pty#slave} returns the assocated slave pseudo terminal.
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

  /**
   * Create a new pseudo terminal with no terminal type.
   * @return 
   */
  public static Pty create() { 
    Pty ret= Pty.newInstance(io.vertx.ext.shell.io.Pty.create());
    return ret;
  }

  /**
   * Create a new pseudo terminal.
   * @param terminalType the terminal type, for instance 
   * @return the created pseudo terminal
   */
  public static Pty create(String terminalType) { 
    Pty ret= Pty.newInstance(io.vertx.ext.shell.io.Pty.create(terminalType));
    return ret;
  }

  /**
   * @return the standard input of the terminal
   * @return 
   */
  public Stream stdin() { 
    Stream ret= Stream.newInstance(this.delegate.stdin());
    return ret;
  }

  /**
   * Set the standard out of the pseudo terminal.
   * @param stdout the standard output
   * @return this current object
   */
  public Pty setStdout(Stream stdout) { 
    this.delegate.setStdout((io.vertx.ext.shell.io.Stream) stdout.getDelegate());
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


  public static Pty newInstance(io.vertx.ext.shell.io.Pty arg) {
    return arg != null ? new Pty(arg) : null;
  }
}
