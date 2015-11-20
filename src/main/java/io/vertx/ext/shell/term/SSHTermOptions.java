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
import io.vertx.ext.auth.AuthOptions;
import io.vertx.ext.auth.shiro.ShiroAuthOptions;

/**
 * The SSH term configuration options.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject(generateConverter = true)
public class SSHTermOptions {

  private static final String DEFAULT_HOST = NetServerOptions.DEFAULT_HOST;
  private static final int DEFAULT_PORT = NetServerOptions.DEFAULT_PORT;

  private String host;
  private int port;
  private KeyCertOptions keyPairOptions;
  private AuthOptions authOptions;

  public SSHTermOptions() {
    host = DEFAULT_HOST;
    port = DEFAULT_PORT;
  }

  public SSHTermOptions(SSHTermOptions that) {
    this.host = that.host;
    this.port = that.port;
    this.keyPairOptions = that.keyPairOptions != null ? that.keyPairOptions.clone() : null;
    this.authOptions = that.authOptions != null ? that.authOptions.clone() : null;
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
  public AuthOptions getAuthOptions() {
    return authOptions;
  }

  /**
   * Set the auth options as a Shiro auth.
   *
   * @param shiroAuthOptions the Shiro auth options
   * @return a reference to this, so the API can be used fluently
   */
  public SSHTermOptions setShiroAuthOptions(ShiroAuthOptions shiroAuthOptions) {
    this.authOptions = shiroAuthOptions;
    return this;
  }
}
