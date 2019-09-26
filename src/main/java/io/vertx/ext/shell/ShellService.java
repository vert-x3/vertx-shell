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

package io.vertx.ext.shell;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.impl.ShellServiceImpl;

/**
 * The shell service, provides a remotely accessible shell available via Telnet or SSH according to the
 * {@link io.vertx.ext.shell.ShellServiceOptions} configuration.<p/>
 *
 * The shell service will expose commands using {@link io.vertx.ext.shell.command.CommandResolver} on the classpath and
 * the shared command registry for the Vert.x instance.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ShellService {

  /**
   * Like {@link #create(Vertx, ShellServiceOptions)}, with default options.
   */
  static ShellService create(Vertx vertx) {
    return create(vertx, new ShellServiceOptions());
  }

  /**
   * Create a new shell service.
   *
   * @param vertx the Vert.x instance
   * @param options the service config options
   * @return the shell service
   */
  static ShellService create(Vertx vertx, ShellServiceOptions options) {
    return new ShellServiceImpl(vertx, options);
  }

  /**
   * Start the shell service, this is an asynchronous start.
   */
  default Future<Void> start() {
    Promise<Void> promise = Promise.promise();
    start(promise);
    return promise.future();
  }

  /**
   * Start the shell service, this is an asynchronous start.
   *
   * @param startHandler handler for getting notified when service is started
   */
  void start(Handler<AsyncResult<Void>> startHandler);

  /**
   * @return the shell server
   */
  ShellServer server();

  /**
   * Stop the shell service, this is an asynchronous stop.
   */
  default Future<Void> stop() {
    Promise<Void> promise = Promise.promise();
    stop(promise);
    return promise.future();
  }

  /**
   * Stop the shell service, this is an asynchronous start.
   *
   * @param stopHandler handler for getting notified when service is stopped
   */
  void stop(Handler<AsyncResult<Void>> stopHandler);

}
