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

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.shell.ShellServiceOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.ShellServiceOptions} original class using Vert.x codegen.
 */
public class ShellServiceOptionsConverter {

  public static void fromJson(JsonObject json, ShellServiceOptions obj) {
    if (json.getValue("sshOptions") instanceof JsonObject) {
      obj.setSSHOptions(new io.vertx.ext.shell.net.SSHOptions((JsonObject)json.getValue("sshOptions")));
    }
    if (json.getValue("telnetOptions") instanceof JsonObject) {
      obj.setTelnetOptions(new io.vertx.ext.shell.net.TelnetOptions((JsonObject)json.getValue("telnetOptions")));
    }
    if (json.getValue("welcomeMessage") instanceof String) {
      obj.setWelcomeMessage((String)json.getValue("welcomeMessage"));
    }
  }

  public static void toJson(ShellServiceOptions obj, JsonObject json) {
    if (obj.getWelcomeMessage() != null) {
      json.put("welcomeMessage", obj.getWelcomeMessage());
    }
  }
}