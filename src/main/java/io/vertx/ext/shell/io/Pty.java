/*
 * Copyright 2015 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 *
 *
 * Copyright (c) 2015 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 *
 */

package io.vertx.ext.shell.io;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.ext.shell.io.impl.PtyImpl;

/**
 * A pseudo terminal used for controlling a {@link io.vertx.ext.shell.io.Tty}. This interface acts as a pseudo
 * terminal master, {@link #slave()} returns the assocated slave pseudo terminal.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Pty {

  /**
   * Create a new pseudo terminal with no terminal type.
   *
   * @see #create(String)
   */
  static Pty create() {
    return new PtyImpl(null);
  }

  /**
   * Create a new pseudo terminal.
   *
   * @param terminalType the terminal type, for instance {@literal vt100}
   * @return the created pseudo terminal
   */
  static Pty create(String terminalType) {
    return new PtyImpl(terminalType);
  }

  /**
   * @return the standard input of the terminal
   */
  Stream stdin();

  Pty setStdout(Stream stdout);

  /**
   * Resize the terminal.
   *
   * @return the new standard input of the terminal
   */
  @Fluent
  Pty setSize(int width, int height);

  /**
   * @return the pseudo terminal slave
   */
  Tty slave();

}
