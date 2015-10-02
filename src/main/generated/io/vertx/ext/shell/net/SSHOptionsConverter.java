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

package io.vertx.ext.shell.net;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.shell.net.SSHOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.net.SSHOptions} original class using Vert.x codegen.
 */
public class SSHOptionsConverter {

  public static void fromJson(JsonObject json, SSHOptions obj) {
    if (json.getValue("host") instanceof String) {
      obj.setHost((String)json.getValue("host"));
    }
    if (json.getValue("keyPairOptions") instanceof JsonObject) {
      obj.setKeyPairOptions(new io.vertx.core.net.JksOptions((JsonObject)json.getValue("keyPairOptions")));
    }
    if (json.getValue("pemKeyPairOptions") instanceof JsonObject) {
      obj.setPemKeyPairOptions(new io.vertx.core.net.PemKeyCertOptions((JsonObject)json.getValue("pemKeyPairOptions")));
    }
    if (json.getValue("pfxKeyPairOptions") instanceof JsonObject) {
      obj.setPfxKeyPairOptions(new io.vertx.core.net.PfxOptions((JsonObject)json.getValue("pfxKeyPairOptions")));
    }
    if (json.getValue("port") instanceof Number) {
      obj.setPort(((Number)json.getValue("port")).intValue());
    }
    if (json.getValue("shiroAuthOptions") instanceof JsonObject) {
      obj.setShiroAuthOptions(new io.vertx.ext.shell.auth.ShiroAuthOptions((JsonObject)json.getValue("shiroAuthOptions")));
    }
  }

  public static void toJson(SSHOptions obj, JsonObject json) {
    if (obj.getHost() != null) {
      json.put("host", obj.getHost());
    }
    json.put("port", obj.getPort());
  }
}