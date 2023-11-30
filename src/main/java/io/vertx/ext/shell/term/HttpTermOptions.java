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
import io.vertx.codegen.json.annotations.JsonGen;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ClientAuth;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.core.net.PfxOptions;
import io.vertx.ext.shell.term.impl.Helper;
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions;

import java.nio.charset.StandardCharsets;

/**
 * The web term configuration options.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
@JsonGen(publicConverter = false)
public class HttpTermOptions extends HttpServerOptions {

  public static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.name();
  public static final String DEFAULT_INPUTRC = "/io/vertx/ext/shell/inputrc";

  /**
   * @return the {@code vertxshell.js} default resource as a buffer
   */
  public static Buffer defaultVertxShellJsResource() {
    return Helper.loadResource("/io/vertx/ext/shell/vertxshell.js");
  }

  /**
   * @return the {@code term.js} default resource as a buffer
   */
  public static Buffer defaultTermJsResource() {
    return Helper.loadResource("/io/vertx/ext/shell/term.js");
  }

  /**
   * @return the {@code shell.html} default resource as a buffer
   */
  public static Buffer defaultShellHtmlResource() {
    return Helper.loadResource("/io/vertx/ext/shell/shell.html");
  }

  private static final String DEFAULT_SOCKJSPATH = "/shell/*";

  private SockJSHandlerOptions sockJSHandlerOptions;
  private JsonObject authOptions;
  private String sockJSPath;
  private Buffer vertsShellJsResource;
  private Buffer termJsResource;
  private Buffer shellHtmlResource;
  private String charset;
  private String intputrc;

  public HttpTermOptions() {
    init();
  }

  public HttpTermOptions(JsonObject json) {
    super(json);
    init();
    HttpTermOptionsConverter.fromJson(json, this);
  }

  public HttpTermOptions(HttpTermOptions that) {
    sockJSHandlerOptions = new SockJSHandlerOptions(that.sockJSHandlerOptions);
    sockJSPath = that.sockJSPath;
    vertsShellJsResource = that.vertsShellJsResource != null ? that.vertsShellJsResource.copy() : null;
    termJsResource = that.termJsResource != null ? that.termJsResource.copy() : null;
    shellHtmlResource = that.shellHtmlResource != null ? that.shellHtmlResource.copy() : null;
    authOptions = that.authOptions != null ? that.authOptions.copy() : null;
    charset = that.charset;
    intputrc = that.intputrc;
  }

  private void init() {
    sockJSHandlerOptions = new SockJSHandlerOptions();
    sockJSPath = DEFAULT_SOCKJSPATH;
    vertsShellJsResource = defaultVertxShellJsResource();
    termJsResource = defaultTermJsResource();
    shellHtmlResource = defaultShellHtmlResource();
    charset = DEFAULT_CHARSET;
    intputrc = DEFAULT_INPUTRC;
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
  public JsonObject getAuthOptions() {
    return authOptions;
  }

  /**
   * Set the auth options.
   *
   * @param authOptions the auth options
   * @return a reference to this, so the API can be used fluently
   */
  public HttpTermOptions setAuthOptions(JsonObject authOptions) {
    this.authOptions = authOptions;
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
  public HttpTermOptions setIdleTimeout(int idleTimeout) {
    return (HttpTermOptions) super.setIdleTimeout(idleTimeout);
  }

  @Override
  public HttpTermOptions setSsl(boolean ssl) {
    return (HttpTermOptions) super.setSsl(ssl);
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

  /**
   * @return the charset used for encoding / decoding text from/to SockJS
   */
  public String getCharset() {
    return charset;
  }

  /**
   * Set the charset used for encoding / decoding text data from/to SockJS
   *
   * @param charset the charset to use
   * @return a reference to this, so the API can be used fluently
   */
  public HttpTermOptions setCharset(String charset) {
    this.charset = charset;
    return this;
  }

  /**
   * @return the current path of the inputrc config
   */
  public String getIntputrc() {
    return intputrc;
  }

  /**
   * The path of the <i>inputrc</i> config.
   *
   * @param intputrc the path of the inputrc config
   * @return a reference to this, so the API can be used fluently
   */
  public HttpTermOptions setIntputrc(String intputrc) {
    this.intputrc = intputrc;
    return this;
  }
}
