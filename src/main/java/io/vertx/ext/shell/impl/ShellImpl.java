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
import io.vertx.ext.shell.Shell;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.session.impl.SessionImpl;
import io.vertx.ext.shell.system.*;
import io.vertx.ext.shell.system.Process;
import io.vertx.ext.shell.system.impl.InternalCommandManager;
import io.vertx.ext.shell.system.impl.JobControllerImpl;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.term.Term;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * The shell session as seen from the shell server perspective.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ShellImpl implements Shell {

  final String id;
  final Future<Void> closedFuture;
  private final InternalCommandManager commandManager;
  private final Session session = new SessionImpl();
  private final JobControllerImpl jobController;
  private Term term;
  private String welcome;
  private Function<Session, String> promptFunc = s -> "% ";

  public ShellImpl(Term term, InternalCommandManager commandManager) {

    session.put("vert.x-command-manager", commandManager);

    this.id = UUID.randomUUID().toString();
    this.jobController = new JobControllerImpl();
    this.commandManager = commandManager;
    this.closedFuture = Future.future();
    this.term = term;

    if (term != null) {
      term.setSession(session);
      jobController.foregroundUpdatedHandler(job -> {
        if (job == null) {
          readline();
        }
      });
    }
  }

  public JobController jobController() {
    return jobController;
  }

  @Override
  public synchronized Job createJob(List<CliToken> args) {
    StringBuilder line = new StringBuilder();
    args.stream().map(CliToken::raw).forEach(line::append);
    Process process = commandManager.createProcess(args);
    return jobController.createJob(process, line.toString());
  }

  @Override
  public Job createJob(String line) {
    return createJob(CliToken.tokenize(line));
  }

  @Override
  public Session session() {
    return session;
  }

  public long lastAccessedTime() {
    return term.lastAccessedTime();
  }

  public void setWelcome(String welcome) {
    this.welcome = welcome;
  }

  @Override
  public void setPrompt(Function<Session, String> prompt) {
    this.promptFunc = prompt;
  }

  public ShellImpl init() {

    term.interruptHandler(key -> jobController().foregroundJob().interrupt());

    term.suspendHandler(key -> {
      term.echo(Helper.fromCodePoints(new int[]{key, '\n'}));
      Job job = jobController.foregroundJob();
      term.echo(statusLine(job, ExecStatus.STOPPED) + "\n");
      job.suspend();
      return true;
    });

    term.closeHandler(v ->
        jobController.close(ar ->
            closedFuture.complete()
        )
    );
    if (welcome != null && welcome.length() > 0) {
      term.write(welcome);
    }
    return this;
  }

  private String statusLine(Job job, ExecStatus status) {
    StringBuilder sb = new StringBuilder("[").append(job.id()).append("]");
    if (findJob() == job) {
      sb.append("+");
    }
    sb.append(" ").append(Character.toUpperCase(status.name().charAt(0))).append(job.status().name().substring(1).toLowerCase());
    sb.append(" ").append(job.line());
    return sb.toString();
  }

  private Job findJob() {
    Job foregroundJob = jobController.foregroundJob();
    return jobController().jobs().
        stream().
        filter(job -> job != foregroundJob).sorted((j1, j2) -> ((Long) j1.lastStopped()).compareTo(j2.lastStopped())).
        findFirst().orElse(null);
  }

  public void readline() {
    String prompt = promptFunc.apply(session);
    term.readline(prompt, line -> {

      if (line == null) {
        // EOF
        term.close();
        return;
      }

      List<CliToken> tokens = CliToken.tokenize(line);

      if (tokens.stream().filter(CliToken::isText).count() == 0) {
        // For now do like this
        ShellImpl.this.readline();
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
            jobController.jobs().forEach(job -> {
              String statusLine = statusLine(job, job.status()) + "\n";
              term.write(statusLine);
            });
            readline();
            return;
          case "fg": {
            Job job = findJob();
            if (job == null) {
              term.write("no such job\n");
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
              term.write("no such job\n");
              readline();
            } else {
              if (job.status() == ExecStatus.STOPPED) {
                job.resume(false);
                term.echo(statusLine(job, ExecStatus.RUNNING) + "\n");
                readline();
              } else {
                term.write("job " + job.id() + " already in background\n");
                readline();
              }
            }
            return;
          }
        }
      }

      Job job;
      try {
        job = createJob(tokens);
      } catch (Exception e) {
        term.echo(e.getMessage() + "\n");
        readline();
        return;
      }
      job.setTty(term);
      job.setSession(session);
      job.run();
    }, completion -> {
      commandManager.complete(completion);
    });
  }

  public void close() {
    if (term != null) {
      term.close();
    } else {
      jobController.close(ar -> closedFuture.complete());
    }
  }
}
