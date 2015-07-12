package io.vertx.ext.shell.command.impl;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.Dimension;
import io.vertx.ext.shell.Stream;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.impl.Process;
import io.vertx.ext.shell.impl.ProcessContext;

import java.util.Collections;
import java.util.List;

/**
 * The command registered with a manager.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ManagedCommand {

  final Context context;
  final CommandImpl command;
  final Handler<CommandProcess> processHandler;
  final Handler<Completion> completionHandler;

  public ManagedCommand(Context context, CommandImpl command) {
    this.context = context;
    this.command = command;
    this.processHandler = command.processHandler;
    this.completionHandler = command.completeHandler;
  }

  public CommandImpl command() {
    return command;
  }

  public Process createProcess() {
    return createProcess(Collections.emptyList());
  }

  public void complete(Completion completion) {
    if (completionHandler != null) {
      context.runOnContext(v -> completionHandler.handle(completion));
    } else {
      completion.complete(Collections.emptyList());
    }
  }

  public Process createProcess(List<CliToken> args) {
    return new Process() {
      public void execute(ProcessContext context) {

        CommandProcess process = new CommandProcess() {

          @Override
          public List<CliToken> args() {
            return args;
          }

          @Override
          public Dimension windowSize() {
            return context.windowSize();
          }

          @Override
          public CommandProcess setStdin(Stream stdin) {
            if (stdin != null) {
              context.setStdin(event -> ManagedCommand.this.context.runOnContext(v -> stdin.handle(event)));
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
          public CommandProcess write(String text) {
            context.stdout().handle(text);
            return this;
          }
          @Override
          public CommandProcess eventHandler(String event, Handler<Void> handler) {
            if (handler != null) {
              context.eventHandler(event, v -> {
                ManagedCommand.this.context.runOnContext(v2 -> {
                  handler.handle(null);
                });
              });
            } else {
              context.eventHandler(event, null);
            }
            return this;
          }
          @Override
          public void end(int code) {
            context.end(code);
          }
        };
        ManagedCommand.this.context.runOnContext(v -> {
          processHandler.handle(process);
          context.begin();
        });
      }
    };
  }
}
