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

package io.vertx.ext.shell.auth;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.shell.auth.ShiroAuthOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.auth.ShiroAuthOptions} original class using Vert.x codegen.
 */
public class ShiroAuthOptionsConverter {

  public static void fromJson(JsonObject json, ShiroAuthOptions obj) {
    if (json.getValue("config") instanceof JsonObject) {
      obj.setConfig(((JsonObject)json.getValue("config")).copy());
    }
    if (json.getValue("type") instanceof String) {
      obj.setType(io.vertx.ext.auth.shiro.ShiroAuthRealmType.valueOf((String)json.getValue("type")));
    }
  }

  public static void toJson(ShiroAuthOptions obj, JsonObject json) {
    if (obj.getConfig() != null) {
      json.put("config", obj.getConfig());
    }
    if (obj.getType() != null) {
      json.put("type", obj.getType().name());
    }
  }
}