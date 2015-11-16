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

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class WebTermOptions {

  private SockJSHandlerOptions sockJSHandlerOptions = new SockJSHandlerOptions();
  private HttpServerOptions httpServerOptions = new HttpServerOptions();

  public WebTermOptions() {
  }

  public WebTermOptions(JsonObject json) {
  }

  public WebTermOptions(WebTermOptions that) {
  }

  public SockJSHandlerOptions getSockJSHandlerOptions() {
    return sockJSHandlerOptions;
  }

  public WebTermOptions setSockJSHandlerOptions(SockJSHandlerOptions sockJSHandlerOptions) {
    this.sockJSHandlerOptions = sockJSHandlerOptions;
    return this;
  }

  public HttpServerOptions getHttpServerOptions() {
    return httpServerOptions;
  }

  public WebTermOptions setHttpServerOptions(HttpServerOptions httpServerOptions) {
    this.httpServerOptions = httpServerOptions;
    return this;
  }
}
