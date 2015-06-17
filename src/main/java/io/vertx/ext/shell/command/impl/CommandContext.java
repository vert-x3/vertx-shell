package io.vertx.ext.shell.command.impl;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.ext.shell.Stream;
import io.vertx.ext.shell.command.Execution;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CommandContext {

  final Context context;
  final CommandImpl command;
  final Handler<Execution> handler;

  public CommandContext(Context context, CommandImpl command) {
    this.context = context;
    this.command = command;
    this.handler = command.handler;
  }

  public void execute(ExecutionContext processContext) {
    Execution execution = new Execution() {
      @Override
      public Execution setStdin(Stream stdin) {
        if (stdin != null) {
          processContext.setStdin(event -> context.runOnContext(v -> stdin.handle(event)));
        } else {
          processContext.setStdin(null);
        }
        return this;
      }
      @Override
      public Stream stdout() {
        return processContext.stdout();
      }
      @Override
      public Execution write(String text) {
        processContext.stdout().handle(text);
        return this;
      }
      @Override
      public void end(int code) {
        processContext.end(code);
      }
    };
    context.runOnContext(v -> {
      handler.handle(execution);
      processContext.begin();
    });
  }
}
