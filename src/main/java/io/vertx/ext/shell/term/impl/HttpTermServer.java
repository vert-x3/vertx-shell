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

import io.termd.core.tty.TtyConnection;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HttpTermServer implements TermServer {

  public static Buffer loadResource(String path) {
    URL resource = HttpTermServer.class.getResource(path);
    if (resource != null) {
      try {
        byte[] tmp = new byte[512];
        InputStream in = resource.openStream();
        Buffer buffer = Buffer.buffer();
        while (true) {
          int l = in.read(tmp);
          if (l == -1) {
            break;
          }
          buffer.appendBytes(tmp, 0, l);
        }
        return buffer;
      } catch (IOException ignore) {
      }
    }
    return null;
  }

  private final Vertx vertx;
  private HttpTermOptions options;
  private Handler<TtyConnection> handler;
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
  public TermServer connectionHandler(Handler<TtyConnection> handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public TermServer termHandler(Handler<Term> handler) {
    if (handler != null) {
      connectionHandler(new TermConnectionHandler(handler));
    } else {
      connectionHandler(null);
    }
    return this;
  }

  @Override
  public TermServer authProvider(AuthProvider provider) {
    authProvider = provider;
    return this;
  }

  @Override
  public TermServer listen(Handler<AsyncResult<TermServer>> listenHandler) {

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
      SockJSHandler sockJSHandler = SockJSHandler.create(vertx, options.getSockJSHandlerOptions());
      sockJSHandler.socketHandler(new SockJSTermHandlerImpl(vertx, charset).connectionHandler(handler::handle));
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
      server.requestHandler(router::accept);
      server.listen(ar -> {
        if (ar.succeeded()) {
          listenHandler.handle(Future.succeededFuture(this));
        } else {
          listenHandler.handle(Future.failedFuture(ar.cause()));
        }
      });
    } else {
      listenHandler.handle(Future.succeededFuture(this));
    }
    return this;
  }

  @Override
  public int actualPort() {
    return -1;
  }

  @Override
  public void close() {
    close(ar -> {});
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
