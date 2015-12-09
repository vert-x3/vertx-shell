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

package io.vertx.ext.shell.command.impl;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.cli.CLIException;
import io.vertx.core.cli.CommandLine;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.system.*;
import io.vertx.ext.shell.system.Process;
import io.vertx.ext.shell.term.Tty;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ProcessImpl implements Process {

  private final Vertx vertx;
  private final Context context;
  private final Context processContext;
  private final Command commandContext;
  private final Handler<CommandProcess> handler;
  private final List<CliToken> args;
  private Tty tty;
  private Session session;
  private Handler<Void> interruptHandler;
  private Handler<Void> suspendHandler;
  private Handler<Void> resumeHandler;
  private Handler<Void> endHandler;
  private Handler<Void> backgroundHandler;
  private Handler<Void> foregroundHandler;
  private Handler<Integer> terminatedHandler;

  // State exposed to the command, updated on the command context
  private boolean foreground;

  // Internal state used by the process
  private ExecStatus processStatus;
  private boolean processForeground;
  private Handler<String> stdinHandler;
  private Handler<Void> resizeHandler;
  private Integer exitCode;

  public ProcessImpl(Vertx vertx, Context context, Command commandContext, List<CliToken> args, Handler<CommandProcess> handler) {
    this.vertx = vertx;
    this.context = context;
    this.commandContext = commandContext;
    this.handler = handler;
    this.args = args;
    processContext = vertx.getOrCreateContext();
    processStatus = ExecStatus.READY;
  }

  @Override
  public Integer exitCode() {
    return exitCode;
  }

  @Override
  public ExecStatus status() {
    return processStatus;
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
  public Process terminatedHandler(Handler<Integer> handler) {
    terminatedHandler = handler;
    return this;
  }

  @Override
  public boolean interrupt(Handler<Void> completionHandler) {
    if (processStatus == ExecStatus.RUNNING || processStatus == ExecStatus.STOPPED) {
      Handler<Void> handler = interruptHandler;
      processContext.runOnContext(v -> {
        try {
          if (handler != null) {
            handler.handle(null);
          }
        } finally {
          if (completionHandler != null) {
            processContext.runOnContext(completionHandler);
          }
        }
      });
      return handler != null;
    } else {
      throw new IllegalStateException("Cannot interrupt process in " + processStatus + " state");
    }
  }

  @Override
  public synchronized void resume(boolean fg, Handler<Void> completionHandler) {
    if (processStatus == ExecStatus.STOPPED) {
      updateStatus(
          ExecStatus.RUNNING,
          null,
          fg,
          resumeHandler,
          terminatedHandler,
          completionHandler);
    } else {
      throw new IllegalStateException("Cannot resume process in " + processStatus + " state");
    }
  }

  @Override
  public synchronized void suspend(Handler<Void> completionHandler) {
    if (processStatus == ExecStatus.RUNNING) {
      updateStatus(
          ExecStatus.STOPPED,
          null,
          false,
          suspendHandler,
          terminatedHandler,
          completionHandler);
    } else {
      throw new IllegalStateException("Cannot suspend process in " + processStatus + " state");
    }
  }

  @Override
  public void toBackground(Handler<Void> completionHandler) {
    if (processStatus == ExecStatus.RUNNING) {
      if (processForeground) {
        updateStatus(
            ExecStatus.RUNNING,
            null,
            false,
            backgroundHandler,
            terminatedHandler,
            completionHandler);
      }
    } else {
      throw new IllegalStateException("Cannot set to background a process in " + processStatus + " state");
    }
  }

  @Override
  public void toForeground(Handler<Void> completionHandler) {
    if (processStatus == ExecStatus.RUNNING) {
      if (!processForeground) {
        updateStatus(
            ExecStatus.RUNNING,
            null,
            true,
            foregroundHandler,
            terminatedHandler,
            completionHandler);
      }
    } else {
      throw new IllegalStateException("Cannot set to foreground a process in " + processStatus + " state");
    }
  }

  @Override
  public void terminate(Handler<Void> completionHandler) {
    if (!terminate(-10, completionHandler)) {
      throw new IllegalStateException("Cannot terminate terminated process");
    }
  }

  private synchronized boolean terminate(int exitCode, Handler<Void> completionHandler) {
    if (processStatus != ExecStatus.TERMINATED) {
      updateStatus(
          ExecStatus.TERMINATED,
          exitCode,
          false,
          endHandler,
          terminatedHandler,
          completionHandler);
      return true;
    } else {
      return false;
    }
  }

  private void updateStatus(ExecStatus statusUpdate, Integer exitCodeUpdate, boolean foregroundUpdate, Handler<Void> handler, Handler<Integer> terminatedHandler, Handler<Void> completionHandler) {
    processStatus = statusUpdate;
    exitCode = exitCodeUpdate;
    if (!foregroundUpdate) {
      if (processForeground) {
        processForeground = false;
        if (stdinHandler != null) {
          tty.stdinHandler(null);
        }
        if (resizeHandler != null) {
          tty.resizehandler(null);
        }
      }
    } else {
      if (!processForeground) {
        processForeground = true;
        if (stdinHandler != null) {
          tty.stdinHandler(stdinHandler);
        }
        if (resizeHandler != null) {
          tty.resizehandler(resizeHandler);
        }
      }
    }
    context.runOnContext(v -> {
      foreground = foregroundUpdate;
      try {
        if (handler != null) {
          handler.handle(null);
        }
      } finally {
        if (completionHandler != null) {
          processContext.runOnContext(completionHandler);
        }
      }
    });
    if (terminatedHandler != null && statusUpdate == ExecStatus.TERMINATED) {
      processContext.runOnContext(v -> {
        terminatedHandler.handle(exitCodeUpdate);
      });
    }
  }

  @Override
  public synchronized void run(boolean fg, Handler<Void> completionHandler) {

    if (processStatus != ExecStatus.READY) {
      throw new IllegalStateException("Cannot run proces in " + processStatus + " state");
    }
    processStatus = ExecStatus.RUNNING;
    processForeground = fg;
    foreground = fg;

    // Make a local copy
    Tty tty = this.tty;
    if (tty == null) {
      throw new IllegalStateException("Cannot execute process without a TTY set");
    }

    CommandLine cl;
    final List<String> args2 = args.stream().filter(CliToken::isText).map(CliToken::value).collect(Collectors.toList());
    if (commandContext.cli() != null) {

      //
      try {
        // Build to skip validation problems
        if (commandContext.cli().parse(args2, false).isAskingForHelp()) {
          StringBuilder usage = new StringBuilder();
          commandContext.cli().usage(usage);
          usage.append('\n');
          tty.write(usage.toString());
          terminate();
          return;
        }

        cl = commandContext.cli().parse(args2);
      } catch (CLIException e) {
        tty.write(e.getMessage() + "\n");
        terminate();
        return;
      }
    } else {
      cl = null;
    }

    CommandProcess process = new CommandProcess() {

      @Override
      public Vertx vertx() {
        return vertx;
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
      public boolean isForeground() {
        return foreground;
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
      public CommandProcess stdinHandler(Handler<String> handler) {
        if (handler != null) {
          stdinHandler = data -> context.runOnContext(v -> handler.handle(data));
        } else {
          stdinHandler = null;
        }
        if (processForeground && stdinHandler != null) {
          tty.stdinHandler(stdinHandler);
        }
        return this;
      }

      @Override
      public CommandProcess write(String data) {
        synchronized (ProcessImpl.this) {
          if (processStatus != ExecStatus.RUNNING) {
            throw new IllegalStateException("Cannot write to standard output when " + status().name().toLowerCase());
          }
        }
        processContext.runOnContext(v -> {
          tty.write(data);
        });
        return this;
      }

      @Override
      public CommandProcess resizehandler(Handler<Void> handler) {
        if (handler != null) {
          resizeHandler = v -> context.runOnContext(handler::handle);
        } else {
          resizeHandler = null;
        }
        tty.resizehandler(resizeHandler);
        return this;
      }

      @Override
      public CommandProcess interruptHandler(Handler<Void> handler) {
        synchronized (ProcessImpl.this) {
          interruptHandler = handler;
        }
        return this;
      }

      @Override
      public CommandProcess suspendHandler(Handler<Void> handler) {
        synchronized (ProcessImpl.this) {
          suspendHandler = handler;
        }
        return this;
      }

      @Override
      public CommandProcess resumeHandler(Handler<Void> handler) {
        synchronized (ProcessImpl.this) {
          resumeHandler = handler;
        }
        return this;
      }

      @Override
      public CommandProcess endHandler(Handler<Void> handler) {
        synchronized (ProcessImpl.this) {
          endHandler = handler;
        }
        return this;
      }

      @Override
      public CommandProcess backgroundHandler(Handler<Void> handler) {
        synchronized (ProcessImpl.this) {
          backgroundHandler = handler;
        }
        return this;
      }

      @Override
      public CommandProcess foregroundHandler(Handler<Void> handler) {
        synchronized (ProcessImpl.this) {
          foregroundHandler = handler;
        }
        return this;
      }

      @Override
      public void end() {
        end(0);
      }

      @Override
      public void end(int statusCode) {
        terminate(statusCode, null);
      }

    };

    //
    context.runOnContext(v -> {
      try {
        try {
          handler.handle(process);
        } finally {
          if (completionHandler != null) {
            processContext.runOnContext(completionHandler);
          }
        }
      } catch (Throwable e) {
        terminate(1, null);
        throw e;
      }
    });
  }
}
