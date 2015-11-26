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

package io.vertx.ext.shell;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.shell.term.SSHTermOptions;
import io.vertx.ext.shell.term.TelnetTermOptions;
import io.vertx.ext.shell.term.HttpTermOptions;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * The configurations options for the shell service, the shell connectors can be configured
 * with {@link TelnetTermOptions}, {@link SSHTermOptions} and {@link HttpTermOptions}.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject(generateConverter = true)
public class ShellServiceOptions extends ShellServerOptions {

  private TelnetTermOptions telnetOptions;
  private SSHTermOptions sshOptions;
  private HttpTermOptions httpOptions;

  public ShellServiceOptions() {
  }

  public ShellServiceOptions(ShellServiceOptions that) {
    super(that);
    this.telnetOptions = that.telnetOptions != null ? new TelnetTermOptions(that.telnetOptions) : null;
    this.sshOptions = that.sshOptions != null ? new SSHTermOptions(that.sshOptions) : null;
    this.httpOptions = that.httpOptions != null ? new HttpTermOptions(that.httpOptions) : null;
  }

  public ShellServiceOptions(JsonObject json) {
    super(json);
    ShellServiceOptionsConverter.fromJson(json, this);
  }

  @Override
  public ShellServiceOptions setWelcomeMessage(String welcomeMessage) {
    return (ShellServiceOptions) super.setWelcomeMessage(welcomeMessage);
  }

  /**
   * @return the Telnet options
   */
  public TelnetTermOptions getTelnetOptions() {
    return telnetOptions;
  }

  /**
   * Set the Telnet options, if the option is null, Telnet will not be started.
   *
   * @param telnetOptions the ssh options
   * @return a reference to this, so the API can be used fluently
   */
  public ShellServiceOptions setTelnetOptions(TelnetTermOptions telnetOptions) {
    this.telnetOptions = telnetOptions;
    return this;
  }

  /**
   * @return the SSH options
   */
  public SSHTermOptions getSSHOptions() {
    return sshOptions;
  }

  /**
   * Set the SSH options, if the option is null, SSH will not be started.
   *
   * @param sshOptions the ssh options
   * @return a reference to this, so the API can be used fluently
   */
  public ShellServiceOptions setSSHOptions(SSHTermOptions sshOptions) {
    this.sshOptions = sshOptions;
    return this;
  }

  public HttpTermOptions getHttpOptions() {
    return httpOptions;
  }

  public ShellServiceOptions setHttpOptions(HttpTermOptions httpOptions) {
    this.httpOptions = httpOptions;
    return this;
  }
}
