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

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.commons.net.telnet.EchoOptionHandler;
import org.apache.commons.net.telnet.SimpleOptionHandler;
import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;
import org.apache.commons.net.telnet.WindowSizeOptionHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class TelnetTermServerTest {

  private TelnetClient client;
  private Vertx vertx;
  private TermServer server;

  @Before
  public void before() throws Exception {
    vertx = Vertx.vertx();
    client = new TelnetClient();
    client.addOptionHandler(new EchoOptionHandler(false, false, true, true));
    client.addOptionHandler(new SimpleOptionHandler(0, false, false, true, true));
    client.addOptionHandler(new TerminalTypeOptionHandler("xterm-color", false, false, true, false));
  }

  @After
  public void after(TestContext context) {
    if (client != null && client.isConnected()) {
      try {
        client.disconnect();
      } catch (IOException ignore) {
      }
    }
    vertx.close(context.asyncAssertSuccess());
  }

  private void startTelnet(TestContext context, Handler<Term> termHandler) {
    startTelnet(context, new TelnetTermOptions(), termHandler);
  }

  private void startTelnet(TestContext context, TelnetTermOptions options, Handler<Term> termHandler) {
    server = TermServer.createTelnetTermServer(vertx, options.setPort(4000));
    server.termHandler(termHandler);
    Async async = context.async();
    server.listen(context.asyncAssertSuccess(v -> async.complete()));
    async.awaitSuccess(5000);
  }

  @Test
  public void testRead(TestContext context) throws IOException {
    Async async = context.async();
    startTelnet(context, term -> {
      term.stdinHandler(s -> {
        context.assertEquals("hello_from_client", s);
        async.complete();
      });
    });
    client.connect("localhost", server.actualPort());
    Writer writer = new OutputStreamWriter(client.getOutputStream());
    writer.write("hello_from_client");
    writer.flush();
  }

  @Test
  public void testWrite(TestContext context) throws IOException {
    startTelnet(context, term -> {
      term.write("hello_from_server");
    });
    client.connect("localhost", server.actualPort());
    Reader reader = new InputStreamReader(client.getInputStream());
    StringBuilder sb = new StringBuilder("hello_from_server");
    while (sb.length() > 0) {
      int c = reader.read();
      context.assertNotEquals(-1, c);
      context.assertEquals(c, (int)sb.charAt(0));
      sb.deleteCharAt(0);
    }
  }

  @Test
  public void testCloseHandler(TestContext context) throws IOException {
    Async async1 = context.async();
    Async async2 = context.async();
    startTelnet(context, term -> {
      term.closeHandler(v -> {
        async2.complete();
      });
      async1.complete();
    });
    client.connect("localhost", server.actualPort());
    async1.awaitSuccess(4000);
    client.disconnect();
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
    Async async1 = context.async();
    Async async2 = context.async();
    startTelnet(context, term -> {
      term.closeHandler(v -> {
        async2.complete();
      });
      closer.accept(term);
      async1.complete();
    });
    client.connect("localhost", server.actualPort());
    async1.await(4000);
    OutputStream out = client.getOutputStream();
    long now = System.currentTimeMillis();
    while (true) {
      context.assertTrue(System.currentTimeMillis() - now < 10000);
      try {
        out.write(4);
        out.flush();
      } catch (IOException ignore) {
        break;
      }
      Thread.sleep(10);
    }
  }

  @Test
  public void testType(TestContext context) throws Exception {
    Async async = context.async();
    startTelnet(context, term -> {
      long now = System.currentTimeMillis();
      vertx.setPeriodic(10, id -> {
        context.assertTrue(System.currentTimeMillis() - now < 10000);
        if (term.type() != null) {
          vertx.cancelTimer(id);
          context.assertEquals("xterm-color", term.type());
          async.complete();
        }
      });
    });
    client.connect("localhost", server.actualPort());
  }

  @Test
  public void testWindowSize(TestContext context) throws Exception {
    Async async = context.async();
    startTelnet(context, term -> {
      context.assertEquals(-1, term.width());
      context.assertEquals(-1, term.height());
      term.resizehandler(v -> {
        context.assertEquals(10, term.width());
        context.assertEquals(20, term.height());
        async.complete();
      });
    });
    client.addOptionHandler(new WindowSizeOptionHandler(10, 20, false, false, true, false));
    client.connect("localhost", server.actualPort());
  }

  @Test
  public void testOutBinaryTrue(TestContext context) throws Exception {
    startTelnet(context, new TelnetTermOptions().setOutBinary(true), term -> {
      term.write("\u20AC");
    });
    client.addOptionHandler(new WindowSizeOptionHandler(10, 20, false, false, true, false));
    client.connect("localhost", server.actualPort());
    InputStream in = client.getInputStream();
    context.assertEquals(226, in.read());
    context.assertEquals(130, in.read());
    context.assertEquals(172, in.read());
  }

  @Test
  public void testOutBinaryFalse(TestContext context) throws Exception {
    byte[] expected = StandardCharsets.US_ASCII.encode("€").array();
    startTelnet(context, new TelnetTermOptions().setOutBinary(false), term -> {
      term.write("\u20AC");
    });
    client.addOptionHandler(new WindowSizeOptionHandler(10, 20, false, false, true, false));
    client.connect("localhost", server.actualPort());
    InputStream in = client.getInputStream();
    for (int i = 0;i < expected.length;i++) {
      context.assertEquals((int)expected[i], in.read());
    }
  }

  @Test
  public void testDifferentCharset(TestContext context) throws Exception {
    startTelnet(context, new TelnetTermOptions().setCharset("ISO_8859_1"), term -> {
      term.write("\u20AC");
      term.close();
    });
    client.connect("localhost", server.actualPort());
    InputStream in = client.getInputStream();
    int b = in.read();
    context.assertEquals(63, b);
  }

  @Test
  public void testKeymapFromFilesystem(TestContext context) throws Exception {
    URL url = TermServer.class.getResource(SSHTermOptions.DEFAULT_INPUTRC);
    File f = new File(url.toURI());
    startTelnet(context, new TelnetTermOptions().setIntputrc(f.getAbsolutePath()), Term::close);
    client.connect("localhost", server.actualPort());
  }
}
