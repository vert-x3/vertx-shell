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

import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.web.Router;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class HttpTermServerSubRouterTest extends HttpTermServerBase {

  public HttpTermServerSubRouterTest() {
    super("/sub");
  }

  @Override
  protected TermServer createServer(TestContext context, HttpTermOptions options) {
    HttpServer httpServer = vertx.createHttpServer(new HttpServerOptions().setPort(8080));
    Router router = Router.router(vertx);
    Router subRouter = Router.router(vertx);
    router.mountSubRouter("/sub", subRouter);
    httpServer.requestHandler(router::accept);
    Async async = context.async();
    httpServer.listen(8080, context.asyncAssertSuccess(s -> {
      async.complete();
    }));
    return TermServer.createWebTermServer(vertx, subRouter, options);
  }
}
