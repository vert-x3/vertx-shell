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

package io.vertx.ext.shell.term;

import io.termd.core.TestBase;
import io.termd.core.telnet.TelnetHandler;
import io.termd.core.telnet.TelnetTermTest;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.ext.shell.term.impl.TelnetSocketHandler;

import java.io.Closeable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * See <a href="http://commons.apache.org/proper/commons-net/examples/telnet/TelnetClientExample.java>for more possibilities</a>
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxTelnetTermTest extends TelnetTermTest {

  public static final Function<Supplier<TelnetHandler>, Closeable> VERTX_SERVER = handlerFactory -> {
    Vertx vertx= Vertx.vertx();
    NetServer server = vertx.createNetServer().connectHandler(new TelnetSocketHandler(vertx, handlerFactory));
    BlockingQueue<AsyncResult<NetServer>> latch = new ArrayBlockingQueue<>(1);
    server.listen(4000, "localhost", latch::add);
    AsyncResult<NetServer> result;
    try {
      result = latch.poll(2, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw TestBase.failure(e);
    }
    if (result.failed()) {
      throw TestBase.failure(result.cause());
    }
    return () -> {
      CountDownLatch closeLatch = new CountDownLatch(1);
      vertx.close(done -> {
        closeLatch.countDown();
      });
      try {
        closeLatch.await(10, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    };
  };

  @Override
  protected Function<Supplier<TelnetHandler>, Closeable> serverFactory() {
    return VERTX_SERVER;
  }
}
