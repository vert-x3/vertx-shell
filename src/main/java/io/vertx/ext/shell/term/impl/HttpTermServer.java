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
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.shell.impl.ShellAuth;
import io.vertx.ext.shell.term.HttpTermOptions;
import io.vertx.ext.shell.term.Term;
import io.vertx.ext.shell.term.TermServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.AuthenticationHandler;
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
  private AuthenticationProvider authProvider;

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
  public TermServer authenticationProvider(AuthenticationProvider provider) {
    authProvider = provider;
    return this;
  }

  public TermServer listen(Completable<Void> listenHandler) {

    Charset charset = Charset.forName(options.getCharset());

    boolean createServer = false;
    if (router == null) {
      createServer = true;
      router = Router.router(vertx);
    }

    if (options.getAuthOptions() != null) {
      authProvider = ShellAuth.load(vertx, options.getAuthOptions());
    }

    if (options.getSockJSPath() != null && options.getSockJSHandlerOptions() != null) {
      if (authProvider != null) {
        AuthenticationHandler basicAuthHandler = BasicAuthHandler.create(authProvider);
        router.route(options.getSockJSPath()).handler(basicAuthHandler);
      }

      Buffer inputrc = Helper.loadResource(vertx.fileSystem(), options.getIntputrc());
      if (inputrc == null) {
        if (listenHandler != null) {
          listenHandler.fail("Could not load inputrc from " + options.getIntputrc());
        }
        return this;
      }
      Keymap keymap = new Keymap(new ByteArrayInputStream(inputrc.getBytes()));
      router.route(options.getSockJSPath())
        .subRouter(
          SockJSHandler.create(vertx, options.getSockJSHandlerOptions())
            .socketHandler(
              new SockJSTermHandlerImpl(vertx, charset, keymap)
                .termHandler(termHandler)));
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
      server.listen()
        .onComplete(ar -> {
          if (listenHandler != null) {
            if (ar.succeeded()) {
              listenHandler.succeed();
            } else {
              listenHandler.fail(ar.cause());
            }
          }
        });
    } else {
      if (listenHandler != null) {
        listenHandler.succeed();
      }
    }
    return this;
  }

  @Override
  public int actualPort() {
    return server.actualPort();
  }

  @Override
  public Future<Void> listen() {
    return Future.future(this::listen);
  }

  @Override
  public Future<Void> close() {
    return Future.future(this::close);
  }

  public void close(Completable<Void> completionHandler) {
    if (server != null) {
      server.close()
        .onComplete(completionHandler);
      server = null;
    } else {
      completionHandler.fail("Not started");
    }
  }
}
