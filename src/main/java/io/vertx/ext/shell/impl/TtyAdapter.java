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

import io.termd.core.readline.Keymap;
import io.termd.core.readline.Readline;
import io.termd.core.tty.TtyConnection;
import io.termd.core.util.Helper;
import io.termd.core.util.Vector;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.term.Tty;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.io.Stream;
import io.vertx.ext.shell.registry.CommandRegistry;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.shell.system.JobStatus;
import io.vertx.ext.shell.system.Shell;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TtyAdapter {

  final CommandRegistry registry;
  final Shell session;
  final Vertx vertx;
  private final TtyConnection conn;
  final Readline readline;
  Job foregroundJob; // The currently running job
  String welcome;

  public TtyAdapter(Vertx vertx, TtyConnection conn, Shell session, CommandRegistry registry) {

    InputStream inputrc = Keymap.class.getResourceAsStream("inputrc");
    Keymap keymap = new Keymap(inputrc);
    Readline readline = new Readline(keymap);

    this.vertx = vertx;
    this.conn = conn;
    this.session = session;
    this.readline = readline;
    this.registry = registry;
  }

  public String welcome() {
    return welcome;
  }

  public void setWelcome(String welcome) {
    this.welcome = welcome;
  }

  public Vector size() {
    return conn.size();
  }

  public void init() {
    for (io.termd.core.readline.Function function : Helper.loadServices(Thread.currentThread().getContextClassLoader(), io.termd.core.readline.Function.class)) {
      readline.addFunction(function);
    }
    conn.setStdinHandler(codePoints -> {
      if (foregroundJob == null) {
        // Bug ???
      } else {
        if (((TtyImpl)foregroundJob.getTty()).stdin != null) {
          // Forward
          ((TtyImpl)foregroundJob.getTty()).stdin.handle(Helper.fromCodePoints(codePoints));
        } else {
          // Echo
          echo(codePoints);
          readline.queueEvent(codePoints);
        }
      }
    });
    conn.setSizeHandler(resize -> {
      Job job = foregroundJob;
      if (job != null) {
        TtyImpl tty = (TtyImpl) job.getTty();
        if (tty.resizeHandler != null) {
          tty.resizeHandler.handle(null);
        }
      }
    });
    conn.setEventHandler((event, key) -> {
      Job job = foregroundJob;
      switch (event) {
        case INTR:
          if (!job.interrupt()) {
            echo(key);
            readline.queueEvent(new int[]{key});
          } else {
            echo(key, '\n');
          }
          break;
        case EOF:
          // Pseudo signal
          if (((TtyImpl)foregroundJob.getTty()).stdin != null) {
            ((TtyImpl)foregroundJob.getTty()).stdin.handle(Helper.fromCodePoints(new int[]{key}));
          } else {
            echo(key);
            readline.queueEvent(new int[]{key});
          }
          break;
        case SUSP:
          echo(key, '\n');
          echo(Helper.toCodePoints(statusLine(job) + "\n"));
          foregroundJob = null;
          ((TtyImpl)job.getTty()).stdout = null;
          job.suspend();
          read(readline);
          break;
      }
    });
    if (welcome != null && welcome.length() > 0) {
      conn.write(welcome);
    }
    read(readline);
  }

  private void echo(int... codePoints) {
    Consumer<int[]> out = conn.stdoutHandler();
    for (int codePoint : codePoints) {
      if (codePoint < 32) {
        if (codePoint == '\t') {
          out.accept(new int[]{'\t'});
        } else if (codePoint == '\b') {
          out.accept(new int[]{'\b',' ','\b'});
        } else if (codePoint == '\r' || codePoint == '\n') {
          out.accept(new int[]{'\n'});
        } else {
          out.accept(new int[]{'^', codePoint + 64});
        }
      } else {
        if (codePoint == 127) {
          out.accept(new int[]{'\b',' ','\b'});
        } else {
          out.accept(new int[]{codePoint});
        }
      }
    }
  }

  void checkPending() {
    if (foregroundJob != null && ((TtyImpl)foregroundJob.getTty()).stdin != null && readline.hasEvent()) {
      ((TtyImpl)foregroundJob.getTty()).stdin.handle(Helper.fromCodePoints(readline.nextEvent().buffer().array()));
      vertx.runOnContext(v -> {
        checkPending();
      });
    }
  }

  public Job foregroundJob() {
    return foregroundJob;
  }

  public Set<Job> jobs() {
    return session.jobs();
  }

  public Job getJob(int id) {
    return session.getJob(id);
  }

  private void jobs(Readline readline) {
    session.jobs().forEach(job -> {
      String line = statusLine(job) + "\n";
      conn.write(line);
    });
    read(readline);
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

  void read(Readline readline) {
    readline.readline(conn, "% ", line -> {

      if (line == null) {
        // EOF
        conn.close();
        return;
      }

      List<CliToken> tokens = CliToken.tokenize(line);

      if (tokens.stream().filter(CliToken::isText).count() == 0) {
        // For now do like this
        read(readline);
        return;
      }

      Optional<CliToken> first = tokens.stream().filter(CliToken::isText).findFirst();
      if (first.isPresent()) {
        String name = first.get().value();
        switch (name) {
          case "exit":
          case "logout":
            // Todo: clean stuff before (like unterminated jobs, etc...)
            conn.close();
            return;
          case "jobs":
            jobs(readline);
            return;
          case "fg": {
            Job job = findJob();
            if (job == null) {
              conn.write("no such job\n");
              read(readline);
            } else {
              foregroundJob = job;
              echo(Helper.toCodePoints(job.line() + "\n"));
              if (job.status() == JobStatus.STOPPED) {
                ((TtyImpl)job.getTty()).stdout = conn::write; // We set stdout whether or not it's background (maybe do something different)
                job.resume();
              } else {
                // BG -> FG : nothing to do for now
              }
            }
            return;
          }
          case "bg": {
            Job job = findJob();
            if (job == null) {
              conn.write("no such job\n");
            } else {
              if (job.status() == JobStatus.STOPPED) {
                ((TtyImpl)job.getTty()).stdout = conn::write; // We set stdout whether or not it's background (maybe do something different)
                job.resume();
                echo(Helper.toCodePoints(statusLine(job) + "\n"));
              } else {
                conn.write("job " + job.id() + " already in background\n");
              }
            }
            read(readline);
            return;
          }
        }
      }

      Job job;
      try {
        job = session.createJob(tokens);
      } catch (Exception e) {
        echo(Helper.toCodePoints(e.getMessage() + "\n"));
        read(readline);
        return;
      }
      foregroundJob = job;
      TtyImpl tty = new TtyImpl(job);
      tty.stdout = conn::write;
      job.setTty(tty);
      job.run(status -> {
        if (foregroundJob == job) {
          foregroundJob = null;
          ((TtyImpl)job.getTty()).stdout = null;
          read(readline);
        }
      });
    }, completion -> {
      String line = Helper.fromCodePoints(completion.line());
      List<CliToken> tokens = Collections.unmodifiableList(CliToken.tokenize(line));
      Completion comp = new Completion() {

        @Override
        public Vertx vertx() {
          return vertx;
        }

        @Override
        public Session session() {
          return session.session();
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
            completion.suggest(candidates.stream().
                map(Helper::toCodePoints).
                collect(Collectors.toList()));
          } else {
            completion.end();
          }
        }

        @Override
        public void complete(String value, boolean terminal) {
          completion.complete(Helper.toCodePoints(value), terminal);
        }
      };
      registry.complete(comp);
    });
  }

  class TtyImpl implements Tty {

    final Job job;
    volatile Stream stdin;
    volatile Stream stdout;
    volatile Handler<Void> resizeHandler;

    public TtyImpl(Job job) {
      this.job = job;
    }

    @Override
    public String type() {
      return conn.terminalType();
    }

    @Override
    public int width() {
      return size() != null ? size().x() : -1;
    }

    @Override
    public int height() {
      return size() != null ? size().y() : -1;
    }

    @Override
    public Tty setStdin(Stream stdin) {
      this.stdin = stdin;
      if (foregroundJob == job) {
        checkPending();
      }
      return this;
    }

    @Override
    public Tty resizehandler(Handler<Void> handler) {
      resizeHandler = handler;
      return this;
    }

    @Override
    public Stream stdout() {
      return stdout;
    }
  }

  public void close() {
  }
}
