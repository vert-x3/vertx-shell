package io.vertx.ext.shell.registry.impl;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.cli.CLIException;
import io.vertx.core.cli.CommandLine;
import io.vertx.core.cli.Option;
import io.vertx.ext.shell.Session;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.io.EventType;
import io.vertx.ext.shell.io.Stream;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.command.impl.CommandImpl;
import io.vertx.ext.shell.registry.CommandRegistration;
import io.vertx.ext.shell.process.Process;
import io.vertx.ext.shell.process.ProcessContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The command registered with a manager.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CommandRegistrationImpl implements CommandRegistration {

  final Vertx vertx;
  final Context context;
  final CommandImpl command;
  final Handler<CommandProcess> processHandler;
  final Handler<Completion> completionHandler;

  public CommandRegistrationImpl(Vertx vertx, Context context, CommandImpl command) {
    this.vertx = vertx;
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

        CommandLine cl;
        final List<String> args2 = args.stream().filter(CliToken::isText).map(CliToken::value).collect(Collectors.toList());
        if (command.cli() != null) {

          // Build to skip validation problems
          Optional<Option> helpOpt = command.cli().getOptions().stream().filter(o -> o.getArgName().equals("help")).findFirst();
          if (helpOpt.isPresent()) {
            if (command.cli().parse(args2, false).isSeenInCommandLine(helpOpt.get())) {
              StringBuilder usage = new StringBuilder();
              command.cli().usage(usage);
              usage.append('\n');
              context.tty().stdout().write(usage.toString());
              context.end(0);
              return;
            }
          }

          //
          try {
            cl = command.cli().parse(args2);
          } catch (CLIException e) {
            context.tty().stdout().write(e.getMessage() + "\n");
            context.end(0);
            return;
          }
        } else {
          cl = null;
        }

        CommandProcess process = new CommandProcess() {

          @Override
          public Vertx vertx() {
            return vertx;
          }

          @Override
          public CommandLine commandLine() {
            return cl;
          }

          @Override
          public List<CliToken> argsTokens() {
            return args;
          }

          @Override
          public List<String> args() {
            return args2;
          }

          @Override
          public Session session() {
            return context.session();
          }

          @Override
          public int width() {
            return context.tty().width();
          }

          @Override
          public int height() {
            return context.tty().height();
          }

          @Override
          public CommandProcess setStdin(Stream stdin) {
            Stream s;
            if (stdin != null) {
              s = Stream.ofObject(data -> CommandRegistrationImpl.this.context.runOnContext(v -> stdin.write(data)));
            } else {
              s = null;
            }
            context.tty().setStdin(s);
            return this;
          }

          @Override
          public CommandProcess setStdin(Handler<String> stdin) {
            return setStdin(Stream.ofString(stdin));
          }

          @Override
          public Stream stdout() {
            return context.tty().stdout();
          }

          @Override
          public CommandProcess write(String text) {
            context.tty().stdout().write(text);
            return this;
          }

          @Override
          public CommandProcess eventHandler(EventType eventType, Handler<Void> handler) {
            if (handler != null) {
              context.tty().eventHandler(eventType, v -> {
                CommandRegistrationImpl.this.context.runOnContext(v2 -> {
                  handler.handle(null);
                });
              });
            } else {
              context.tty().eventHandler(eventType, null);
            }
            return this;
          }

          @Override
          public void end() {
            context.end(0);
          }

          @Override
          public void end(int status) {
            context.end(status);
          }
        };
        CommandRegistrationImpl.this.context.runOnContext(v -> {
          processHandler.handle(process);
        });
      }
    };
  }
}
