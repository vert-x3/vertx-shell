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
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.KeyCertOptions;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PfxOptions;

import java.nio.charset.StandardCharsets;

/**
 * The SSH term configuration options.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject(generateConverter = true)
public class SSHTermOptions {

  public static final String DEFAULT_HOST = NetServerOptions.DEFAULT_HOST;
  public static final int DEFAULT_PORT = NetServerOptions.DEFAULT_PORT;
  public static final String DEFAULT_DEFAULT_CHARSET = StandardCharsets.UTF_8.name();
  public static final String DEFAULT_INPUTRC = "/io/vertx/ext/shell/inputrc";

  private String host;
  private int port;
  private KeyCertOptions keyPairOptions;
  private JsonObject authOptions;
  private String defaultCharset;
  private String intputrc;

  public SSHTermOptions() {
    host = DEFAULT_HOST;
    port = DEFAULT_PORT;
    defaultCharset = DEFAULT_DEFAULT_CHARSET;
    intputrc = DEFAULT_INPUTRC;
  }

  public SSHTermOptions(SSHTermOptions that) {
    this.host = that.host;
    this.port = that.port;
    this.keyPairOptions = that.keyPairOptions != null ? that.keyPairOptions.copy() : null;
    this.authOptions = that.authOptions != null ? that.authOptions.copy() : null;
    this.defaultCharset = that.defaultCharset;
    this.intputrc = that.intputrc;
  }

  public SSHTermOptions(JsonObject json) {
    this();
    SSHTermOptionsConverter.fromJson(json, this);
  }

  /**
   * @return the host
   */
  public String getHost() {
    return host;
  }

  /**
   * Set the host
   * @param host the host
   * @return a reference to this, so the API can be used fluently
   */
  public SSHTermOptions setHost(String host) {
    this.host = host;
    return this;
  }

  /**
   * @return the port
   */
  public int getPort() {
    return port;
  }

  /**
   * Set the port
   * @param port the port
   * @return a reference to this, so the API can be used fluently
   */
  public SSHTermOptions setPort(int port) {
    this.port = port;
    return this;
  }

  /**
   * @return the key pair options
   */
  @GenIgnore
  public KeyCertOptions getKeyPairOptions() {
    return keyPairOptions;
  }

  /**
   * Set the key pair options in jks format, aka Java keystore.
   * @param options the key store in jks format
   * @return a reference to this, so the API can be used fluently
   */
  public SSHTermOptions setKeyPairOptions(JksOptions options) {
    this.keyPairOptions = options;
    return this;
  }

  /**
   * Set the key pair options in pfx format.
   * @param options the key cert options in pfx format
   * @return a reference to this, so the API can be used fluently
   */
  public SSHTermOptions setPfxKeyPairOptions(PfxOptions options) {
    this.keyPairOptions = options;
    return this;
  }

  /**
   * Set the key pair store options in pem format.
   * @param options the options in pem format
   * @return a reference to this, so the API can be used fluently
   */
  public SSHTermOptions setPemKeyPairOptions(PemKeyCertOptions options) {
    this.keyPairOptions = options;
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
  public SSHTermOptions setAuthOptions(JsonObject authOptions) {
    this.authOptions = authOptions;
    return this;
  }

  public String getDefaultCharset() {
    return defaultCharset;
  }

  /**
   * Set the default charset to use when the client does not specifies one.
   *
   * @param defaultCharset the default charset
   * @return a reference to this, so the API can be used fluently
   */
  public SSHTermOptions setDefaultCharset(String defaultCharset) {
    this.defaultCharset = defaultCharset;
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
  public SSHTermOptions setIntputrc(String intputrc) {
    this.intputrc = intputrc;
    return this;
  }
}
