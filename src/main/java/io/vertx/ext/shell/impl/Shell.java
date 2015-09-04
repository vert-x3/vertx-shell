package io.vertx.ext.shell.impl;

import io.termd.core.readline.Keymap;
import io.termd.core.readline.Readline;
import io.termd.core.tty.TtyConnection;
import io.termd.core.util.Helper;
import io.termd.core.util.Vector;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.registry.CommandRegistry;
import io.vertx.ext.shell.cli.CliToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Shell {

  final Vertx vertx;
  private final TtyConnection conn;
  final Readline readline;
  final CommandRegistry mgr;
  Vector size;
  Job foregroundJob; // The currently running job
  final SortedMap<Integer, Job> jobs = new TreeMap<>();
  String welcome;


  public Shell(Vertx vertx, TtyConnection conn, CommandRegistry mgr) {

    InputStream inputrc = Keymap.class.getResourceAsStream("inputrc");
    Keymap keymap = new Keymap(inputrc);
    Readline readline = new Readline(keymap);

    this.vertx = vertx;
    this.conn = conn;
    this.mgr = mgr;
    this.readline = readline;
  }

  public String welcome() {
    return welcome;
  }

  public void setWelcome(String welcome) {
    this.welcome = welcome;
  }

  public void init() {
    for (io.termd.core.readline.Function function : Helper.loadServices(Thread.currentThread().getContextClassLoader(), io.termd.core.readline.Function.class)) {
      readline.addFunction(function);
    }
    conn.setStdinHandler(codePoints -> {
      if (foregroundJob == null) {
        // Bug ???
      } else {
        if (foregroundJob.stdin != null) {
          // Forward
          foregroundJob.stdin.handle(Helper.fromCodePoints(codePoints));
        } else {
          // Echo
          echo(codePoints);
          readline.queueCodePoints(codePoints);
        }
      }
    });
    conn.setSizeHandler(resize -> {
      size = resize;
      Job job = foregroundJob;
      if (job != null) {
        job.sendEvent("SIGWINCH");
      }
    });
    conn.setEventHandler((event, cp) -> {
      Job job = foregroundJob;
      switch (event) {
        case INTR:
          if (!job.sendEvent("SIGINT")) {
            echo(cp);
            readline.queueEvent(event, cp);
          } else {
            echo(cp, '\n');
          }
          break;
        case EOT:
          // Pseudo signal
          if (!job.sendEvent("EOT")) {
            echo(cp);
            readline.queueEvent(event, cp);
          }
          break;
        case SUSP:
          echo(cp, '\n');
          echo(Helper.toCodePoints(job.statusLine() + "\n"));
          foregroundJob = null;
          job.stdout = null;
          job.status = JobStatus.STOPPED;
          job.sendEvent("SIGTSTP");
          read(readline);
          break;
      }
    });
    if (welcome != null) {
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
    if (foregroundJob != null && foregroundJob.stdin != null) {
      if (readline.hasCodePoints()) {
        foregroundJob.stdin.handle(Helper.fromCodePoints(readline.nextCodePoints()));
        vertx.runOnContext(v -> {
          checkPending();
        });
      }
    }
  }

  public Job foregroundJob() {
    return foregroundJob;
  }

  public Map<Integer, Job> jobs() {
    return jobs;
  }

  public Job getJob(int id) {
    return jobs.get(id);
  }

  private void jobs(Readline readline) {
    jobs.forEach((id, job) -> {
      String line = job.statusLine() + "\n";
      conn.write(line);
    });
    read(readline);
  }

  Job findJob() {
    return jobs.
        values().
        stream().
        filter(job -> job != foregroundJob).sorted((j1, j2) -> ((Long) j1.lastStopped).compareTo(j2.lastStopped)).
        findFirst().orElse(null);
  }

  void read(Readline readline) {
    readline.readline(conn, "% ", line -> {

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
            // Should clean stuff before!!!
            conn.close();
            break;
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
              echo(Helper.toCodePoints(job.line + "\n"));
              if (job.status == JobStatus.STOPPED) {
                job.stdout = conn::write; // We set stdout whether or not it's background (maybe do something different)
                job.status = JobStatus.RUNNING;
                job.sendEvent("SIGCONT");
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
              if (job.status == JobStatus.STOPPED) {
                job.stdout = conn::write; // We set stdout whether or not it's background (maybe do something different)
                job.status = JobStatus.RUNNING;
                job.sendEvent("SIGCONT");
                echo(Helper.toCodePoints(job.statusLine() + "\n"));
              } else {
                conn.write("job " + job.id + " already in background\n");
              }
            }
            read(readline);
            return;
          }
        }
      }

      mgr.createProcess(tokens, ar -> {
        if (ar.succeeded()) {
          int id = jobs.isEmpty() ? 1 : jobs.lastKey() + 1;
          Job job = new Job(id, this, ar.result(), line);
          foregroundJob = job;
          job.stdout = conn::write;
          jobs.put(id, job);
          job.run();
        } else {
          echo(Helper.toCodePoints(line + ": command not found\n"));
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
        public String line() {
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
          }
          completion.end();
        }

        @Override
        public void complete(String value, boolean terminal) {
          completion.complete(Helper.toCodePoints(value), terminal).end();
        }
      };
      mgr.complete(comp);
    });
  }

  public void close() {
  }
}
