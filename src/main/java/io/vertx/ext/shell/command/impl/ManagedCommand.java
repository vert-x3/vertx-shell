package io.vertx.ext.shell.command.impl;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.ext.shell.Stream;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.impl.Process;
import io.vertx.ext.shell.impl.ProcessContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The command registered with a manager.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ManagedCommand {

  final Context context;
  final CommandImpl command;
  final Handler<CommandProcess> handler;

  public ManagedCommand(Context context, CommandImpl command) {
    this.context = context;
    this.command = command;
    this.handler = command.handler;
  }

  public CommandImpl command() {
    return command;
  }

  public Process createProcess() {
    return createProcess(Collections.emptyMap(), Collections.emptyList());
  }

  public Process createProcess(Map<String, List<String>> options, List<String> arguments) {
    return new Process() {
      public void execute(ProcessContext context) {

        AtomicReference<Handler<String>> signalHandler = new AtomicReference<>();
        context.setSignalHandler(signal -> {
          Handler<String> handler = signalHandler.get();
          if (handler != null) {
            ManagedCommand.this.context.runOnContext(v -> {
              handler.handle(signal);
            });
          }
        });

        CommandProcess process = new CommandProcess() {

          @Override
          public List<String> arguments() {
            return arguments;
          }

          @Override
          public List<String> getOption(String name) {
            return options.get(name);
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
          public CommandProcess setSignalHandler(Handler<String> handler) {
            signalHandler.set(handler);
            return this;
          }
          @Override
          public void end(int code) {
            context.end(code);
          }
        };
        ManagedCommand.this.context.runOnContext(v -> {
          handler.handle(process);
          context.begin();
        });
      }
    };
  }
}
