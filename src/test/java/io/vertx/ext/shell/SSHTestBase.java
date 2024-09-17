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

import com.jcraft.jsch.*;
import io.vertx.core.Vertx;
import io.vertx.core.VertxException;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.shell.term.SSHTermOptions;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public abstract class SSHTestBase {

  protected Vertx vertx;

  @Before
  public void before() {
    vertx = Vertx.vertx();
  }

  @After
  public void after() throws Exception {
    vertx.close().await(20, TimeUnit.SECONDS);
  }

  protected abstract void startShell(SSHTermOptions options) throws ExecutionException, InterruptedException, TimeoutException;

  protected void startShell() throws Exception {
    startShell(new SSHTermOptions().setPort(5000).setHost("localhost").setKeyPairOptions(
      new JksOptions().setPath("src/test/resources/server-keystore.jks").setPassword("wibble")).
      setAuthOptions(new JsonObject()
        .put("provider", "properties")
        .put("config",
          new JsonObject().put("file", "test-auth.properties"))));
  }

  protected Session createSession(String username, String password, boolean interactive) throws Exception {
    JSch jsch = new JSch();
    Session session = jsch.getSession(username, "localhost", 5000);
    if (!interactive) {
      session.setPassword(password);
    }
    session.setUserInfo(new UserInfo() {
      @Override
      public String getPassphrase() {
        return null;
      }

      @Override
      public String getPassword() {
        return interactive ? password : null;
      }

      @Override
      public boolean promptPassword(String s) {
        return interactive;
      }

      @Override
      public boolean promptPassphrase(String s) {
        return false;
      }

      @Override
      public boolean promptYesNo(String s) {
        ;        // Accept all server keys
        return true;
      }

      @Override
      public void showMessage(String s) {
      }
    });
    return session;
  }

  @Test
  public void testAuthenticate() throws Exception {
    startShell();
    for (boolean interactive : new boolean[]{false, true}) {
      Session session = createSession("paulo", "secret", interactive);
      session.connect();
      Channel channel = session.openChannel("shell");
      channel.connect();
      InputStream in = channel.getInputStream();
      byte[] out = new byte[2];
      assertEquals(2, in.read(out));
      assertEquals("% ", new String(out));
      channel.disconnect();
      session.disconnect();
    }
  }

  @Test
  public void testAuthenticationFail() throws Exception {
    startShell();
    for (boolean interactive : new boolean[]{false, true}) {
      Session session = createSession("paulo", "secret_", interactive);
      try {
        session.connect();
        fail();
      } catch (JSchException e) {
        String msg = e.getMessage();
        assertTrue("Unexpected failure message " + msg, "Auth cancel".equals(msg) || "Auth fail".equals(msg));
      }
    }
  }

  @Test
  public void testNoAuthenticationConfigured() throws Exception {
    try {
      startShell(new SSHTermOptions().setPort(5000).setHost("localhost").setKeyPairOptions(
        new JksOptions().setPath("src/test/resources/server-keystore.jks").setPassword("wibble"))
      );
      fail();
    } catch (VertxException e) {
      assertEquals("No authenticator", e.getMessage());
    }
  }

  @Test
  public void testNoKeyPairConfigured() throws Exception {
    try {
      startShell(new SSHTermOptions().setPort(5000).setHost("localhost").
        setAuthOptions(new JsonObject()
          .put("provider", "properties")
          .put("config",
            new JsonObject().put("file", "test-auth.properties")))
      );
      fail();
    } catch (VertxException e) {
      assertEquals("No key pair store configured", e.getMessage());
    }
  }

  @Test(timeout = 5000)
  public void testExec(TestContext context) throws Exception {
    startShell();
    Session session = createSession("paulo", "secret", false);
    session.connect();
    ChannelExec channel = (ChannelExec) session.openChannel("exec");
    channel.setCommand("the-command arg1 arg2");
    channel.connect();
    InputStream in = channel.getInputStream();
    StringBuilder input = new StringBuilder();
    while (!input.toString().equals("the_output")) {
      int a = in.read();
      if (a == -1) {
        break;
      }
      input.append((char) a);
    }
    OutputStream out = channel.getOutputStream();
    out.write("the_input".getBytes());
    out.flush();
    while (channel.isConnected()) {
      Thread.sleep(1);
    }
    assertEquals(2, channel.getExitStatus());
    session.disconnect();
  }
}
