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
import io.vertx.ext.auth.AuthOptions;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.shell.term.SockJSTermHandler;
import io.vertx.ext.shell.term.Term;
import io.vertx.ext.shell.term.TermServer;
import io.vertx.ext.shell.term.WebTermOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.BasicAuthHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.UserSessionHandler;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class WebTermServer implements TermServer {

  public static Buffer loadResource(String path) {
    URL resource = WebTermServer.class.getResource(path);
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

  // Load resources
  private Buffer termHtml;
  private Buffer termJs;
  private Buffer vertxTermJs;

  private final Vertx vertx;
  private final WebTermOptions options;
  private Consumer<TtyConnection> handler;
  private HttpServer server;
  private Router router;

  public WebTermServer(Vertx vertx, WebTermOptions options) {
    this.termHtml = SockJSTermHandler.defaultTermMarkupResource();
    this.termJs = SockJSTermHandler.defaultTermScriptResource();
    this.vertxTermJs = SockJSTermHandler.defaultVertxTermScriptResource();
    this.vertx = vertx;
    this.options = options;
  }

  public WebTermServer(Vertx vertx, Router router, WebTermOptions options) {
    this.termHtml = SockJSTermHandler.defaultTermMarkupResource();
    this.termJs = SockJSTermHandler.defaultTermScriptResource();
    this.vertxTermJs = SockJSTermHandler.defaultVertxTermScriptResource();
    this.vertx = vertx;
    this.options = options;
    this.router = router;
  }

  public Consumer<TtyConnection> getHandler() {
    return handler;
  }

  public WebTermServer setHandler(Consumer<TtyConnection> handler) {
    this.handler = handler;
    return this;
  }

  @Override
  public TermServer termHandler(Handler<Term> handler) {
    if (handler != null) {
      setHandler(new TermConnectionHandler(handler));
    } else {
      setHandler(null);
    }
    return this;
  }

  @Override
  public TermServer listen(Handler<AsyncResult<TermServer>> listenHandler) {

    boolean createServer = false;
    if (router == null) {
      createServer = true;
      router = Router.router(vertx);
    }

    //
    AuthProvider authProvider = Helper.toAuthProvider(vertx, options.getAuthOptions());
    if (authProvider != null) {
      AuthHandler basicAuthHandler = BasicAuthHandler.create(authProvider);
      router.route(options.getSockJSPath()).handler(basicAuthHandler);
    }

    router.get("/vertxterm.js").handler(ctx -> ctx.response().putHeader("Content-Type", "application/javascript").end(vertxTermJs));
    router.get("/term.js").handler(ctx -> ctx.response().putHeader("Content-Type", "application/javascript").end(termJs));
    router.get("/term.html").handler(ctx -> ctx.response().putHeader("Content-Type", "text/html").end(termHtml));

    SockJSHandler sockJSHandler = SockJSHandler.create(vertx, options.getSockJSHandlerOptions());
    sockJSHandler.socketHandler(new SockJSTermHandlerImpl(vertx).handler(handler));
    router.route(options.getSockJSPath()).handler(sockJSHandler);

    //
    if (createServer) {
      server = vertx.createHttpServer(options.getHttpServerOptions());
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
  public void close(Handler<AsyncResult<Void>> handler) {
    if (server != null) {
      server.close(handler);
    }
  }
}
