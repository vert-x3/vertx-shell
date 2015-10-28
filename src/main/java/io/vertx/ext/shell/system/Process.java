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

package io.vertx.ext.shell.system;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.shell.term.Tty;
import io.vertx.ext.shell.session.Session;

/**
 * A process managed by the shell.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Process {

  /**
   * Set the process tty.
   *
   * @param tty the process tty
   * @return this object
   */
  @Fluent
  Process setTty(Tty tty);

  /**
   * @return the process tty
   */
  Tty getTty();

  /**
   * Set the process session
   *
   * @param session the process session
   * @return this object
   */
  @Fluent
  Process setSession(Session session);

  /**
   * @return the process session
   */
  Session getSession();

  /**
   * Set an handler called when the process terminates.
   *
   * @param handler the terminate handler
   * @return this object
   */
  @Fluent
  Process terminateHandler(Handler<Integer> handler);

  /**
   * Run the process.
   */
  void run();

  /**
   * Attempt to interrupt the process.
   *
   * @return true if the process caught the signal
   */
  boolean interrupt();

  /**
   * Suspend the process.
   */
  void resume();

  /**
   * Resume the process.
   */
  void suspend();

  /**
   * Terminate the process.
   */
  void terminate();


}
