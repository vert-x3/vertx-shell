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
import com.jcraft.jsch.Session;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.mongo.MongoAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandRegistry;
import io.vertx.ext.shell.term.SSHTermOptions;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SSHShellTest extends SSHTestBase {

  ShellService service;

  @After
  public void after() throws Exception {
    if (service != null) {
      CountDownLatch latch = new CountDownLatch(1);
      Handler<AsyncResult<Void>> handler = ar -> latch.countDown();
      service.stop(handler);
      assertTrue(latch.await(10, TimeUnit.SECONDS));
    }
    super.after();
  }

  protected void startShell(SSHTermOptions options) throws ExecutionException, InterruptedException, TimeoutException {

    if (service != null) {
      throw new IllegalStateException();
    }

    service = ShellService.create(vertx, new ShellServiceOptions().
        setWelcomeMessage("").
        setSSHOptions(options));

    CompletableFuture<Void> fut = new CompletableFuture<>();
    service.start(ar -> {
      if (ar.succeeded()) {
        fut.complete(null);
      } else {
        fut.completeExceptionally(ar.cause());
      }
    });
    fut.get(10, TimeUnit.SECONDS);
  }

  @Test
  public void testDeployServiceWithShiroAuthOptions(TestContext context) throws Exception {
    Async async = context.async();
    vertx.deployVerticle("service:io.vertx.ext.shell", new DeploymentOptions().setConfig(new JsonObject().put("sshOptions",
        new JsonObject().
            put("host", "localhost").
            put("port", 5000).
            put("keyPairOptions", new JsonObject().
                put("path", "src/test/resources/server-keystore.jks").
                put("password", "wibble")).
            put("authOptions", new JsonObject().put("provider", "shiro").put("config",
                new JsonObject().
                    put("properties_path", "classpath:test-auth.properties")))))
        , context.asyncAssertSuccess(v -> {
      async.complete();
    }));
    async.awaitSuccess(2000);
    Session session = createSession("paulo", "secret", false);
    session.connect();
    Channel channel = session.openChannel("shell");
    channel.connect();
    channel.disconnect();
    session.disconnect();
  }

  protected static JsonObject config() {
    return new JsonObject()
        .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
        .put("driver_class", "org.hsqldb.jdbcDriver");
  }

  @Test
  public void testDeployServiceWithJDBCAuthOptions(TestContext context) throws Exception {
    List<String> SQL  = Arrays.asList(
        "create table user (username varchar(255), password varchar(255), password_salt varchar(255) );",
        "create table user_roles (username varchar(255), role varchar(255));",
        "insert into user values ('tim', 'EC0D6302E35B7E792DF9DA4A5FE0DB3B90FCAB65A6215215771BF96D498A01DA8234769E1CE8269A105E9112F374FDAB2158E7DA58CDC1348A732351C38E12A0', 'C59EB438D1E24CACA2B1A48BC129348589D49303858E493FBE906A9158B7D5DC');"
    );
    Connection conn = DriverManager.getConnection(config().getString("url"));
    for (String sql : SQL) {
      conn.createStatement().execute(sql);
    }
    Async async = context.async();
    vertx.deployVerticle("service:io.vertx.ext.shell", new DeploymentOptions().setConfig(new JsonObject().put("sshOptions",
        new JsonObject().
            put("host", "localhost").
            put("port", 5000).
            put("keyPairOptions", new JsonObject().
                put("path", "src/test/resources/server-keystore.jks").
                put("password", "wibble")).
            put("authOptions", new JsonObject().put("provider", "jdbc").put("config",
                new JsonObject()
                    .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
                    .put("driver_class", "org.hsqldb.jdbcDriver")))))
        , context.asyncAssertSuccess(v -> {
      async.complete();
    }));
    async.awaitSuccess(2000);
    Session session = createSession("tim", "sausages", false);
    session.connect();
    Channel channel = session.openChannel("shell");
    channel.connect();
    channel.disconnect();
    session.disconnect();
  }

  @Test
  public void testDeployServiceWithMongoAuthOptions(TestContext context) throws Exception {
    Async mongo = context.async();
    vertx.deployVerticle("service:io.vertx.vertx-mongo-embedded-db", context.asyncAssertSuccess(v -> mongo.complete()));
    mongo.awaitSuccess(120 * 1000); // Enough time for downloading the db
    JsonObject config = new JsonObject().put("connection_string", "mongodb://localhost:27018");
    MongoClient client = MongoClient.createNonShared(vertx, config);
    MongoAuth auth = MongoAuth.create(client, new JsonObject());
    Async ready = context.async();
    auth.insertUser("admin", "password", Collections.emptyList(), Collections.emptyList(), context.asyncAssertSuccess(v -> ready.complete()));
    ready.awaitSuccess(2000);
    Async async = context.async();
    vertx.deployVerticle("service:io.vertx.ext.shell", new DeploymentOptions().setConfig(new JsonObject().put("sshOptions",
        new JsonObject().
            put("host", "localhost").
            put("port", 5000).
            put("keyPairOptions", new JsonObject().
                put("path", "src/test/resources/server-keystore.jks").
                put("password", "wibble")).
            put("authOptions", new JsonObject().put("provider", "mongo").put("config",
                new JsonObject().
                    put("connection_string", "mongodb://localhost:27018")))))
        , context.asyncAssertSuccess(v -> {
      async.complete();
    }));
    async.awaitSuccess(2000);
    Session session = createSession("admin", "password", false);
    session.connect();
    Channel channel = session.openChannel("shell");
    channel.connect();
    channel.disconnect();
    session.disconnect();
  }

  @Override
  public void testExec(TestContext context) throws Exception {
    AtomicReference<Vertx> execCommand = new AtomicReference<>();
    CommandRegistry registry = CommandRegistry.getShared(vertx);
    registry.registerCommand(CommandBuilder.command("the-command").processHandler(process -> {
      context.assertNotNull(Vertx.currentContext());
      execCommand.set(process.vertx());
      context.assertEquals(-1, process.width());
      context.assertEquals(-1, process.height());
      context.assertEquals(Arrays.asList("arg1", "arg2"), process.args());
      context.assertFalse(process.isForeground());
      process.vertx().setTimer(10, id -> {
        process.end(2);
      });
    }).build(vertx));
    super.testExec(context);
    assertEquals(execCommand.get(), vertx);
  }
}
