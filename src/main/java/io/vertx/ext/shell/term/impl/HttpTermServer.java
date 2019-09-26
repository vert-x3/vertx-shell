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
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.shell.term.Term;
import io.vertx.ext.shell.term.TermServer;
import io.vertx.ext.shell.term.HttpTermOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.BasicAuthHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HttpTermServer implements TermServer {

  private final Vertx vertx;
  private HttpTermOptions options;
  private Handler<Term> termHandler;
  private HttpServer server;
  private Router router;
  private AuthProvider authProvider;

  public HttpTermServer(Vertx vertx, HttpTermOptions options) {
    this(vertx, null, options);
  }

  public HttpTermServer(Vertx vertx, Router router, HttpTermOptions options) {
    this.vertx = vertx;
    // this.options = new HttpTermOptions(options); <-- can't use that because SockJSHandlerOptions is not copiable yet
    this.options = options;
    this.router = router;
  }

  @Override
  public TermServer termHandler(Handler<Term> handler) {
    termHandler = handler;
    return this;
  }

  @Override
  public TermServer authProvider(AuthProvider provider) {
    authProvider = provider;
    return this;
  }

  @Override
  public TermServer listen(Handler<AsyncResult<Void>> listenHandler) {

    Charset charset = Charset.forName(options.getCharset());

    boolean createServer = false;
    if (router == null) {
      createServer = true;
      router = Router.router(vertx);
    }

    if (options.getAuthOptions() != null) {
      authProvider = options.getAuthOptions().createProvider(vertx);
    }

    if (options.getSockJSPath() != null && options.getSockJSHandlerOptions() != null) {
      if (authProvider != null) {
        AuthHandler basicAuthHandler = BasicAuthHandler.create(authProvider);
        router.route(options.getSockJSPath()).handler(basicAuthHandler);
      }

      Buffer inputrc = Helper.loadResource(vertx.fileSystem(), options.getIntputrc());
      if (inputrc == null) {
        if (listenHandler != null) {
          listenHandler.handle(Future.failedFuture("Could not load inputrc from " + options.getIntputrc()));
        }
        return this;
      }
      Keymap keymap = new Keymap(new ByteArrayInputStream(inputrc.getBytes()));
      SockJSHandler sockJSHandler = SockJSHandler.create(vertx, options.getSockJSHandlerOptions());
      sockJSHandler.socketHandler(new SockJSTermHandlerImpl(vertx, charset, keymap).termHandler(termHandler));
      router.route(options.getSockJSPath()).handler(sockJSHandler);
    }

    if (options.getVertsShellJsResource() != null) {
      router.get("/vertxshell.js").handler(ctx -> ctx.response().putHeader("Content-Type", "application/javascript").end(options.getVertsShellJsResource()));
    }
    if (options.getTermJsResource() != null) {
      router.get("/term.js").handler(ctx -> ctx.response().putHeader("Content-Type", "application/javascript").end(options.getTermJsResource()));
    }
    if (options.getShellHtmlResource() != null) {
      router.get("/shell.html").handler(ctx -> ctx.response().putHeader("Content-Type", "text/html").end(options.getShellHtmlResource()));
    }

    if (createServer) {
      server = vertx.createHttpServer(options);
      server.requestHandler(router);
      server.listen(ar -> {
        if (listenHandler != null) {
          if (ar.succeeded()) {
            listenHandler.handle(Future.succeededFuture());
          } else {
            listenHandler.handle(Future.failedFuture(ar.cause()));
          }
        }
      });
    } else {
      if (listenHandler != null) {
        listenHandler.handle(Future.succeededFuture());
      }
    }
    return this;
  }

  @Override
  public int actualPort() {
    return -1;
  }

  @Override
  public void close(Handler<AsyncResult<Void>> completionHandler) {
    if (server != null) {
      server.close(completionHandler);
      server = null;
    } else {
      completionHandler.handle(Future.failedFuture("Not started"));
    }
  }
}
