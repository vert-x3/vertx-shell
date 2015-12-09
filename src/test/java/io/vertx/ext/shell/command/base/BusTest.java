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

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.CaseInsensitiveHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.shell.Shell;
import io.vertx.ext.shell.ShellServer;
import io.vertx.ext.shell.system.ExecStatus;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.shell.term.Pty;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import sun.misc.BASE64Encoder;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class BusTest {

  Vertx vertx;
  ShellServer server;

  @Before
  public void before(TestContext context) throws Exception {
    vertx = Vertx.vertx();
    server = ShellServer.create(vertx).registerCommandResolver(new BaseCommandPack(vertx)).listen(context.asyncAssertSuccess());
  }

  @Test
  public void testBusSend(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send the_address the_message", msg -> {
      context.assertEquals("the_message", msg.body());
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendHeader(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --header=foo:bar the_address the_message", msg -> {
      context.assertEquals("the_message", msg.body());
      context.assertEquals(Collections.singleton("foo"), msg.headers().names());
      context.assertEquals(Collections.singletonList("bar"), msg.headers().getAll("foo"));
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendHeaders(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --header=foo:bar1 --header=foo:bar2 the_address the_message", msg -> {
      context.assertEquals("the_message", msg.body());
      context.assertEquals(Collections.singleton("foo"), msg.headers().names());
      context.assertEquals(Arrays.asList("bar1", "bar2"), msg.headers().getAll("foo"));
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendReply(TestContext context) {
    Async consumerAsync = context.async();
    String result = assertBusSend(context, "bus-send --reply the_address the_message", msg -> {
      msg.reply("the_reply");
      context.assertEquals("the_message", msg.body());
      consumerAsync.complete();
    });
    context.assertEquals("Reply: <the_reply>\n", result);
  }

  @Test
  public void testBusSendReplyTimeout(TestContext context) {
    Async consumerAsync = context.async();
    String result = assertBusSend(context, "bus-send --reply --timeout 50 the_address the_message", msg -> {
      context.assertEquals("the_message", msg.body());
      consumerAsync.complete();
    });
    context.assertEquals("Error: Timed out waiting for a reply\n", result);
  }

  @Test
  public void testBusSendReplyVerbose(TestContext context) {
    Async consumerAsync = context.async();
    String result = assertBusSend(context, "bus-send --reply --verbose the_address the_message", msg -> {
      msg.reply("the_reply", new DeliveryOptions().addHeader("header_name", "header_value_1").addHeader("header_name", "header_value_2"));
      context.assertEquals("the_message", msg.body());
      consumerAsync.complete();
    });
    context.assertEquals("Reply address: null\nReply header header_name:[header_value_1, header_value_2]\nReply: <the_reply>\n", result);
  }

  @Test
  public void testBusSendNull(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send the_address", msg -> {
      context.assertEquals(null, msg.body());
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendString(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --type STRING the_address hello_string", msg -> {
      context.assertEquals("hello_string", msg.body());
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendBoolean(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --type BOOLEAN the_address true", msg -> {
      context.assertEquals(true, msg.body());
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendByte(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --type BYTE the_address 123", msg -> {
      context.assertEquals((byte)123, msg.body());
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendShort(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --type SHORT the_address 1234", msg -> {
      context.assertEquals((short)1234, msg.body());
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendInteger(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --type INTEGER the_address 12345678", msg -> {
      context.assertEquals(12345678, msg.body());
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendLong(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --type LONG the_address 12345678", msg -> {
      context.assertEquals(12345678L, msg.body());
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendFloat(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --type FLOAT the_address 0.12", msg -> {
      context.assertEquals(0.12f, msg.body());
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendDouble(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --type DOUBLE the_address 0.1234", msg -> {
      context.assertEquals(0.1234D, msg.body());
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendCharacter(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --type CHARACTER the_address A", msg -> {
      context.assertEquals('A', msg.body());
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendJsonObject(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --type JSON_OBJECT the_address '{\"foo\":\"foo_value\",\"bar\":3}'", msg -> {
      context.assertEquals(new JsonObject().put("foo", "foo_value").put("bar", 3), msg.body());
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendArrayObject(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --type JSON_ARRAY the_address '[\"foo\",3]'", msg -> {
      context.assertEquals(new JsonArray().add("foo").add(3), msg.body());
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendBuffer(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --type BUFFER the_address hello_world", msg -> {
      context.assertEquals(Buffer.buffer("hello_world"), msg.body());
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendHex(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --type HEX the_address 001FFF", msg -> {
      context.assertEquals(Buffer.buffer(new byte[]{0,31,-1}), msg.body());
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendBase64(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --type BASE64 the_address " + new BASE64Encoder().encode(new byte[]{0,31,-1}), msg -> {
      context.assertEquals(Buffer.buffer(new byte[]{0,31,-1}), msg.body());
      consumerAsync.complete();
    });
  }

  @Test
  public void testBusSendNegativeLong(TestContext context) {
    Async consumerAsync = context.async();
    assertBusSend(context, "bus-send --type LONG the_address -12345678", msg -> {
      context.assertEquals(-12345678L, msg.body());
      consumerAsync.complete();
    });
  }

  private <T> String assertBusSend(TestContext context, String cmd, Handler<Message<T>> handler) {
    Async terminatedLatch = context.async();
    vertx.eventBus().consumer("the_address", handler);
    Shell shell = server.createShell();
    Pty pty = Pty.create();
    StringBuffer result = new StringBuffer();
    pty.stdoutHandler(result::append);
    Job job = shell.createJob(cmd).setTty(pty.slave());
    job.statusUpdateHandler(status -> {
      if (status == ExecStatus.TERMINATED) {
        terminatedLatch.complete();
      }
    });
    job.run();
    terminatedLatch.awaitSuccess(5000);
    return result.toString();
  }

  @Test
  public void testBusTail(TestContext context) {
    EventBus eb = vertx.eventBus();
    assertBusTail(context, "bus-tail the_address1 the_address2", () -> {
      eb.send("the_address1", "the_message1");
      eb.send("the_address2", "the_message2");
      eb.send("the_address1", "the_message3");
    }, "the_address1:the_message1\nthe_address2:the_message2\nthe_address1:the_message3\n");
  }

  @Test
  public void testBusTailVerbose(TestContext context) {
    EventBus eb = vertx.eventBus();
    assertBusTail(context, "bus-tail --verbose the_address", () -> {
      eb.send("the_address", "the_message", new DeliveryOptions().setHeaders(new CaseInsensitiveHeaders()).addHeader("header_name", "header_value"));
    }, "the_address:\nReply address: null\nHeader header_name:[header_value]\nthe_message\n");
  }

  private void assertBusTail(TestContext context, String cmd, Runnable send, String expected) {
    Async runningLatch = context.async();
    Shell shell = server.createShell();
    Pty pty = Pty.create();
    StringBuffer result = new StringBuffer();
    pty.stdoutHandler(result::append);
    Job job = shell.createJob(cmd).setTty(pty.slave());
    job.statusUpdateHandler(status -> {
      if (status == ExecStatus.RUNNING) {
        runningLatch.complete();
      }
    });
    job.run();
    runningLatch.awaitSuccess(5000);
    send.run();
    long now = System.currentTimeMillis();
    while (result.length() < expected.length()) {
      context.assertTrue(System.currentTimeMillis() - now < 2000);
    }
    context.assertEquals(expected, result.toString());
  }
}
