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
import io.vertx.ext.shell.term.SSHOptions;
import io.vertx.ext.shell.term.TelnetOptions;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * The configurations options for the shell service, the shell connectors can be configured
 * with {@link io.vertx.ext.shell.term.TelnetOptions} and {@link io.vertx.ext.shell.term.SSHOptions}.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject(generateConverter = true)
public class ShellServiceOptions {

  public static final String DEFAULT_WELCOME_MESSAGE;

  static {
    String welcome = "Welcome to Vert.x Shell";
    InputStream resource = ShellServiceOptions.class.getResourceAsStream("vertx-banner.txt");
    if (resource != null) {
      try(InputStream in = resource) {
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        byte[] buf = new byte[256];
        while (true) {
          int len = in.read(buf);
          if (len == -1) {
            break;
          }
          tmp.write(buf, 0, len);
        }
        welcome = tmp.toString();
      } catch (Exception ignore) {
        // Could not load
      }
    }
    DEFAULT_WELCOME_MESSAGE = welcome + "\n\n";
  }

  private String welcomeMessage;
  private TelnetOptions telnetOptions;
  private SSHOptions sshOptions;

  public ShellServiceOptions() {
    welcomeMessage = DEFAULT_WELCOME_MESSAGE;
  }

  public ShellServiceOptions(ShellServiceOptions that) {
    this.telnetOptions = that.telnetOptions != null ? new TelnetOptions(that.telnetOptions) : null;
    this.welcomeMessage = that.welcomeMessage;
  }

  public ShellServiceOptions(JsonObject json) {
    this();
    ShellServiceOptionsConverter.fromJson(json, this);
  }

  /**
   * @return the shell welcome message
   */
  public String getWelcomeMessage() {
    return welcomeMessage;
  }

  /**
   * Set the shell welcome message, i.e the message displayed in the user console when he connects to the shell.
   *
   * @param welcomeMessage the welcome message
   * @return a reference to this, so the API can be used fluently
   */
  public ShellServiceOptions setWelcomeMessage(String welcomeMessage) {
    this.welcomeMessage = welcomeMessage;
    return this;
  }

  /**
   * @return the Telnet options
   */
  public TelnetOptions getTelnetOptions() {
    return telnetOptions;
  }

  /**
   * Set the Telnet options, if the option is null, Telnet will not be started.
   *
   * @param telnetOptions the ssh options
   * @return a reference to this, so the API can be used fluently
   */
  public ShellServiceOptions setTelnetOptions(TelnetOptions telnetOptions) {
    this.telnetOptions = telnetOptions;
    return this;
  }

  /**
   * @return the SSH options
   */
  public SSHOptions getSSH() {
    return sshOptions;
  }

  /**
   * Set the SSH options, if the option is null, SSH will not be started.
   *
   * @param sshOptions the ssh options
   * @return a reference to this, so the API can be used fluently
   */
  public ShellServiceOptions setSSHOptions(SSHOptions sshOptions) {
    this.sshOptions = sshOptions;
    return this;
  }
}
