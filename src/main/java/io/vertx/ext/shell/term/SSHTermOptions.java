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
import io.vertx.core.VertxException;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.KeyCertOptions;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PfxOptions;
import io.vertx.ext.auth.AuthOptions;

import java.lang.reflect.Constructor;
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

  private String host;
  private int port;
  private KeyCertOptions keyPairOptions;
  private AuthOptions authOptions;
  private String defaultCharset;

  public SSHTermOptions() {
    host = DEFAULT_HOST;
    port = DEFAULT_PORT;
    defaultCharset = DEFAULT_DEFAULT_CHARSET;
  }

  public SSHTermOptions(SSHTermOptions that) {
    this.host = that.host;
    this.port = that.port;
    this.keyPairOptions = that.keyPairOptions != null ? that.keyPairOptions.clone() : null;
    this.authOptions = that.authOptions != null ? that.authOptions.clone() : null;
    this.defaultCharset = that.defaultCharset;
  }

  public SSHTermOptions(JsonObject json) {
    this();
    SSHTermOptionsConverter.fromJson(json, this);
    authOptions = json.getJsonObject("authOptions") != null ? createAuthOptions(json.getJsonObject("authOptions")) : null;
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
   * Set the auth options.
   *
   * @param authOptions the auth options
   * @return a reference to this, so the API can be used fluently
   */
  @GenIgnore
  public SSHTermOptions setAuthOptions(AuthOptions authOptions) {
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
   * Internal method needed to load auth options supposed by Vert.x Shell.
   *
   * Create the auth options from a json value, the implementation makes a lookup on the {@literal provider}
   * property of the json object and returns the corresponding class.
   *
   * @param json the json value
   * @return the auth provider
   */
  static AuthOptions createAuthOptions(JsonObject json) {

    String provider = json.getString("provider", "");
    String impl;
    switch (provider) {
      case "shiro":
        impl = "io.vertx.ext.auth.shiro.ShiroAuthOptions";
        break;
      case "jdbc":
        impl = "io.vertx.ext.auth.jdbc.JDBCAuthOptions";
        break;
      case "mongo":
        impl = "io.vertx.ext.auth.mongo.MongoAuthOptions";
        break;
      default:
        throw new IllegalArgumentException("Invalid auth provider: " + provider);
    }

    try {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      Class<?> optionsClass = cl.loadClass(impl);
      Constructor<?> ctor = optionsClass.getConstructor(JsonObject.class);
      return (AuthOptions) ctor.newInstance(json);
    } catch (ClassNotFoundException e) {
      throw new VertxException("Provider class not found " + impl + " / check your classpath");
    } catch(InstantiationException e) {
      throw new VertxException("Cannot create " + provider +" options", e.getCause());
    } catch (Exception e) {
      throw new VertxException("Cannot create " + provider + " options" + provider, e);
    }
  }
}
