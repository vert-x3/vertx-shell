package io.vertx.ext.shell.impl;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.impl.ExecutionContext;
import io.vertx.ext.shell.Signal;
import io.vertx.ext.shell.Stream;
import io.vertx.ext.shell.command.impl.CommandContext;

import io.vertx.ext.shell.Process;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ProcessImpl implements Process {

  final Vertx vertx;
  final CommandContext commandContext;
  volatile Handler<Integer> endHandler;
  volatile Stream stdin;
  volatile Stream stdout;

  public ProcessImpl(Vertx vertx, CommandContext commandContext) {
    this.vertx = vertx;
    this.commandContext = commandContext;
  }

  @Override
  public Stream stdin() {
    return stdin;
  }

  @Override
  public void setStdout(Stream stdout) {
    this.stdout = stdout;
  }

  @Override
  public void run() {
    run(v -> {});
  }

  @Override
  public void run(Handler<Void> beginHandler) {
    Context context = vertx.getOrCreateContext();
    Handler<Integer> endHandler = this.endHandler;
    ExecutionContext processContext = new ExecutionContext() {
      @Override
      public void begin() {
        context.runOnContext(beginHandler);
      }
      @Override
      public ExecutionContext setStdin(Stream stdin) {
        ProcessImpl.this.stdin = stdin;
        return this;
      }
      @Override
      public Stream stdout() {
        return txt -> {
          context.runOnContext(v -> {
            Stream abc = stdout;
            if (abc != null) {
              abc.handle(txt);
            }
          });
        };
      }
      @Override
      public void end(int code) {
        if (endHandler != null) {
          context.runOnContext(v -> endHandler.handle(code));
        }
      }
    };
    commandContext.execute(processContext);
  }

  @Override
  public void sendSignal(Signal signal) {

  }

  @Override
  public void endHandler(Handler<Integer> handler) {
    endHandler = handler;
  }
}
