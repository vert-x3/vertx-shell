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
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.NoStackTraceThrowable;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.authentication.UsernamePasswordCredentials;
import io.vertx.ext.shell.SSHTestBase;
import io.vertx.ext.shell.term.impl.SSHExec;
import io.vertx.ext.shell.term.impl.SSHServer;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.After;
import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SSHServerTest extends SSHTestBase {

  TermServer server;
  Handler<Term> termHandler;
  Handler<SSHExec> execHandler;
  AuthenticationProvider authProvider;

  @Override
  public void before() {
    super.before();
    termHandler = term -> term.write("% ");
  }

  @After
  public void after() throws Exception {
    TermServer s = server;
    if (s != null) {
      server = null;
      try {
        s.close().await(20, TimeUnit.SECONDS);
      } catch (Throwable e) {
      }
    }
    super.after();
  }

  protected void startShell(SSHTermOptions options) throws TimeoutException {
    if (server != null) {
      throw new IllegalStateException();
    }
    server = TermServer.createSSHTermServer(vertx, options);
    server.termHandler(termHandler);
    ((SSHServer) server).setExecHandler(execHandler);
    server.authenticationProvider(authProvider);
    server.listen().await(20, TimeUnit.SECONDS);
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
        sb.append((char) code);
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
    authProvider = credentials -> {
      count.incrementAndGet();
      String username = ((UsernamePasswordCredentials) credentials).getUsername();
      String password = ((UsernamePasswordCredentials) credentials).getPassword();
      if (username.equals("paulo") && password.equals("anothersecret")) {
        return Future.succeededFuture(User.create(credentials.toJson()));
      } else {
        return Future.failedFuture("not authenticated");
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
    authProvider = credentials -> {
      count.incrementAndGet();
      return Future.failedFuture("not authenticated");
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
      setAuthOptions(new JsonObject()
        .put("provider", "properties")
        .put("type", "PROPERTIES")
        .put("config",
          new JsonObject().put("file", "test-auth.properties"))));
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
      setAuthOptions(new JsonObject()
        .put("provider", "properties")
        .put("type", "PROPERTIES")
        .put("config",
          new JsonObject().put("file", "test-auth.properties"))));
    Session session = createSession("paulo", "secret", false);
    session.connect();
    Channel channel = session.openChannel("shell");
    channel.connect();
  }

  @Override
  public void testExec(TestContext context) throws Exception {
    execHandler = exec -> {
      context.assertNotNull(Vertx.currentContext());
      context.assertEquals("the-command arg1 arg2", exec.command());
      exec.write("the_output");
      StringBuilder input = new StringBuilder();
      context.assertEquals(-1, exec.width());
      context.assertEquals(-1, exec.height());
      exec.stdinHandler(data -> {
        input.append(data);
        if (input.toString().equals("the_input")) {
          exec.end(2);
        }
      });
    };
    super.testExec(context);
  }
}
