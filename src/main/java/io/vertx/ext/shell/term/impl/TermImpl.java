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

package io.vertx.ext.shell.term.impl;

import io.termd.core.readline.Keymap;
import io.termd.core.readline.Readline;
import io.termd.core.tty.TtyConnection;
import io.termd.core.util.Helper;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.term.SignalHandler;
import io.vertx.ext.shell.term.Term;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TermImpl implements Term {

  private Vertx vertx;
  private final Readline readline;
  private final Consumer<int[]> echoHandler;
  final TtyConnection conn;
  volatile Handler<String> stdinHandler;
  private SignalHandler interruptHandler;
  private SignalHandler suspendHandler;
  private Session session;
  private boolean inReadline;

  public TermImpl(Vertx vertx, TtyConnection conn) {
    this.vertx = vertx;
    java.io.InputStream inputrc = Keymap.class.getResourceAsStream("inputrc");
    Keymap keymap = new Keymap(inputrc);
    this.conn = conn;
    readline = new Readline(keymap);
    for (io.termd.core.readline.Function function : Helper.loadServices(Thread.currentThread().getContextClassLoader(), io.termd.core.readline.Function.class)) {
      readline.addFunction(function);
    }
    echoHandler = codePoints -> {
      // Echo
      echo(codePoints);
      readline.queueEvent(codePoints);
    };
    conn.setStdinHandler(echoHandler);
    conn.setEventHandler((event, key) -> {
      switch (event) {
        case INTR:
          if (interruptHandler == null || !interruptHandler.deliver(key)) {
            echo(key, '\n');
          }
          break;
        case EOF:
          // Pseudo signal
          if (stdinHandler != null) {
            stdinHandler.handle(Helper.fromCodePoints(new int[]{key}));
          } else {
            echo(key);
            readline.queueEvent(new int[]{key});
          }
          break;
        case SUSP:
          if (suspendHandler == null || !suspendHandler.deliver(key)) {
            echo(key, 'Z' - 64);
          }
          break;
      }
    });
  }

  @Override
  public Term setSession(Session session) {
    this.session = session;
    return this;
  }

  @Override
  public void readline(String prompt, Handler<String> lineHandler) {
    if (conn.getStdinHandler() != echoHandler) {
      throw new IllegalStateException();
    }
    if (inReadline) {
      throw new IllegalStateException();
    }
    inReadline = true;
    readline.readline(conn, prompt, line -> {
      inReadline = false;
      lineHandler.handle(line);
    });
  }

  public void readline(String prompt, Handler<String> lineHandler, Handler<Completion> completionHandler) {
    if (conn.getStdinHandler() != echoHandler) {
      throw new IllegalStateException();
    }
    if (inReadline) {
      throw new IllegalStateException();
    }
    inReadline = true;
    readline.readline(conn, prompt, line -> {
      inReadline = false;
      lineHandler.handle(line);
    }, abc -> {
      String line = Helper.fromCodePoints(abc.line());
      List<CliToken> tokens = Collections.unmodifiableList(CliToken.tokenize(line));
      Completion comp = new Completion() {

        @Override
        public Vertx vertx() {
          return vertx;
        }

        @Override
        public Session session() {
          return session;
        }

        @Override
        public String rawLine() {
          return line;
        }

        @Override
        public List<CliToken> lineTokens() {
          return tokens;
        }

        @Override
        public void complete(List<String> candidates) {
          if (candidates.size() > 0) {
            abc.suggest(candidates.stream().
                map(Helper::toCodePoints).
                collect(Collectors.toList()));
          } else {
            abc.end();
          }
        }

        @Override
        public void complete(String value, boolean terminal) {
          abc.complete(Helper.toCodePoints(value), terminal);
        }
      };
      completionHandler.handle(comp);
    });
  }

  @Override
  public Term closeHandler(Handler<Void> handler) {
    if (handler != null) {
      conn.setCloseHandler(handler::handle);
    } else {
      conn.setCloseHandler(null);
    }
    return this;
  }

  public long lastAccessedTime() {
    return conn.lastAccessedTime();
  }

  @Override
  public String type() {
    return conn.terminalType();
  }

  @Override
  public int width() {
    return conn.size() != null ? conn.size().x() : -1;
  }

  @Override
  public int height() {
    return conn.size() != null ? conn.size().y() : -1;
  }

  void checkPending() {
    if (stdinHandler != null && readline.hasEvent()) {
      stdinHandler.handle(Helper.fromCodePoints(readline.nextEvent().buffer().array()));
      vertx.runOnContext(v -> {
        checkPending();
      });
    }
  }

  @Override
  public TermImpl resizehandler(Handler<Void> handler) {
    if (inReadline) {
      throw new IllegalStateException();
    }
    if (handler != null) {
      conn.setSizeHandler(resize -> {
        handler.handle(null);
      });
    } else {
      conn.setSizeHandler(null);
    }
    return this;
  }

  @Override
  public Term stdinHandler(Handler<String> handler) {
    if (inReadline) {
      throw new IllegalStateException();
    }
    stdinHandler = handler;
    if (handler != null) {
      conn.setStdinHandler(codePoints -> {
        handler.handle(Helper.fromCodePoints(codePoints));
      });
      checkPending();
    } else {
      conn.setStdinHandler(echoHandler);
    }
    return this;
  }

  @Override
  public Term write(String data) {
    conn.write(data);
    return this;
  }

  public TermImpl interruptHandler(SignalHandler handler) {
    interruptHandler = handler;
    return this;
  }

  public TermImpl suspendHandler(SignalHandler handler) {
    suspendHandler = handler;
    return this;
  }

  public void close() {
    conn.close();
  }

  public TermImpl echo(String text) {
    echo(Helper.toCodePoints(text));
    return this;
  }

  public void echo(int... codePoints) {
    Consumer<int[]> out = conn.stdoutHandler();
    for (int codePoint : codePoints) {
      if (codePoint < 32) {
        if (codePoint == '\t') {
          out.accept(new int[]{'\t'});
        } else if (codePoint == '\b') {
          out.accept(new int[]{'\b', ' ', '\b'});
        } else if (codePoint == '\r' || codePoint == '\n') {
          out.accept(new int[]{'\n'});
        } else {
          out.accept(new int[]{'^', codePoint + 64});
        }
      } else {
        if (codePoint == 127) {
          out.accept(new int[]{'\b', ' ', '\b'});
        } else {
          out.accept(new int[]{codePoint});
        }
      }
    }
  }
}
