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

package io.vertx.ext.shell.registry.impl;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.cli.CLIException;
import io.vertx.core.cli.CommandLine;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.io.Stream;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.system.*;
import io.vertx.ext.shell.system.Process;
import io.vertx.ext.shell.term.Tty;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class ProcessImpl implements Process {

  private final CommandRegistrationImpl registration;
  private final List<CliToken> args;
  private Tty tty;
  private Session session;
  private Handler<Void> interruptHandler;
  private Handler<Void> suspendHandler;
  private Handler<Void> resumeHandler;
  private Handler<Void> endHandler;
  private Handler<Integer> terminateHandler;
  private Context runnerContext;
  private ExecStatus status;

  public ProcessImpl(CommandRegistrationImpl registration, List<CliToken> args) {
    this.registration = registration;
    this.args = args;
    status = ExecStatus.READY;
  }

  @Override
  public synchronized Process setTty(Tty tty) {
    this.tty = tty;
    return this;
  }

  @Override
  public synchronized Tty getTty() {
    return tty;
  }

  @Override
  public synchronized Process setSession(Session session) {
    this.session = session;
    return this;
  }

  @Override
  public synchronized Session getSession() {
    return session;
  }

  @Override
  public Process terminateHandler(Handler<Integer> handler) {
    terminateHandler = handler;
    return this;
  }

  @Override
  public synchronized boolean interrupt() {
    if (status == ExecStatus.RUNNING || status == ExecStatus.STOPPED) {
      Handler<Void> handler = interruptHandler;
      if (handler != null) {
        registration.context.runOnContext(handler::handle);
      }
      return handler != null;
    } else {
      throw new IllegalStateException("Cannot interrupt process in " + status + " state");
    }
  }

  @Override
  public synchronized void resume() {
    if (status == ExecStatus.STOPPED) {
      status = ExecStatus.RUNNING;
      Handler<Void> handler = resumeHandler;
      if (handler != null) {
        registration.context.runOnContext(handler::handle);
      }
    } else {
      throw new IllegalStateException("Cannot resume process in " + status + " state");
    }
  }

  @Override
  public synchronized void suspend() {
    if (status == ExecStatus.RUNNING) {
      status = ExecStatus.STOPPED;
      Handler<Void> handler = suspendHandler;
      if (handler != null) {
        registration.context.runOnContext(handler::handle);
      }
    } else {
      throw new IllegalStateException("Cannot suspend process in " + status + " state");
    }
  }

  @Override
  public void terminate() {
    if (!terminate(-10)) {
      throw new IllegalStateException("Cannot terminate terminated process");
    }
  }

  private synchronized boolean terminate(int statusCode) {
    if (status != ExecStatus.TERMINATED) {
      status = ExecStatus.TERMINATED;
      tty.setStdin(null);
      Handler<Integer> terminateHandler = this.terminateHandler;
      if (terminateHandler != null) {
        runnerContext.runOnContext(v -> {
          terminateHandler.handle(statusCode);
        });
      }
      Handler<Void> handler = endHandler;
      if (handler != null) {
        registration.context.runOnContext(handler::handle);
      }
      return true;
    } else {
      return false;
    }
  }

  @Override
  public synchronized void run() {

    if (status != ExecStatus.READY) {
      throw new IllegalStateException("Cannot run proces in " + status + " state");
    }

    // Make a local copy
    Tty tty = this.tty;
    if (tty == null) {
      throw new IllegalStateException("Cannot execute process without a TTY set");
    }

    // Make a local copy
    Session session = this.session;
    if (session == null) {
      throw new IllegalStateException("Cannot execute process without a Session set");
    }

    this.runnerContext = registration.registry.vertx.getOrCreateContext();

    CommandLine cl;
    final List<String> args2 = args.stream().filter(CliToken::isText).map(CliToken::value).collect(Collectors.toList());
    if (registration.command.cli() != null) {

      // Build to skip validation problems
      if (registration.command.cli().parse(args2, false).isAskingForHelp()) {
        StringBuilder usage = new StringBuilder();
        registration.command.cli().usage(usage);
        usage.append('\n');
        tty.stdout().handle(usage.toString());
        terminate();
        return;
      }

      //
      try {
        cl = registration.command.cli().parse(args2);
      } catch (CLIException e) {
        tty.stdout().handle(e.getMessage() + "\n");
        terminate();
        return;
      }
    } else {
      cl = null;
    }

    CommandProcess process = new CommandProcess() {

      private Stream stdout;
      private Stream wrappedStdout;

      @Override
      public Vertx vertx() {
        return registration.registry.vertx;
      }

      @Override
      public String type() {
        return tty.type();
      }

      @Override
      public CommandLine commandLine() {
        return cl;
      }

      @Override
      public List<CliToken> argsTokens() {
        return args;
      }

      @Override
      public List<String> args() {
        return args2;
      }

      @Override
      public Session session() {
        return session;
      }

      @Override
      public int width() {
        return tty.width();
      }

      @Override
      public int height() {
        return tty.height();
      }

      @Override
      public CommandProcess setStdin(Stream stdin) {
        Stream s;
        if (stdin != null) {
          s = data -> registration.context.runOnContext(v -> stdin.handle(data));
        } else {
          s = null;
        }
        tty.setStdin(s);
        return this;
      }

      @Override
      public Stream stdout() {
        Stream contextStdout = tty.stdout();
        if (contextStdout != stdout) {
          if (contextStdout != null) {
            wrappedStdout = line -> {
              runnerContext.runOnContext(v -> {
                contextStdout.handle(line);
              });
            };
          } else {
            wrappedStdout = null;
          }
          stdout = contextStdout;
        }
        return wrappedStdout;
      }

      @Override
      public CommandProcess write(String text) {
        stdout().handle(text);
        return this;
      }

      @Override
      public CommandProcess resizehandler(Handler<Void> handler) {
        if (handler != null) {
          tty.resizehandler(v -> registration.context.runOnContext(handler::handle));
        } else {
          tty.resizehandler(null);
        }
        return this;
      }

      @Override
      public CommandProcess interruptHandler(Handler<Void> handler) {
        interruptHandler = handler;
        return this;
      }

      @Override
      public CommandProcess suspendHandler(Handler<Void> handler) {
        suspendHandler = handler;
        return this;
      }

      @Override
      public CommandProcess resumeHandler(Handler<Void> handler) {
        resumeHandler = handler;
        return this;
      }

      @Override
      public CommandProcess endHandler(Handler<Void> handler) {
        endHandler = handler;
        return this;
      }

      @Override
      public void end() {
        end(0);
      }

      @Override
      public void end(int statusCode) {
        terminate(statusCode);
      }
    };

    //
    registration.context.runOnContext(v -> {
      synchronized (ProcessImpl.this) {
        if (status == ExecStatus.READY) {
          status = ExecStatus.RUNNING;
        } else {
          // It can only be terminated status
          return;
        }
      }
      try {
        registration.command.process(process);
      } catch (Throwable e) {
        terminate(1);
        throw e;
      }
    });
  }
}
