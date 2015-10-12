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

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.registry.CommandRegistry;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.shell.system.ShellSession;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ShellSessionImpl implements ShellSession {

  final CommandRegistry registry;
  final SessionImpl session = new SessionImpl();
  final SortedMap<Integer, Job> jobs = new TreeMap<>();

  public ShellSessionImpl(CommandRegistry registry) {
    this.registry = registry;
  }

  public Set<Job> jobs() {
    return new HashSet<>(jobs.values());
  }

  public Job getJob(int id) {
    return jobs.get(id);
  }

  @Override
  public Session session() {
    return session;
  }

  @Override
  public void createJob(List<CliToken> args, Handler<AsyncResult<Job>> handler) {
    StringBuilder line = new StringBuilder();
    args.stream().map(CliToken::raw).forEach(line::append);
    registry.createProcess(args, ar -> {
      if (ar.succeeded()) {
        int id = jobs.isEmpty() ? 1 : jobs.lastKey() + 1;
        JobImpl job = new JobImpl(id, this, ar.result(), line.toString());
        jobs.put(id, job);
        handler.handle(Future.succeededFuture(job));
      } else {
        handler.handle(Future.failedFuture(line + ": command not found"));
      }
    });
  }

}
