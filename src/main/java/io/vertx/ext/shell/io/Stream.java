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

package io.vertx.ext.shell.io;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
@FunctionalInterface
public interface Stream {

  static Stream ofString(Handler<String> handler) {
    return new Stream() {
      @Override
      public Stream write(Object data) {
        if (data instanceof String) {
          handler.handle((String) data);
        } else if (data instanceof JsonObject) {
          handler.handle(((JsonObject) data).encode());
        } else {
          handler.handle(data != null ? data.toString() : null);
        }
        return this;
      }
    };
  }

  static Stream ofJson(Handler<JsonObject> handler) {
    return new Stream() {
      @Override
      public Stream write(Object data) {
        if (data instanceof JsonObject) {
          handler.handle((JsonObject) data);
        } else {
          handler.handle(data != null ? new JsonObject(data.toString()) : null);
        }
        return this;
      }
    };
  }

  @GenIgnore
  static Stream ofObject(Handler<Object> handler) {
    return new Stream() {
      @Override
      public Stream write(Object data) {
        handler.handle(data);
        return this;
      }
    };
  }

  @Fluent
  default Stream write(String data) {
    return write((Object) data);
  }

  @Fluent
  default Stream write(JsonObject data) {
    return write((Object) data);
  }

  @GenIgnore
  Stream write(Object data);

}
