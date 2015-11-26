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
import io.vertx.ext.shell.term.HttpTermOptions;
import io.vertx.ext.shell.term.SSHTermOptions;
import io.vertx.ext.shell.term.TelnetTermOptions;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * The configurations options for the shell server.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject(generateConverter = true)
public class ShellServerOptions {

  public static final String DEFAULT_WELCOME_MESSAGE;

  static {
    String welcome = "Welcome to Vert.x Shell";
    InputStream resource = ShellServerOptions.class.getResourceAsStream("vertx-banner.txt");
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

  public ShellServerOptions() {
    welcomeMessage = DEFAULT_WELCOME_MESSAGE;
  }

  public ShellServerOptions(ShellServerOptions that) {
    this.welcomeMessage = that.welcomeMessage;
  }

  public ShellServerOptions(JsonObject json) {
    this();
    ShellServerOptionsConverter.fromJson(json, this);
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
  public ShellServerOptions setWelcomeMessage(String welcomeMessage) {
    this.welcomeMessage = welcomeMessage;
    return this;
  }
}
