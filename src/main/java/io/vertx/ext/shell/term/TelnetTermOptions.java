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
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.core.net.PfxOptions;

import java.nio.charset.StandardCharsets;

/**
 * Telnet options configuration, extends {@link io.vertx.core.net.NetServerOptions}.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject()
public class TelnetTermOptions extends NetServerOptions {

  public static final boolean DEFAULT_IN_BINARY = true;
  public static final boolean DEFAULT_OUT_BINARY = true;
  public static final String DEFAULT_CHARSET = StandardCharsets.UTF_8.name();

  private boolean outBinary;
  private boolean inBinary;
  private String charset;

  public TelnetTermOptions() {
    this.outBinary = DEFAULT_OUT_BINARY;
    this.inBinary = DEFAULT_IN_BINARY;
    this.charset = DEFAULT_CHARSET;
  }

  public TelnetTermOptions(TelnetTermOptions other) {
    super(other);
    outBinary = other.outBinary;
    inBinary = other.inBinary;
    charset = other.charset;
  }

  public TelnetTermOptions(JsonObject json) {
    super(json);
  }

  @Override
  public TelnetTermOptions setSendBufferSize(int sendBufferSize) {
    return (TelnetTermOptions) super.setSendBufferSize(sendBufferSize);
  }

  @Override
  public TelnetTermOptions setReceiveBufferSize(int receiveBufferSize) {
    return (TelnetTermOptions) super.setReceiveBufferSize(receiveBufferSize);
  }

  @Override
  public TelnetTermOptions setReuseAddress(boolean reuseAddress) {
    return (TelnetTermOptions) super.setReuseAddress(reuseAddress);
  }

  @Override
  public TelnetTermOptions setTrafficClass(int trafficClass) {
    return (TelnetTermOptions) super.setTrafficClass(trafficClass);
  }

  @Override
  public TelnetTermOptions setTcpNoDelay(boolean tcpNoDelay) {
    return (TelnetTermOptions) super.setTcpNoDelay(tcpNoDelay);
  }

  @Override
  public TelnetTermOptions setTcpKeepAlive(boolean tcpKeepAlive) {
    return (TelnetTermOptions) super.setTcpKeepAlive(tcpKeepAlive);
  }

  @Override
  public TelnetTermOptions setSoLinger(int soLinger) {
    return (TelnetTermOptions) super.setSoLinger(soLinger);
  }

  @Override
  public TelnetTermOptions setUsePooledBuffers(boolean usePooledBuffers) {
    return (TelnetTermOptions) super.setUsePooledBuffers(usePooledBuffers);
  }

  @Override
  public TelnetTermOptions setIdleTimeout(int idleTimeout) {
    return (TelnetTermOptions) super.setIdleTimeout(idleTimeout);
  }

  @Override
  public TelnetTermOptions setSsl(boolean ssl) {
    return (TelnetTermOptions) super.setSsl(ssl);
  }

  @Override
  public TelnetTermOptions setKeyStoreOptions(JksOptions options) {
    return (TelnetTermOptions) super.setKeyStoreOptions(options);
  }

  @Override
  public TelnetTermOptions setPfxKeyCertOptions(PfxOptions options) {
    return (TelnetTermOptions) super.setPfxKeyCertOptions(options);
  }

  @Override
  public TelnetTermOptions setPemKeyCertOptions(PemKeyCertOptions options) {
    return (TelnetTermOptions) super.setPemKeyCertOptions(options);
  }

  @Override
  public TelnetTermOptions setTrustStoreOptions(JksOptions options) {
    return (TelnetTermOptions) super.setTrustStoreOptions(options);
  }

  @Override
  public TelnetTermOptions setPfxTrustOptions(PfxOptions options) {
    return (TelnetTermOptions) super.setPfxTrustOptions(options);
  }

  @Override
  public TelnetTermOptions setPemTrustOptions(PemTrustOptions options) {
    return (TelnetTermOptions) super.setPemTrustOptions(options);
  }

  @Override
  public TelnetTermOptions addEnabledCipherSuite(String suite) {
    return (TelnetTermOptions) super.addEnabledCipherSuite(suite);
  }

  @Override
  public TelnetTermOptions addCrlPath(String crlPath) throws NullPointerException {
    return (TelnetTermOptions) super.addCrlPath(crlPath);
  }

  @Override
  public TelnetTermOptions addCrlValue(Buffer crlValue) throws NullPointerException {
    return (TelnetTermOptions) super.addCrlValue(crlValue);
  }

  @Override
  public TelnetTermOptions setAcceptBacklog(int acceptBacklog) {
    return (TelnetTermOptions) super.setAcceptBacklog(acceptBacklog);
  }

  @Override
  public TelnetTermOptions setHost(String host) {
    return (TelnetTermOptions) super.setHost(host);
  }

  @Override
  public TelnetTermOptions setPort(int port) {
    return (TelnetTermOptions) super.setPort(port);
  }

  @Override
  public TelnetTermOptions setClientAuthRequired(boolean clientAuthRequired) {
    return (TelnetTermOptions) super.setClientAuthRequired(clientAuthRequired);
  }

  public boolean getOutBinary() {
    return outBinary;
  }

  /**
   * Set the telnet connection to negociate binary data format when sending to the client, the default value is true. This
   * allows to send data in 8 bit format and thus charset like UTF-8.
   *
   * @param outBinary the out binary value
   * @return a reference to this, so the API can be used fluently
   */
  public TelnetTermOptions setOutBinary(boolean outBinary) {
    this.outBinary = outBinary;
    return this;
  }

  public boolean getInBinary() {
    return inBinary;
  }

  /**
   * Set the telnet connection to negociate binary data format when receiving from the client, the default value is true. This
   * allows to send data in 8 bit format and thus charset like UTF-8.
   *
   * @param inBinary the in binary value
   * @return a reference to this, so the API can be used fluently
   */
  public TelnetTermOptions setInBinary(boolean inBinary) {
    this.inBinary = inBinary;
    return this;
  }

  public String getCharset() {
    return charset;
  }

  /**
   * Set the charset to use when binary mode is active, see {@link #setInBinary(boolean)} and {@link #setOutBinary(boolean)}.
   *
   * @param charset the charset
   * @return a reference to this, so the API can be used fluently
   */
  public TelnetTermOptions setCharset(String charset) {
    this.charset = charset;
    return this;
  }
}
