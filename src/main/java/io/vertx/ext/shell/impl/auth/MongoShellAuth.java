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
package io.vertx.ext.shell.impl.auth;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.mongo.*;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.shell.impl.ShellAuth;

public class MongoShellAuth implements ShellAuth {

  @Override
  public String provider() {
    return "mongo";
  }

  @Override
  public AuthenticationProvider create(Vertx vertx, JsonObject config) {
    MongoClient client;
    // move one key down
    config = config.getJsonObject("config");

    if (config.getBoolean("shared", false)) {
      String datasourceName = config.getString("datasourceName");
      if (datasourceName != null) {
        client = MongoClient.createShared(vertx, config, datasourceName);
      } else {
        client = MongoClient.createShared(vertx, config);
      }
    } else {
      client = MongoClient.create(vertx, config);
    }

    return MongoAuthentication.create(client, new MongoAuthenticationOptions(config));
  }
}
