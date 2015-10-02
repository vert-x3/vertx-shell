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

package io.vertx.rxjava.ext.shell.io;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.ext.shell.io.EventType;
import io.vertx.core.Handler;

/**
 * Provide interactions with the Shell TTY.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.io.Tty original} non RX-ified interface using Vert.x codegen.
 */

public class Tty {

  final io.vertx.ext.shell.io.Tty delegate;

  public Tty(io.vertx.ext.shell.io.Tty delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * @return the current width, i.e the number of rows or  if unknown
   * @return 
   */
  public int width() { 
    int ret = this.delegate.width();
    return ret;
  }

  /**
   * @return the current height, i.e the number of columns or  if unknown
   * @return 
   */
  public int height() { 
    int ret = this.delegate.height();
    return ret;
  }

  /**
   * Set a stream on the standard input to read the data.
   * @param stdin the standard input
   * @return this object
   */
  public Tty setStdin(Stream stdin) { 
    this.delegate.setStdin((io.vertx.ext.shell.io.Stream) stdin.getDelegate());
    return this;
  }

  /**
   * Set an handler the standard input to read the data in String format.
   * @param stdin the standard input
   * @return this object
   */
  public Tty setStdin(Handler<String> stdin) { 
    this.delegate.setStdin(stdin);
    return this;
  }

  /**
   * @return the standard output for emitting data
   * @return 
   */
  public Stream stdout() { 
    Stream ret= Stream.newInstance(this.delegate.stdout());
    return ret;
  }

  /**
   * Set an event handler to be notified by Shell events.
   * @param eventType the event type
   * @param handler 
   * @return 
   */
  public Tty eventHandler(EventType eventType, Handler<Void> handler) { 
    this.delegate.eventHandler(eventType, handler);
    return this;
  }


  public static Tty newInstance(io.vertx.ext.shell.io.Tty arg) {
    return arg != null ? new Tty(arg) : null;
  }
}
