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

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.shiro.ShiroAuthOptions;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.shell.SSHTestBase;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SSHTermServerTest extends SSHTestBase {

  TermServer server;
  Handler<Term> termHandler;
  AuthProvider authProvider;

  @Override
  public void before() {
    super.before();
    termHandler = term -> term.write("% ");
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

  protected void startShell(SSHTermOptions options) throws ExecutionException, InterruptedException, TimeoutException {
    if (server != null) {
      throw new IllegalStateException();
    }
    server = TermServer.createSSHTermServer(vertx, options);
    CompletableFuture<Void> fut = new CompletableFuture<>();
    server.termHandler(termHandler);
    server.authProvider(authProvider);
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
      term.stdinHandler(s -> {
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
      term.write("hello");
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

  @Test
  public void testExternalAuthProvider(TestContext context) throws Exception {
    AtomicInteger count = new AtomicInteger();
    authProvider = (authInfo, resultHandler) -> {
      count.incrementAndGet();
      String username = authInfo.getString("username");
      String password = authInfo.getString("password");
      if (username.equals("paulo") && password.equals("anothersecret")) {
        resultHandler.handle(Future.succeededFuture(new AbstractUser() {
          @Override
          protected void doIsPermitted(String permission, Handler<AsyncResult<Boolean>> resultHandler) {
            resultHandler.handle(Future.succeededFuture(true));
          }
          @Override
          public JsonObject principal() {
            return new JsonObject().put("username", username);
          }
          @Override
          public void setAuthProvider(AuthProvider authProvider) {
          }
        }));
      } else {
        resultHandler.handle(Future.failedFuture("not authenticated"));
      }
    };
    Async async = context.async();
    termHandler = term -> {
      context.assertEquals(1, count.get());
      async.complete();
    };
    startShell(new SSHTermOptions().setPort(5000).setHost("localhost").setKeyPairOptions(
        new JksOptions().setPath("src/test/resources/server-keystore.jks").setPassword("wibble")));
    Session session = createSession("paulo", "anothersecret", false);
    session.connect();
    Channel channel = session.openChannel("shell");
    channel.connect();
  }

  @Test
  public void testExternalAuthProviderFails(TestContext context) throws Exception {
    AtomicInteger count = new AtomicInteger();
    authProvider = (authInfo, resultHandler) -> {
      count.incrementAndGet();
      resultHandler.handle(Future.failedFuture("not authenticated"));
    };
    termHandler = term -> {
      context.fail();
    };
    startShell(new SSHTermOptions().setPort(5000).setHost("localhost").setKeyPairOptions(
        new JksOptions().setPath("src/test/resources/server-keystore.jks").setPassword("wibble")));
    Session session = createSession("paulo", "anothersecret", false);
    try {
      session.connect();
      context.fail("Was not expected to login");
    } catch (JSchException e) {
      assertEquals("Auth cancel", e.getMessage());
    }
    context.assertEquals(1, count.get());
  }

  @Test
  public void testDifferentCharset(TestContext context) throws Exception {
    termHandler = term -> {
      term.write("\u20AC");
      term.close();
    };
    startShell(new SSHTermOptions().setDefaultCharset("ISO_8859_1").setPort(5000).setHost("localhost").setKeyPairOptions(
        new JksOptions().setPath("src/test/resources/server-keystore.jks").setPassword("wibble")).
        setAuthOptions(new ShiroAuthOptions().setType(ShiroAuthRealmType.PROPERTIES).setConfig(
            new JsonObject().put("properties_path", "classpath:test-auth.properties"))));
    Session session = createSession("paulo", "secret", false);
    session.connect();
    Channel channel = session.openChannel("shell");
    channel.connect();
    InputStream in = channel.getInputStream();
    int b = in.read();
    context.assertEquals(63, b);
  }

  @Test
  public void testKeymapFromFilesystem() throws Exception {
    URL url = TermServer.class.getResource(SSHTermOptions.DEFAULT_INPUTRC);
    File f = new File(url.toURI());
    termHandler = Term::close;
    startShell(new SSHTermOptions().setIntputrc(f.getAbsolutePath()).setPort(5000).setHost("localhost").setKeyPairOptions(
        new JksOptions().setPath("src/test/resources/server-keystore.jks").setPassword("wibble")).
        setAuthOptions(new ShiroAuthOptions().setType(ShiroAuthRealmType.PROPERTIES).setConfig(
            new JsonObject().put("properties_path", "classpath:test-auth.properties"))));
    Session session = createSession("paulo", "secret", false);
    session.connect();
    Channel channel = session.openChannel("shell");
    channel.connect();
  }
}
