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
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.io.EventType;
import io.vertx.ext.shell.io.Stream;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.registry.CommandRegistration;
import io.vertx.ext.shell.process.Process;
import io.vertx.ext.shell.process.ProcessContext;

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
      public void execute(ProcessContext context) {

        CommandLine cl;
        final List<String> args2 = args.stream().filter(CliToken::isText).map(CliToken::value).collect(Collectors.toList());
        if (command.cli() != null) {

          // Build to skip validation problems
          if (command.cli().parse(args2, false).isAskingForHelp()) {
            StringBuilder usage = new StringBuilder();
            command.cli().usage(usage);
            usage.append('\n');
            context.tty().stdout().handle(usage.toString());
            context.end(0);
            return;
          }

          //
          try {
            cl = command.cli().parse(args2);
          } catch (CLIException e) {
            context.tty().stdout().handle(e.getMessage() + "\n");
            context.end(0);
            return;
          }
        } else {
          cl = null;
        }

        CommandProcess process = new CommandProcess() {

          @Override
          public Vertx vertx() {
            return registry.vertx;
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
            return context.session();
          }

          @Override
          public int width() {
            return context.tty().width();
          }

          @Override
          public int height() {
            return context.tty().height();
          }

          @Override
          public CommandProcess setStdin(Stream stdin) {
            Stream s;
            if (stdin != null) {
              s = data -> CommandRegistrationImpl.this.context.runOnContext(v -> stdin.handle(data));
            } else {
              s = null;
            }
            context.tty().setStdin(s);
            return this;
          }

          @Override
          public Stream stdout() {
            return context.tty().stdout();
          }

          @Override
          public CommandProcess write(String text) {
            context.tty().stdout().handle(text);
            return this;
          }

          @Override
          public CommandProcess resizehandler(Handler<Void> handler) {
            if (handler != null) {
              context.tty().resizehandler(v -> CommandRegistrationImpl.this.context.runOnContext(handler::handle));
            } else {
              context.tty().resizehandler(null);
            }
            return this;
          }

          @Override
          public CommandProcess eventHandler(EventType eventType, Handler<Void> handler) {
            if (handler != null) {
              context.eventHandler(eventType, v -> {
                CommandRegistrationImpl.this.context.runOnContext(v2 -> {
                  handler.handle(null);
                });
              });
            } else {
              context.eventHandler(eventType, null);
            }
            return this;
          }

          @Override
          public void end() {
            context.end(0);
          }

          @Override
          public void end(int status) {
            context.end(status);
          }
        };
        CommandRegistrationImpl.this.context.runOnContext(v -> {
          try {
            command.process(process);
          } catch (Throwable e) {
            context.end(1);
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
