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

package io.vertx.ext.shell.term.impl;

import io.termd.core.readline.Keymap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.ext.shell.term.TelnetTermOptions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Helper {

  public static Keymap defaultKeymap() {
    Buffer buffer = Helper.loadResource(TelnetTermOptions.DEFAULT_INPUTRC);
    return new Keymap(new ByteArrayInputStream(buffer.getBytes()));
  }

  public static Buffer loadResource(FileSystem fs, String path) {
    try {
      return fs.readFileBlocking(path);
    } catch (Exception e) {
      return loadResource(path);
    }
  }

  public static Buffer loadResource(String path) {
    URL resource = HttpTermServer.class.getResource(path);
    if (resource != null) {
      try {
        byte[] tmp = new byte[512];
        InputStream in = resource.openStream();
        Buffer buffer = Buffer.buffer();
        while (true) {
          int l = in.read(tmp);
          if (l == -1) {
            break;
          }
          buffer.appendBytes(tmp, 0, l);
        }
        return buffer;
      } catch (IOException ignore) {
      }
    }
    return null;
  }
}
