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
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxException;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.auth.shiro.ShiroAuthOptions;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.shell.term.SSHOptions;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    CountDownLatch latch = new CountDownLatch(1);
    Handler<AsyncResult<Void>> handler = ar -> {
      latch.countDown();
    };
    vertx.close(handler);
    assertTrue(latch.await(10, TimeUnit.SECONDS));
  }

  protected abstract void startShell(SSHOptions options) throws ExecutionException, InterruptedException, TimeoutException;

  protected void startShell() throws Exception {
    startShell(new SSHOptions().setPort(5000).setHost("localhost").setKeyPairOptions(
        new JksOptions().setPath("src/test/resources/server-keystore.jks").setPassword("wibble")).
        setShiroAuthOptions(new ShiroAuthOptions().setType(ShiroAuthRealmType.PROPERTIES).setConfig(
            new JsonObject().put("properties_path", "classpath:test-auth.properties"))));
  }

  protected Session createSession(String username, String password, boolean interactive) throws Exception {
    JSch jsch= new JSch();
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
      startShell(new SSHOptions().setPort(5000).setHost("localhost").setKeyPairOptions(
              new JksOptions().setPath("src/test/resources/server-keystore.jks").setPassword("wibble"))
      );
      fail();
    } catch (ExecutionException e) {
      assertTrue(e.getCause() instanceof VertxException);
      assertEquals("No authenticator", e.getCause().getMessage());
    }
  }

  @Test
  public void testNoKeyPairConfigured() throws Exception {
    try {
      startShell(new SSHOptions().setPort(5000).setHost("localhost").
              setShiroAuthOptions(new ShiroAuthOptions().setType(ShiroAuthRealmType.PROPERTIES).setConfig(
                  new JsonObject().put("properties_path", "classpath:test-auth.properties")))
      );
    } catch (ExecutionException e) {
      assertTrue(e.getCause() instanceof VertxException);
      assertEquals("No key pair store configured", e.getCause().getMessage());
    }
  }
}
