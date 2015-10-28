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

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.cli.CLIException;
import io.vertx.core.cli.CommandLine;
import io.vertx.ext.shell.io.Tty;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.io.Stream;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.registry.CommandRegistration;
import io.vertx.ext.shell.system.Process;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The command registered with a manager.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CommandRegistrationImpl implements CommandRegistration {

  final CommandRegistryImpl registry;
  final Command command;
  final Context context;
  final String deploymendID;

  public CommandRegistrationImpl(CommandRegistryImpl registry, Command command, Context context, String deploymentID) {
    this.registry = registry;
    this.command = command;
    this.deploymendID = deploymentID;
    this.context = context;
  }

  public Command command() {
    return command;
  }

  public Process createProcess() {
    return createProcess(Collections.emptyList());
  }

  public void complete(Completion completion) {
    context.runOnContext(v -> {
      try {
        command.complete(completion);
      } catch (Throwable t) {
        completion.complete(Collections.emptyList());
        throw t;
      }
    });
  }

  public Process createProcess(List<CliToken> args) {
    return new Process() {

      private volatile Tty tty;
      private volatile Session session;
      private volatile Handler<Void> interruptHandler;
      private volatile Handler<Void> suspendHandler;
      private volatile Handler<Void> resumeHandler;

      @Override
      public void setTty(Tty tty) {
        this.tty = tty;
      }

      @Override
      public Tty getTty() {
        return tty;
      }

      @Override
      public void setSession(Session session) {
        this.session = session;
      }

      @Override
      public Session getSession() {
        return session;
      }

      @Override
      public boolean interrupt() {
        Handler<Void> handler = interruptHandler;
        if (handler != null) {
          CommandRegistrationImpl.this.context.runOnContext(handler::handle);
        }
        return handler != null;
      }

      @Override
      public void resume() {
        Handler<Void> handler = resumeHandler;
        if (handler != null) {
          CommandRegistrationImpl.this.context.runOnContext(handler::handle);
        }
      }

      @Override
      public void suspend() {
        Handler<Void> handler = suspendHandler;
        if (handler != null) {
          CommandRegistrationImpl.this.context.runOnContext(handler::handle);
        }
      }

      @Override
      public void execute(Handler<Integer> endHandler) {

        Context outerContext = registry.vertx.getOrCreateContext();

        CommandLine cl;
        final List<String> args2 = args.stream().filter(CliToken::isText).map(CliToken::value).collect(Collectors.toList());
        if (command.cli() != null) {

          // Build to skip validation problems
          if (command.cli().parse(args2, false).isAskingForHelp()) {
            StringBuilder usage = new StringBuilder();
            command.cli().usage(usage);
            usage.append('\n');
            tty.stdout().handle(usage.toString());
            endHandler.handle(0);
            return;
          }

          //
          try {
            cl = command.cli().parse(args2);
          } catch (CLIException e) {
            tty.stdout().handle(e.getMessage() + "\n");
            endHandler.handle(0);
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
            return registry.vertx;
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
              s = data -> CommandRegistrationImpl.this.context.runOnContext(v -> stdin.handle(data));
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
                  outerContext.runOnContext(v -> {
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
              tty.resizehandler(v -> CommandRegistrationImpl.this.context.runOnContext(handler::handle));
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
          public void end() {
            end(0);
          }

          @Override
          public void end(int status) {
            tty.setStdin(null);
            outerContext.runOnContext(v -> {
              endHandler.handle(status);
            });
          }
        };
        CommandRegistrationImpl.this.context.runOnContext(v -> {
          try {
            command.process(process);
          } catch (Throwable e) {
            endHandler.handle(1);
            throw e;
          }
        });
      }
    };
  }

  @Override
  public void unregister() {
    unregister(null);
  }

  @Override
  public void unregister(Handler<AsyncResult<Void>> handler) {
    if (handler != null) {
      registry.vertx.undeploy(deploymendID, handler);
    } else {
      registry.vertx.undeploy(deploymendID);
    }
  }
}
