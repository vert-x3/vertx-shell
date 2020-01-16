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
import io.vertx.ext.auth.jdbc.JDBCAuth;
import io.vertx.ext.auth.jdbc.JDBCAuthOptions;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.shell.impl.ShellAuth;

public class JDBCShellAuth implements ShellAuth {

  @Override
  public String provider() {
    return "jdbc";
  }

  @Override
  public AuthProvider create(Vertx vertx, JsonObject config) {
    final JDBCAuthOptions options = new JDBCAuthOptions(config);
    final JDBCClient client;

    if (options.isShared()) {
      String datasourceName = options.getDatasourceName();
      if (datasourceName != null) {
        client = JDBCClient.createShared(vertx, options.getConfig(), datasourceName);
      } else {
        client = JDBCClient.createShared(vertx, options.getConfig());
      }
    } else {
      client = JDBCClient.create(vertx, options.getConfig());
    }

    final JDBCAuth auth = JDBCAuth.create(vertx, client);

    if (options.getAuthenticationQuery() != null) {
      auth.setAuthenticationQuery(options.getAuthenticationQuery());
    }
    if (options.getRolesQuery() != null) {
      auth.setRolesQuery(options.getRolesQuery());
    }
    if (options.getPermissionsQuery() != null) {
      auth.setPermissionsQuery(options.getPermissionsQuery());
    }
    if (options.getRolesPrefix() != null) {
      auth.setRolePrefix(options.getRolesPrefix());
    }
    return auth;
  }
}
