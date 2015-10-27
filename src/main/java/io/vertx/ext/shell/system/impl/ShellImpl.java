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

import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.process.Process;
import io.vertx.ext.shell.registry.CommandRegistry;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.shell.system.Shell;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ShellImpl implements Shell {

  final CommandRegistry registry;
  final SessionImpl session = new SessionImpl();
  private final SortedMap<Integer, Job> jobs = new TreeMap<>();

  public ShellImpl(CommandRegistry registry) {
    this.registry = registry;
  }

  public synchronized Set<Job> jobs() {
    return new HashSet<>(jobs.values());
  }

  public synchronized Job getJob(int id) {
    return jobs.get(id);
  }

  @Override
  public Session session() {
    return session;
  }

  synchronized boolean removeJob(int id) {
    return jobs.remove(id) != null;
  }

  @Override
  public synchronized Job createJob(List<CliToken> args) {
    StringBuilder line = new StringBuilder();
    args.stream().map(CliToken::raw).forEach(line::append);
    Process process = registry.createProcess(args);
    int id = jobs.isEmpty() ? 1 : jobs.lastKey() + 1;
    JobImpl job = new JobImpl(id, this, process, line.toString());
    jobs.put(id, job);
    return job;
  }

  @Override
  public Job createJob(String line) {
    return createJob(CliToken.tokenize(line));
  }
}
