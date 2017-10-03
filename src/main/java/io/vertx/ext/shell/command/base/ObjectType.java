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

package io.vertx.ext.shell.command.base;

import io.netty.util.internal.StringUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Base64;
import java.util.function.Function;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public enum ObjectType {

  STRING(Function.<String>identity()),
  BYTE(Byte::parseByte),
  SHORT(Short::parseShort),
  INTEGER(Integer::parseInt),
  LONG(Long::parseLong),
  FLOAT(Float::parseFloat),
  DOUBLE(Double::parseDouble),
  CHARACTER(s -> s.charAt(0)),
  BOOLEAN(Boolean::parseBoolean),
  JSON_OBJECT(JsonObject::new),
  JSON_ARRAY(JsonArray::new),
  BUFFER(Buffer::buffer),
  BASE64(s -> {
    byte[] bytes = Base64.getDecoder().decode(s);
    return Buffer.buffer(bytes);
  }),
  HEX(s -> Buffer.buffer(StringUtil.decodeHexDump(s)));

  final Function<String, ?> parser;

  ObjectType(Function<String, ?> parser) {
    this.parser = parser;
  }
}
