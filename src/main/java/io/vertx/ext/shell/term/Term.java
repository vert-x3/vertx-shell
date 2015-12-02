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

package io.vertx.ext.shell.term;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.io.Stream;
import io.vertx.ext.shell.session.Session;

/**
 * The terminal.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface Term extends Tty {

  @Override
  Term resizehandler(Handler<Void> handler);

  @Override
  Term setStdin(Stream stdin);

  /**
   * @return the last time this term received input
   */
  long lastAccessedTime();

  /**
   * Echo some text in the terminal.
   *
   * @param text the text to echo
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  Term echo(String text);

  /**
   * Associate the term with a session.
   *
   * @param session the session to set
   * @return a reference to this, so the API can be used fluently
   */
  Term setSession(Session session);

  /**
   * Set an interrupt signal handler on the term.
   *
   * @param handler the interrupt handler
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  Term interruptHandler(SignalHandler handler);

  /**
   * Set a suspend signal handler on the term.
   *
   * @param handler the suspend handler
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  Term suspendHandler(SignalHandler handler);

  /**
   * Prompt the user a line of text.
   *
   * @param prompt the displayed prompt
   * @param lineHandler the line handler called with the line
   */
  void readline(String prompt, Handler<String> lineHandler);

  /**
   * Prompt the user a line of text, providing a completion handler to handle user's completion.
   *
   * @param prompt the displayed prompt
   * @param lineHandler the line handler called with the line
   * @param completionHandler the completion handler
   */
  void readline(String prompt, Handler<String> lineHandler, Handler<Completion> completionHandler);

  /**
   * Set a handler that will be called when the terminal is closed.
   *
   * @param handler the handler
   * @return a reference to this, so the API can be used fluently
   */
  @Fluent
  Term closeHandler(Handler<Void> handler);

  /**
   * Close the connection to terminal.
   */
  void close();
}
