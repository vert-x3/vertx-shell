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

package io.vertx.ext.shell.impl;

import io.termd.core.util.Helper;
import io.vertx.core.Future;
import io.vertx.ext.shell.system.impl.InternalCommandManager;
import io.vertx.ext.shell.system.impl.ShellImpl;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.shell.system.ExecStatus;
import io.vertx.ext.shell.system.Shell;
import io.vertx.ext.shell.term.Term;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * The shell session as seen from the shell server perspective.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ShellSession {

  final String id;
  final Future<Void> closedFuture;
  private final InternalCommandManager manager;
  private final Shell shell;
  private Term term;
  private Job foregroundJob; // The currently running job
  private String welcome;

  public ShellSession(Term term, InternalCommandManager manager) {
    this.id = UUID.randomUUID().toString();
    this.shell = new ShellImpl(manager);
    this.manager = manager;
    this.closedFuture = Future.future();
    this.term = term.setSession(shell.session());
  }

  public Shell getShell() {
    return shell;
  }

  public long lastAccessedTime() {
    return term.lastAccessedTime();
  }

  public void setWelcome(String welcome) {
    this.welcome = welcome;
  }

  public ShellSession init() {

    term.interruptHandler(key -> foregroundJob.interrupt());

    term.suspendHandler(key -> {
      term.echo(Helper.fromCodePoints(new int[]{key, '\n'}));
      term.echo(statusLine(foregroundJob) + "\n");
      foregroundJob.suspend();
      return true;
    });

    term.closeHandler(v ->
        shell.close(ar ->
            closedFuture.complete()
        )
    );
    if (welcome != null && welcome.length() > 0) {
      term.stdout().write(welcome);
    }
    return this;
  }

  public Job foregroundJob() {
    return foregroundJob;
  }

  public Set<Job> jobs() {
    return shell.jobs();
  }

  public Job getJob(int id) {
    return shell.getJob(id);
  }

  public String statusLine(Job job) {
    StringBuilder sb = new StringBuilder("[").append(job.id()).append("]");
    if (findJob() == job) {
      sb.append("+");
    }
    sb.append(" ").append(Character.toUpperCase(job.status().name().charAt(0))).append(job.status().name().substring(1).toLowerCase());
    sb.append(" ").append(job.line());
    return sb.toString();
  }


  Job findJob() {
    return jobs().
        stream().
        filter(job -> job != foregroundJob).sorted((j1, j2) -> ((Long) j1.lastStopped()).compareTo(j2.lastStopped())).
        findFirst().orElse(null);
  }

  public void readline() {
    term.readline("% ", line -> {

      if (line == null) {
        // EOF
        term.close();
        return;
      }

      List<CliToken> tokens = CliToken.tokenize(line);

      if (tokens.stream().filter(CliToken::isText).count() == 0) {
        // For now do like this
        ShellSession.this.readline();
        return;
      }

      Optional<CliToken> first = tokens.stream().filter(CliToken::isText).findFirst();
      if (first.isPresent()) {
        String name = first.get().value();
        switch (name) {
          case "exit":
          case "logout":
            term.close();
            return;
          case "jobs":
            shell.jobs().forEach(job -> {
              String statusLine = statusLine(job) + "\n";
              term.stdout().write(statusLine);
            });
            readline();
            return;
          case "fg": {
            Job job = findJob();
            if (job == null) {
              term.stdout().write("no such job\n");
              readline();
            } else {
              if (job.status() == ExecStatus.STOPPED) {
                job.resume(true);
              } else {
                job.toForeground();
              }
            }
            return;
          }
          case "bg": {
            Job job = findJob();
            if (job == null) {
              term.stdout().write("no such job\n");
            } else {
              if (job.status() == ExecStatus.STOPPED) {
                job.resume(false);
                term.echo(statusLine(job) + "\n");
              } else {
                term.stdout().write("job " + job.id() + " already in background\n");
              }
            }
            readline();
            return;
          }
        }
      }

      Job job;
      try {
        job = shell.createJob(tokens);
      } catch (Exception e) {
        term.echo(e.getMessage() + "\n");
        readline();
        return;
      }
      foregroundJob = job;
      job.setTty(term);
      job.statusUpdateHandler(status -> {
        if (foregroundJob == job && !status.isForeground()) {
          foregroundJob = null;
          readline();
        } else if (status.getExecStatus() == ExecStatus.RUNNING && status.isForeground()) {
          foregroundJob = job;
          term.echo(job.line() + "\n");
        } else if (foregroundJob == job && status.getExecStatus() == ExecStatus.STOPPED) {
          foregroundJob = null;
          readline();
        }
      });
      job.run();
    }, completion -> {
      manager.complete(completion);
    });
  }

  void close() {
    term.close();
  }
}
