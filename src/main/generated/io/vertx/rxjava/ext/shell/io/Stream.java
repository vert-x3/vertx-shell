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

package io.vertx.rxjava.ext.shell.io;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.core.json.JsonObject;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.io.Stream original} non RX-ified interface using Vert.x codegen.
 */

public class Stream {

  final io.vertx.ext.shell.io.Stream delegate;

  public Stream(io.vertx.ext.shell.io.Stream delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static Stream ofString(Handler<String> handler) { 
    Stream ret= Stream.newInstance(io.vertx.ext.shell.io.Stream.ofString(handler));
    return ret;
  }

  public static Stream ofJson(Handler<JsonObject> handler) { 
    Stream ret= Stream.newInstance(io.vertx.ext.shell.io.Stream.ofJson(handler));
    return ret;
  }

  public Stream write(String data) { 
    this.delegate.write(data);
    return this;
  }

  public Stream write(JsonObject data) { 
    this.delegate.write(data);
    return this;
  }


  public static Stream newInstance(io.vertx.ext.shell.io.Stream arg) {
    return arg != null ? new Stream(arg) : null;
  }
}
