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

package io.vertx.ext.shell;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.Session;
import io.vertx.core.Handler;
import io.vertx.ext.shell.term.SSHOptions;
import io.vertx.ext.shell.term.TermServer;
import io.vertx.ext.shell.term.Term;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.After;
import org.junit.Test;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SSHServerTest extends SSHTestBase {

  TermServer server;
  Handler<Term> termHandler;

  @Override
  public void before() {
    super.before();
    termHandler = term -> term.stdout().write("% ");
  }

  @After
  public void after() throws Exception {
    if (server != null) {
      CountDownLatch latch = new CountDownLatch(1);
      server.close(ar -> latch.countDown());
      assertTrue(latch.await(10, TimeUnit.SECONDS));
    }
    super.after();
  }

  protected void startShell(SSHOptions options) throws ExecutionException, InterruptedException, TimeoutException {
    if (server != null) {
      throw new IllegalStateException();
    }
    server = TermServer.createSSHServer(vertx, options);
    CompletableFuture<Void> fut = new CompletableFuture<>();
    server.termHandler(termHandler);
    server.listen(ar -> {
      if (ar.succeeded()) {
        fut.complete(null);
      } else {
        fut.completeExceptionally(ar.cause());
      }
    });
    fut.get(10, TimeUnit.SECONDS);
  }

  @Test
  public void testRead(TestContext context) throws Exception {
    Async async = context.async();
    termHandler = term -> {
      term.setStdin(s -> {
        context.assertEquals("hello", s);
        async.complete();
      });
    };
    startShell();
    Session session = createSession("paulo", "secret", false);
    session.connect();
    Channel channel = session.openChannel("shell");
    channel.connect();
    OutputStream out = channel.getOutputStream();
    out.write("hello".getBytes());
    out.flush();
    channel.disconnect();
    session.disconnect();
  }

  @Test
  public void testWrite() throws Exception {
    termHandler = term -> {
      term.stdout().write("hello");
    };
    startShell();
    Session session = createSession("paulo", "secret", false);
    session.connect();
    Channel channel = session.openChannel("shell");
    channel.connect();
    Reader in = new InputStreamReader(channel.getInputStream());
    int count = 5;
    StringBuilder sb = new StringBuilder();
    while (count > 0) {
      int code = in.read();
      if (code == -1) {
        count = 0;
      } else {
        count--;
        sb.append((char)code);
      }
    }
    assertEquals("hello", sb.toString());
    channel.disconnect();
    session.disconnect();
  }

  @Test
  public void testResizeHandler(TestContext context) throws Exception {
    Async async = context.async();
    termHandler = term -> {
      term.resizehandler(v -> {
        context.assertEquals(20, term.width());
        context.assertEquals(10, term.height());
        async.complete();
      });
    };
    startShell();
    Session session = createSession("paulo", "secret", false);
    session.connect();
    ChannelShell channel = (ChannelShell) session.openChannel("shell");
    channel.connect();
    OutputStream out = channel.getOutputStream();
    channel.setPtySize(20, 10, 20 * 8, 10 * 8);
    out.flush();
    channel.disconnect();
    session.disconnect();
  }

  @Test
  public void testCloseHandler(TestContext context) throws Exception {
    Async async = context.async();
    termHandler = term -> {
      term.closeHandler(v -> {
        async.complete();
      });
    };
    startShell();
    Session session = createSession("paulo", "secret", false);
    session.connect();
    Channel channel = session.openChannel("shell");
    channel.connect();
    channel.disconnect();
    session.disconnect();
  }

  @Test
  public void testClose(TestContext context) throws Exception {
    testClose(context, term -> {
      vertx.setTimer(10, id -> {
        term.close();
      });
    });
  }

  @Test
  public void testCloseImmediatly(TestContext context) throws Exception {
    testClose(context, Term::close);
  }

  private void testClose(TestContext context, Consumer<Term> closer) throws Exception {
    Async async = context.async();
    termHandler = term -> {
      term.closeHandler(v -> {
        async.complete();
      });
      closer.accept(term);
    };
    startShell();
    Session session = createSession("paulo", "secret", false);
    session.connect();
    Channel channel = session.openChannel("shell");
    channel.connect();
    while (channel.isClosed()) {
      Thread.sleep(10);
    }
  }

  @Test
  public void testType(TestContext context) throws Exception {
    Async async = context.async();
    termHandler = term -> {
      context.assertEquals("vt100", term.type());
      async.complete();
    };
    startShell();
    Session session = createSession("paulo", "secret", false);
    session.connect();
    Channel channel = session.openChannel("shell");
    channel.connect();
  }
}
