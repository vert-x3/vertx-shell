package io.vertx.ext.shell.impl;

import io.termd.core.readline.KeyDecoder;
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
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Shell {

  final Vertx vertx;
  final TtyConnection conn;
  final Readline readline;
  final CommandRegistry mgr;
  Vector size;
  Job foregroundJob;
  final SortedMap<Integer, Job> jobs = new TreeMap<>();
  String welcome;


  public Shell(Vertx vertx, TtyConnection conn, CommandRegistry mgr) {

    InputStream inputrc = KeyDecoder.class.getResourceAsStream("inputrc");
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
    readline.setSizeHandler(resize -> {
      size = resize;
      Job job = foregroundJob;
      if (job != null) {
        job.sendEvent("SIGWINCH");
      }
    });
    readline.setEventHandler(event -> {
      Job job = foregroundJob;
      switch (event) {
        case INTR:
          if (job != null) {
            job.sendEvent("SIGINT");
          } else {
            // New line
          }
          break;
        case EOT:
          if (job != null) {
            // Pseudo signal
            job.sendEvent("EOT");
          }  else {
            // Disconnect if not handled
            conn.close();
          }
          break;
        case SUSP:
          if (job != null) {
            foregroundJob = null;
            readline.setReadHandler(null);
            job.stdout = null;
            job.status = JobStatus.STOPPED;
            job.sendEvent("SIGTSTP");
            read(readline);
          }
          break;
      }
    });
    readline.install(conn);
    if (welcome != null) {
      conn.write(welcome);
    }
    read(readline);
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
      String line = "[" + id + "] " + job.line + " \n";
      conn.write(line);
    });
    read(readline);
  }

  private void resume(Readline readline, boolean toForeground) {
    Optional<Job> j = jobs.values().stream().filter(job -> job != foregroundJob).sorted((j1, j2) -> ((Long) j1.lastStopped).compareTo(j2.lastStopped)).findFirst();
    if (j.isPresent()) {
      // A bit hackish, find a better way to do that
      Job job = j.get();
      if (toForeground) {
        foregroundJob = job;
        if (job.stdin != null) {
          readline.setReadHandler(codePoints -> job.stdin.handle(Helper.fromCodePoints(codePoints))).schedulePending();
        }
      }
      job.stdout = conn::write; // We should somehow buffer the output
      job.status = JobStatus.RUNNING;
      job.sendEvent("SIGCONT");
      if (!toForeground) {
        read(readline);
      }
    } else {
      conn.write("no such job\n");
      read(readline);
    }
  }

  private void read(Readline readline) {
    readline.readline("% ", line -> {

      List<CliToken> tokens = CliToken.tokenize(line);

      Optional<CliToken> first = tokens.stream().filter(CliToken::isText).findFirst();
      if (first.isPresent()) {
        String name = first.get().value();
        switch (name) {
          case "jobs":
            jobs(readline);
            return;
          case "fg":
            resume(readline, true);
            return;
          case "bg":
            resume(readline, false);
            return;
        }
      }

      mgr.createProcess(tokens, ar -> {
        if (ar.succeeded()) {
          int id = jobs.isEmpty() ? 1 : jobs.lastKey() + 1;
          Job job = new Job(id, this, ar.result(), line);
          foregroundJob = job;
          job.endHandler(code -> {
            foregroundJob = null;
            readline.setReadHandler(null);
            job.stdout = null;
            read(readline);
          });
          job.stdout = conn::write;
          jobs.put(id, job);
          job.run();


        } else {
          ar.cause().printStackTrace(new PrintWriter(new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
              conn.write(new String(cbuf, off, len));
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
          }));
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
    readline.schedulePending();
  }

  public void close() {
    readline.uninstall();
  }
}
