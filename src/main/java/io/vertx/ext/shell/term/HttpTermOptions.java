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
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ClientAuth;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.core.net.PfxOptions;
import io.vertx.ext.auth.AuthOptions;
import io.vertx.ext.auth.shiro.ShiroAuthOptions;
import io.vertx.ext.shell.term.impl.HttpTermServer;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;

/**
 * The web term configuration options.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject(generateConverter = true)
public class HttpTermOptions extends HttpServerOptions {

  /**
   * @return the {@code vertxshell.js} default resource as a buffer
   */
  public static Buffer defaultVertxShellJsResource() {
    return HttpTermServer.loadResource("/io/vertx/ext/shell/vertxshell.js");
  }

  /**
   * @return the {@code term.js} default resource as a buffer
   */
  public static Buffer defaultTermJsResource() {
    return HttpTermServer.loadResource("/io/vertx/ext/shell/term.js");
  }

  /**
   * @return the {@code shell.html} default resource as a buffer
   */
  public static Buffer defaultShellHtmlResource() {
    return HttpTermServer.loadResource("/io/vertx/ext/shell/shell.html");
  }

  private static final String DEFAULT_SOCKJSPATH = "/shell/*";

  private SockJSHandlerOptions sockJSHandlerOptions;
  private AuthOptions authOptions;
  private String sockJSPath;
  private Buffer vertsShellJsResource;
  private Buffer termJsResource;
  private Buffer shellHtmlResource;

  public HttpTermOptions() {
    init();
  }

  public HttpTermOptions(JsonObject json) {
    super(json);
    HttpTermOptionsConverter.fromJson(json, this);
  }

  public HttpTermOptions(HttpTermOptions that) {
    sockJSHandlerOptions = new SockJSHandlerOptions(that.sockJSHandlerOptions);
    sockJSPath = that.sockJSPath;
    vertsShellJsResource = that.vertsShellJsResource != null ? that.vertsShellJsResource.copy() : null;
    termJsResource = that.termJsResource != null ? that.termJsResource.copy() : null;
    shellHtmlResource = that.shellHtmlResource != null ? that.shellHtmlResource.copy() : null;
  }

  private void init() {
    sockJSHandlerOptions = new SockJSHandlerOptions();
    sockJSPath = DEFAULT_SOCKJSPATH;
    vertsShellJsResource = defaultVertxShellJsResource();
    termJsResource = defaultTermJsResource();
    shellHtmlResource = defaultShellHtmlResource();
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
  public HttpTermOptions setSockJSHandlerOptions(SockJSHandlerOptions sockJSHandlerOptions) {
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
  public HttpTermOptions setSockJSPath(String sockJSPath) {
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
  public HttpTermOptions setShiroAuthOptions(ShiroAuthOptions shiroAuthOptions) {
    this.authOptions = shiroAuthOptions;
    return this;
  }

  @Override
  public HttpTermOptions setSendBufferSize(int sendBufferSize) {
    return (HttpTermOptions) super.setSendBufferSize(sendBufferSize);
  }

  @Override
  public HttpTermOptions setReceiveBufferSize(int receiveBufferSize) {
    return (HttpTermOptions) super.setReceiveBufferSize(receiveBufferSize);
  }

  @Override
  public HttpTermOptions setReuseAddress(boolean reuseAddress) {
    return (HttpTermOptions) super.setReuseAddress(reuseAddress);
  }

  @Override
  public HttpTermOptions setTrafficClass(int trafficClass) {
    return (HttpTermOptions) super.setTrafficClass(trafficClass);
  }

  @Override
  public HttpTermOptions setTcpNoDelay(boolean tcpNoDelay) {
    return (HttpTermOptions) super.setTcpNoDelay(tcpNoDelay);
  }

  @Override
  public HttpTermOptions setTcpKeepAlive(boolean tcpKeepAlive) {
    return (HttpTermOptions) super.setTcpKeepAlive(tcpKeepAlive);
  }

  @Override
  public HttpTermOptions setSoLinger(int soLinger) {
    return (HttpTermOptions) super.setSoLinger(soLinger);
  }

  @Override
  public HttpTermOptions setUsePooledBuffers(boolean usePooledBuffers) {
    return (HttpTermOptions) super.setUsePooledBuffers(usePooledBuffers);
  }

  @Override
  public HttpTermOptions setIdleTimeout(int idleTimeout) {
    return (HttpTermOptions) super.setIdleTimeout(idleTimeout);
  }

  @Override
  public HttpTermOptions setSsl(boolean ssl) {
    return (HttpTermOptions) super.setSsl(ssl);
  }

  @Override
  public HttpTermOptions setKeyStoreOptions(JksOptions options) {
    return (HttpTermOptions) super.setKeyStoreOptions(options);
  }

  @Override
  public HttpTermOptions setPfxKeyCertOptions(PfxOptions options) {
    return (HttpTermOptions) super.setPfxKeyCertOptions(options);
  }

  @Override
  public HttpTermOptions setPemKeyCertOptions(PemKeyCertOptions options) {
    return (HttpTermOptions) super.setPemKeyCertOptions(options);
  }

  @Override
  public HttpTermOptions setTrustStoreOptions(JksOptions options) {
    return (HttpTermOptions) super.setTrustStoreOptions(options);
  }

  @Override
  public HttpTermOptions setPemTrustOptions(PemTrustOptions options) {
    return (HttpTermOptions) super.setPemTrustOptions(options);
  }

  @Override
  public HttpTermOptions setPfxTrustOptions(PfxOptions options) {
    return (HttpTermOptions) super.setPfxTrustOptions(options);
  }

  @Override
  public HttpTermOptions addEnabledCipherSuite(String suite) {
    return (HttpTermOptions) super.addEnabledCipherSuite(suite);
  }

  @Override
  public HttpTermOptions addCrlPath(String crlPath) throws NullPointerException {
    return (HttpTermOptions) super.addCrlPath(crlPath);
  }

  @Override
  public HttpTermOptions addCrlValue(Buffer crlValue) throws NullPointerException {
    return (HttpTermOptions) super.addCrlValue(crlValue);
  }

  @Override
  public HttpTermOptions setAcceptBacklog(int acceptBacklog) {
    return (HttpTermOptions) super.setAcceptBacklog(acceptBacklog);
  }

  @Override
  public HttpTermOptions setPort(int port) {
    return (HttpTermOptions) super.setPort(port);
  }

  @Override
  public HttpTermOptions setHost(String host) {
    return (HttpTermOptions) super.setHost(host);
  }

  @Override
  public HttpTermOptions setClientAuth(ClientAuth clientAuth) {
    return (HttpTermOptions) super.setClientAuth(clientAuth);
  }

  @Override
  public HttpTermOptions setCompressionSupported(boolean compressionSupported) {
    return (HttpTermOptions) super.setCompressionSupported(compressionSupported);
  }

  @Override
  public HttpTermOptions setHandle100ContinueAutomatically(boolean handle100ContinueAutomatically) {
    return (HttpTermOptions) super.setHandle100ContinueAutomatically(handle100ContinueAutomatically);
  }

  /**
   * @return the {@code vertxshell.js} resource for this server
   */
  public Buffer getVertsShellJsResource() {
    return vertsShellJsResource;
  }

  /**
   * Set {@code vertxshell.js} resource to use.
   *
   * @param vertsShellJsResource the resource
   * @return a reference to this, so the API can be used fluently
   */
  public HttpTermOptions setVertsShellJsResource(Buffer vertsShellJsResource) {
    this.vertsShellJsResource = vertsShellJsResource;
    return this;
  }

  /**
   * @return the {@code term.js} resource for this server
   */
  public Buffer getTermJsResource() {
    return termJsResource;
  }

  /**
   * Set {@code term.js} resource to use.
   *
   * @param termJsResource the resource
   * @return a reference to this, so the API can be used fluently
   */
  public HttpTermOptions setTermJsResource(Buffer termJsResource) {
    this.termJsResource = termJsResource;
    return this;
  }

  /**
   * @return the {@code shell.html} resource for this server
   */
  public Buffer getShellHtmlResource() {
    return shellHtmlResource;
  }

  /**
   * Set {@code shell.html} resource to use.
   *
   * @param shellHtmlResource the resource
   * @return a reference to this, so the API can be used fluently
   */
  public HttpTermOptions setShellHtmlResource(Buffer shellHtmlResource) {
    this.shellHtmlResource = shellHtmlResource;
    return this;
  }
}
