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
package io.vertx.ext.shell.impl;

import io.vertx.core.Vertx;
import io.vertx.core.VertxException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * A simple service loader to decouple the auth providers.
 */
public interface ShellAuth {

  String provider();

  AuthProvider create(Vertx vertx, JsonObject config);

  static AuthProvider load(Vertx vertx, JsonObject config) {
    ServiceLoader<ShellAuth> loader = ServiceLoader.load(ShellAuth.class);

    Iterator<ShellAuth> factories = loader.iterator();

    while (factories.hasNext()) {
      try {
        // might fail to start (missing classes for example...
        ShellAuth auth = factories.next();
        if (auth != null) {
          if (auth.provider().equals(config.getString("provider", ""))) {
            return auth.create(vertx, config);
          }
        }
      } catch (RuntimeException e) {
        // continue...
      }
    }
    throw new VertxException("Provider not found [" + config.getString("provider", "") + "] / check your classpath");
  }
}
