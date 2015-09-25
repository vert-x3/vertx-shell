package io.vertx.ext.shell.impl;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.ext.shell.Session;
import io.vertx.ext.shell.io.Stream;

import io.vertx.ext.shell.process.*;
import io.vertx.ext.shell.process.Process;
import io.vertx.ext.shell.io.Tty;

import java.util.HashMap;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Job {

  final int id;
  final Shell shell;
  final io.vertx.ext.shell.process.Process process;
  final String line;
  final HashMap<String, Handler<Void>> eventHandlers = new HashMap<>();
  volatile JobStatus status;
  volatile long lastStopped; // When the job was last stopped
  volatile Stream stdout;
  volatile Handler<String> stdin;

  public Job(int id, Shell shell, Process process, String line) {
    this.id = id;
    this.shell = shell;
    this.process = process;
    this.line = line;
  }

  boolean sendEvent(String event) {
    Handler<Void> handler = eventHandlers.get(event);
    if (handler != null) {
      handler.handle(null);
      return true;
    } else {
      return false;
    }
  }

  public JobStatus status() {
    return status;
  }

  public String line() {
    return line;
  }

  public String statusLine() {
    StringBuilder sb = new StringBuilder("[").append(id).append("]");
    if (shell.findJob() == this) {
      sb.append("+");
    }
    sb.append(" ").append(Character.toUpperCase(status.name().charAt(0))).append(status.name().substring(1).toLowerCase());
    sb.append(" ").append(line);
    return sb.toString();
  }

  public void run() {
    status = JobStatus.RUNNING;
    Context context = shell.vertx.getOrCreateContext(); // Maybe just a current context since it may run with SSHD
    Tty tty = new Tty() {
      @Override
      public int width() {
        return shell.size() != null ? shell.size().x() : -1;
      }
      @Override
      public int height() {
        return shell.size() != null ? shell.size().y() : -1;
      }
      @Override
      public Tty setStdin(Handler<String> stdin) {
        Job.this.stdin = stdin;
        if (shell.foregroundJob == Job.this) {
          shell.checkPending();
        }
        return this;
      }
      @Override
      public Tty setStdin(Stream stdin) {
        return setStdin((Handler<String>) stdin::write);
      }
      @Override
      public Stream stdout() {
        return Job.this.stdout;
      }
      @Override
      public Tty eventHandler(String event, Handler<Void> handler) {
        if (handler != null) {
          eventHandlers.put(event, handler);
        } else {
          eventHandlers.remove(event);
        }
        return this;
      }
    };
    ProcessContext processContext = new ProcessContext() {

      @Override
      public Tty tty() {
        return tty;
      }

      @Override
      public Session session() {
        return shell.session;
      }

      @Override
      public void end(int status) {
        Job.this.status = JobStatus.TERMINATED;
        shell.jobs.remove(Job.this.id);
        stdout = null;
        if (shell.foregroundJob == Job.this) {
          shell.foregroundJob = null;
          shell.read(shell.readline);
        }
      }
    };
    process.execute(processContext);
  }
}
