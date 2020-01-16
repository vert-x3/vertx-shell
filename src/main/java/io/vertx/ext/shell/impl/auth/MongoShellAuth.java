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
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.mongo.MongoAuth;
import io.vertx.ext.auth.mongo.MongoAuthOptions;
import io.vertx.ext.auth.mongo.MongoAuthOptionsConverter;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.shell.impl.ShellAuth;

public class MongoShellAuth implements ShellAuth {

  @Override
  public String provider() {
    return "mongo";
  }

  @Override
  public AuthProvider create(Vertx vertx, JsonObject config) {
    final MongoAuthOptions options = new MongoAuthOptions(config);
    MongoClient client;
    if (options.getShared()) {
      String datasourceName = options.getDatasourceName();
      if (datasourceName != null) {
        client = MongoClient.createShared(vertx, options.getConfig(), datasourceName);
      } else {
        client = MongoClient.createShared(vertx, options.getConfig());
      }
    } else {
      client = MongoClient.create(vertx, options.getConfig());
    }

    JsonObject authConfig = new JsonObject();
    MongoAuthOptionsConverter.toJson(options, authConfig);
    return MongoAuth.create(client, authConfig);
  }
}
