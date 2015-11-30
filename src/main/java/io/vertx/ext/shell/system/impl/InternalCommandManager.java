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

package io.vertx.ext.shell.system.impl;

import io.vertx.core.Vertx;
import io.vertx.core.VertxException;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandResolver;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.system.Process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class InternalCommandManager {

  private final List<CommandResolver> resolvers;

  public InternalCommandManager(CommandResolver... resolvers) {
    this.resolvers = Arrays.asList(resolvers);
  }

  public InternalCommandManager(List<CommandResolver> resolvers) {
    this.resolvers = resolvers;
  }

  public List<CommandResolver> getResolvers() {
    return resolvers;
  }

  /**
   * Parses a command line and try to create a process.
   *
   * @param line the command line to parse
   * @return the created process
   */
  public Process createProcess(String line) {
    return createProcess(CliToken.tokenize(line));
  }

  /**
   * Try to create a process from the command line tokens.
   *
   * @param line the command line tokens
   * @return the created process
   */
  public Process createProcess(List<CliToken> line) {
    try {
      return makeRequest(line);
    } catch (Exception e) {
      throw new VertxException(e);
    }
  }

  private Process makeRequest(List<CliToken> s) {
    ListIterator<CliToken> tokens = s.listIterator();
    while (tokens.hasNext()) {
      CliToken token = tokens.next();
      if (token.isText()) {
        for (CommandResolver resolver : resolvers) {
          Command command = resolver.getCommand(token.value());
          if (command != null) {
            List<CliToken> remaining = new ArrayList<>();
            while (tokens.hasNext()) {
              remaining.add(tokens.next());
            }
            return command.createProcess(remaining);
          }
        }
        throw new IllegalArgumentException(token.value() + ": command not found");
      }
    }
    throw new IllegalArgumentException();
  }

  /**
   * Perform completion, the completion argument will be notified of the completion progress.
   *
   * @param completion the completion object
   */
  public void complete(Completion completion) {
    LinkedList<CliToken> tokens = new LinkedList<>(completion.lineTokens());

    // Remove any leading white space
    while (tokens.size() > 0 && tokens.getFirst().isBlank()) {
      tokens.removeFirst();
    }

    // > 1 means it's a text token followed by something else
    if (tokens.size() > 1) {
      ListIterator<CliToken> it = tokens.listIterator();
      while (it.hasNext()) {
        CliToken ct = it.next();
        it.remove();
        if (ct.isText()) {
          List<CliToken> newTokens = new ArrayList<>();
          while (it.hasNext()) {
            newTokens.add(it.next());
          }
          StringBuilder tmp = new StringBuilder();
          newTokens.stream().forEach(token -> tmp.append(token.raw()));
          String line = tmp.toString();
          for (CommandResolver resolver : resolvers) {
            Command command = resolver.getCommand(ct.value());
            if (command != null) {
              command.complete(new Completion() {
                @Override
                public Vertx vertx() {
                  return completion.vertx();
                }
                @Override
                public Session session() {
                  return completion.session();
                }
                @Override
                public String rawLine() {
                  return line;
                }
                @Override
                public List<CliToken> lineTokens() {
                  return newTokens;
                }
                @Override
                public void complete(List<String> candidates) {
                  completion.complete(candidates);
                }
                @Override
                public void complete(String value, boolean terminal) {
                  completion.complete(value, terminal);
                }
              });
              return;
            }
          }
          completion.complete(Collections.emptyList());
        }
      }
    } else {
      String prefix = tokens.size() > 0 ? tokens.getFirst().value() : "";
      List<String> names = resolvers.stream().
          flatMap(res -> res.commands().stream()).
          map(Command::name).
          filter(name -> name.startsWith(prefix)).
          distinct().
          collect(Collectors.toList());
      if (names.size() == 1) {
        completion.complete(names.get(0).substring(prefix.length()), true);
      } else {
        String commonPrefix = Completion.findLongestCommonPrefix(names);
        if (commonPrefix.length() > prefix.length()) {
          completion.complete(commonPrefix.substring(prefix.length()), false);
        } else {
          completion.complete(names);
        }
      }
    }
  }

}
