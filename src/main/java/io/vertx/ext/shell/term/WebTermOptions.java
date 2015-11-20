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
import io.vertx.ext.auth.AuthOptions;
import io.vertx.ext.auth.shiro.ShiroAuthOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;

/**
 * The web term configuration options.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject(generateConverter = true)
public class WebTermOptions {

  private static final String DEFAULT_SOCKJSPATH = "/term/*";

  private SockJSHandlerOptions sockJSHandlerOptions;
  private HttpServerOptions httpServerOptions;
  private AuthOptions authOptions;
  private String sockJSPath;

  public WebTermOptions() {
    sockJSHandlerOptions = new SockJSHandlerOptions();
    httpServerOptions = new HttpServerOptions();
    sockJSPath = DEFAULT_SOCKJSPATH;
  }

  public WebTermOptions(JsonObject json) {
    this();
    WebTermOptionsConverter.fromJson(json, this);
  }

  public WebTermOptions(WebTermOptions that) {
    sockJSHandlerOptions = new SockJSHandlerOptions(that.sockJSHandlerOptions);
    httpServerOptions = new HttpServerOptions(that.httpServerOptions);
    sockJSPath = that.sockJSPath;
  }

  /**
   * @return the http server options
   */
  public HttpServerOptions getHttpServerOptions() {
    return httpServerOptions;
  }

  /**
   * The http server options used when the web term bootstraps a web server.
   *
   * @param httpServerOptions the http server options
   * @return a reference to this, so the API can be used fluently
   */
  public WebTermOptions setHttpServerOptions(HttpServerOptions httpServerOptions) {
    this.httpServerOptions = httpServerOptions;
    return this;
  }

  /**
   * @return the SockJS handler options
   */
  public SockJSHandlerOptions getSockJSHandlerOptions() {
    return sockJSHandlerOptions;
  }

  /**
   * The SockJS handler options.
   *
   * @param sockJSHandlerOptions the options to use
   * @return a reference to this, so the API can be used fluently
   */
  public WebTermOptions setSockJSHandlerOptions(SockJSHandlerOptions sockJSHandlerOptions) {
    this.sockJSHandlerOptions = sockJSHandlerOptions;
    return this;
  }

  /**
   * @return the SockJS path
   */
  public String getSockJSPath() {
    return sockJSPath;
  }

  /**
   * Configure the SockJS path, the default value is {@code /term/*}.
   *
   * @param sockJSPath the new SockJS path
   * @return a reference to this, so the API can be used fluently
   */
  public WebTermOptions setSockJSPath(String sockJSPath) {
    this.sockJSPath = sockJSPath;
    return this;
  }

  /**
   * @return the auth options
   */
  public AuthOptions getAuthOptions() {
    return authOptions;
  }

  /**
   * Set the auth options as a Shiro auth.
   *
   * @param shiroAuthOptions the Shiro auth options
   * @return a reference to this, so the API can be used fluently
   */
  public WebTermOptions setShiroAuthOptions(ShiroAuthOptions shiroAuthOptions) {
    this.authOptions = shiroAuthOptions;
    return this;
  }
}
