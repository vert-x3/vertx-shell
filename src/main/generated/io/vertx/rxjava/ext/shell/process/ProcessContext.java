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

package io.vertx.rxjava.ext.shell.process;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.ext.shell.Session;
import io.vertx.rxjava.ext.shell.io.Tty;

/**
 * Allow a process to interact with its context during execution.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.process.ProcessContext original} non RX-ified interface using Vert.x codegen.
 */

public class ProcessContext {

  final io.vertx.ext.shell.process.ProcessContext delegate;

  public ProcessContext(io.vertx.ext.shell.process.ProcessContext delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * @return the tty assocated with this process
   * @return 
   */
  public Tty tty() { 
    Tty ret= Tty.newInstance(this.delegate.tty());
    return ret;
  }

  /**
   * @return the shell session
   * @return 
   */
  public Session session() { 
    Session ret= Session.newInstance(this.delegate.session());
    return ret;
  }

  /**
   * End the process.
   * @param status the termination status
   */
  public void end(int status) { 
    this.delegate.end(status);
  }


  public static ProcessContext newInstance(io.vertx.ext.shell.process.ProcessContext arg) {
    return arg != null ? new ProcessContext(arg) : null;
  }
}
