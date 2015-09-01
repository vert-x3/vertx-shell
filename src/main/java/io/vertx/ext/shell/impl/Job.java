package io.vertx.ext.shell.impl;

import io.termd.core.util.Helper;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.ext.shell.Dimension;
import io.vertx.ext.shell.Stream;

import io.vertx.ext.shell.process.*;
import io.vertx.ext.shell.process.Process;
import io.vertx.ext.shell.Tty;

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
  volatile Handler<Integer> endHandler;
  volatile JobStatus status;
  volatile long lastStopped; // When the job was last stopped
  volatile Stream stdout;
  volatile Stream stdin;

  public Job(int id, Shell shell, Process process, String line) {
    this.id = id;
    this.shell = shell;
    this.process = process;
    this.line = line;
  }

  void sendEvent(String event) {
    Handler<Void> handler = eventHandlers.get(event);
    if (handler != null) {
      handler.handle(null);
    }
  }

  public JobStatus status() {
    return status;
  }

  public String line() {
    return line;
  }

  public void run() {
    status = JobStatus.RUNNING;
    Context context = shell.vertx.getOrCreateContext(); // Maybe just a current context since it may run with SSHD
    Handler<Integer> endHandler = this.endHandler;
    Tty tty = new Tty() {
      @Override
      public Dimension windowSize() {
        return shell.size;
      }
      @Override
      public void setStdin(Stream stdin) {
        // Maybe translate the thread ?
        Job.this.stdin = stdin;
        if (shell.foregroundJob == Job.this) {
          if (stdin != null) {
            shell.readline.
                setReadHandler(codePoints -> stdin.handle(Helper.fromCodePoints(codePoints))).
                schedulePending();
          } else {
            shell.readline.setReadHandler(null);
          }
        }
      }
      @Override
      public Stream stdout() {
        Stream stdout = Job.this.stdout;
        if (stdout != null) {
          Stream tmp = stdout;
          stdout = txt -> {
            context.runOnContext(v ->
                tmp.handle(txt)
            );
          };
        }
        return stdout;
      }
      @Override
      public void eventHandler(String event, Handler<Void> handler) {
        if (handler != null) {
          eventHandlers.put(event, handler);
        } else {
          eventHandlers.remove(event);
        }
      }
    };
    ProcessContext processContext = new ProcessContext() {
      @Override
      public Tty tty() {
        return tty;
      }
      @Override
      public void end(int status) {
        Job.this.status = JobStatus.TERMINATED;
        shell.jobs.remove(Job.this.id);
        if (endHandler != null) {
          context.runOnContext(v -> endHandler.handle(status));
        }
      }
    };
    process.execute(processContext);
  }

  public void endHandler(Handler<Integer> handler) {
    endHandler = handler;
  }
}
