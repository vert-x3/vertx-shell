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

  ExecStatus status();

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
  default void run() {
    run(null);
  }

  /**
   * Run the process.
   */
  default void run(boolean foreground) {
    run(foreground, null);
  }

  /**
   * Run the process.
   *
   * @param completionHandler handler called after process callback
   */
  default void run(Handler<Void> completionHandler) {
    run(true, completionHandler);
  }

  /**
   * Run the process.
   *
   * @param completionHandler handler called after process callback
   */
  void run(boolean foregraound, Handler<Void> completionHandler);

  /**
   * Attempt to interrupt the process.
   *
   * @return true if the process caught the signal
   */
  default boolean interrupt() {
    return interrupt(null);
  }

  /**
   * Attempt to interrupt the process.
   *
   * @param completionHandler handler called after interrupt callback
   * @return true if the process caught the signal
   */
  boolean interrupt(Handler<Void> completionHandler);

  /**
   * Suspend the process.
   */
  default void resume() {
    resume(null);
  }

  /**
   * Suspend the process.
   */
  default void resume(boolean foreground) {
    resume(foreground, null);
  }

  /**
   * Suspend the process.
   *
   * @param completionHandler handler called after resume callback
   */
  default void resume(Handler<Void> completionHandler) {
    resume(true, completionHandler);
  }

  /**
   * Suspend the process.
   *
   * @param completionHandler handler called after resume callback
   */
  void resume(boolean foreground, Handler<Void> completionHandler);

  /**
   * Resume the process.
   */
  default void suspend() {
    suspend(null);
  }

  /**
   * Resume the process.
   *
   * @param completionHandler handler called after suspend callback
   */
  void suspend(Handler<Void> completionHandler);

  /**
   * Terminate the process.
   */
  default void terminate() {
    terminate(null);
  }

  /**
   * Terminate the process.
   *
   * @param completionHandler handler called after end callback
   */
  void terminate(Handler<Void> completionHandler);

  /**
   * Set the process in background.
   */
  default void toBackground() {
    toBackground(null);
  }

  /**
   * Set the process in background.
   *
   * @param completionHandler handler called after background callback
   */
  void toBackground(Handler<Void> completionHandler);

  /**
   * Set the process in foreground.
   */
  default void toForeground() {
    toForeground(null);
  }

  /**
   * Set the process in foreground.
   *
   * @param completionHandler handler called after foreground callback
   */
  void toForeground(Handler<Void> completionHandler);
}
