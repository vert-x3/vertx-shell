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

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.impl.ShellServerImpl;
import io.vertx.ext.shell.command.CommandResolver;
import io.vertx.ext.shell.system.JobController;
import io.vertx.ext.shell.term.Term;
import io.vertx.ext.shell.term.TermServer;

/**
 * The shell server.<p/>
 *
 * A shell server is associated with a collection of {@link TermServer term servers}: the {@link #registerTermServer(TermServer)}
 * method registers a term server. Term servers life cycle are managed by this server.<p/>
 *
 * When a {@link TermServer term server} receives an incoming connection, a {@link JobController} instance is created and
 * associated with this connection.<p/>
 *
 * The {@link #createShell()} method can be used to create {@link JobController} instance for testing purposes.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface ShellServer {

  /**
   * Create a new shell server with default options.
   *
   * @param vertx the vertx
   * @param options the options
   * @return the created shell server
   */
  static ShellServer create(Vertx vertx, ShellServerOptions options) {
    return new ShellServerImpl(vertx, options);
  }

  /**
   * Create a new shell server with specific options.
   *
   * @param vertx the vertx
   * @return the created shell server
   */
  static ShellServer create(Vertx vertx) {
    return new ShellServerImpl(vertx, new ShellServerOptions());
  }

  /**
   * Register a command resolver for this server.
   *
   * @param resolver the resolver
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  ShellServer registerCommandResolver(CommandResolver resolver);

  /**
   * Register a term server to this shell server, the term server lifecycle methods are managed by this shell server.
   *
   * @param termServer the term server to add
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  ShellServer registerTermServer(TermServer termServer);

  /**
   * Create a new shell, the returned shell should be closed explicitely.
   *
   * @param term the shell associated terminal
   * @return the created shell
   */
  Shell createShell(Term term);

  /**
   * Create a new shell, the returned shell should be closed explicitely.
   *
   * @return the created shell
   */
  Shell createShell();

  /**
   * Start the shell service, this is an asynchronous start.
   */
  @Fluent
  default ShellServer listen() {
    return listen(ar -> {});
  }

  /**
   * Start the shell service, this is an asynchronous start.
   *
   * @param listenHandler handler for getting notified when service is started
   */
  @Fluent
  ShellServer listen(Handler<AsyncResult<Void>> listenHandler);

  /**
   * Close the shell server, this is an asynchronous close.
   */
  default void close() {
    close(ar -> {});
  }

  /**
   * Close the shell server, this is an asynchronous close.
   *
   * @param completionHandler handler for getting notified when service is stopped
   */
  void close(Handler<AsyncResult<Void>> completionHandler);


  /**
   * Called when a new shell is created. Can be used to prepopulate the shell session with objects
   * or set the prompt.
   *
   * @param shellHandler handler for getting notified when the server creates a new shell.
   */
  void shellHandler(Handler<Shell> shellHandler);



}
