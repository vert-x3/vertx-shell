package io.vertx.ext.shell.command.impl;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.ext.shell.Stream;
import io.vertx.ext.shell.command.Execution;
import io.vertx.ext.shell.impl.Process;
import io.vertx.ext.shell.impl.ProcessContext;

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

  public CommandImpl command() {
    return command;
  }

  public Process createProcess() {
    return new Process() {
      public void execute(ProcessContext context) {
        Execution execution = new Execution() {
          @Override
          public Execution setStdin(Stream stdin) {
            if (stdin != null) {
              context.setStdin(event -> CommandContext.this.context.runOnContext(v -> stdin.handle(event)));
            } else {
              context.setStdin(null);
            }
            return this;
          }
          @Override
          public Stream stdout() {
            return context.stdout();
          }
          @Override
          public Execution write(String text) {
            context.stdout().handle(text);
            return this;
          }
          @Override
          public void end(int code) {
            context.end(code);
          }
        };
        CommandContext.this.context.runOnContext(v -> {
          handler.handle(execution);
          context.begin();
        });
      }
    };
  }
}
