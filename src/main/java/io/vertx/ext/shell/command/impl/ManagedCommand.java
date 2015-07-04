package io.vertx.ext.shell.command.impl;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.ext.shell.Dimension;
import io.vertx.ext.shell.Stream;
import io.vertx.ext.shell.cli.CliParser;
import io.vertx.ext.shell.cli.CliRequest;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.impl.Process;
import io.vertx.ext.shell.impl.ProcessContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    return createProcess(Collections.emptyList());
  }

  public Process createProcess(List<CliToken> tokens) {

    CliParser parser = new CliParser(command);

    CliRequest req = parser.parse(tokens.listIterator());


    return new Process() {
      public void execute(ProcessContext context) {

        CommandProcess process = new CommandProcess() {

          @Override
          public List<String> arguments() {
            return req.getArguments();
          }

          @Override
          public List<String> getOption(String name) {
            return req.getOptions().get(name);
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
          handler.handle(process);
          context.begin();
        });
      }
    };
  }
}
