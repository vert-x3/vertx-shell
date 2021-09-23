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

package io.vertx.ext.shell.term.impl;

import io.termd.core.readline.Keymap;
import io.termd.core.telnet.TelnetTtyConnection;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.shell.term.TelnetTermOptions;
import io.vertx.ext.shell.term.TermServer;
import io.vertx.ext.shell.term.Term;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

/**
 * Encapsulate the Telnet server setup.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TelnetTermServer implements TermServer {

  private final Vertx vertx;
  private final TelnetTermOptions options;
  private Handler<Term> termHandler;
  private NetServer server;

  public TelnetTermServer(Vertx vertx, TelnetTermOptions options) {
    this.vertx = vertx;
    this.options = new TelnetTermOptions(options);
  }

  @Override
  public TermServer authenticationProvider(AuthenticationProvider provider) {
    return this;
  }

  @Override
  public TermServer termHandler(Handler<Term> handler) {
    termHandler = handler;
    return this;
  }

  @Override
  public TermServer listen(Handler<AsyncResult<Void>> listenHandler) {
    Charset charset = Charset.forName(options.getCharset());
    if (server == null) {
      server = vertx.createNetServer(options);
      Buffer inputrc = Helper.loadResource(vertx.fileSystem(), options.getIntputrc());
      if (inputrc == null) {
        listenHandler.handle(Future.failedFuture("Could not load inputrc from " + options.getIntputrc()));
        return this;
      }
      Keymap keymap = new Keymap(new ByteArrayInputStream(inputrc.getBytes()));
      TermConnectionHandler connectionHandler = new TermConnectionHandler(vertx, keymap, termHandler);
      server.connectHandler(new TelnetSocketHandler(vertx, () -> {
        return new TelnetTtyConnection(options.getInBinary(), options.getOutBinary(), charset, connectionHandler::handle);
      }));
      server.listen(ar -> {
        if (ar.succeeded()) {
          listenHandler.handle(Future.succeededFuture());
        } else {
          listenHandler.handle(Future.failedFuture(ar.cause()));
        }
      });
    } else {
      listenHandler.handle(Future.failedFuture("Already started"));
    }
    return this;
  }

  public void close(Handler<AsyncResult<Void>> completionHandler) {
    if (server != null) {
      server.close(completionHandler);
      server = null;
    } else {
      completionHandler.handle(Future.failedFuture("No started"));
    }
  }

  public int actualPort() {
    return server.actualPort();
  }
}
