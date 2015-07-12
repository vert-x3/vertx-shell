package io.vertx.ext.shell.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.*;
import io.vertx.ext.shell.Job;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.command.CommandManager;
import io.vertx.ext.shell.command.impl.ManagedCommand;
import io.vertx.ext.shell.command.impl.CommandManagerImpl;
import io.vertx.ext.shell.cli.Completion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ShellImpl implements Shell {

  final Vertx vertx;
  final CommandManagerImpl manager;

  public ShellImpl(Vertx vertx, CommandManager manager) {
    this.vertx = vertx;
    this.manager = (CommandManagerImpl) manager;
  }

  @Override
  public void createJob(String s, Handler<AsyncResult<Job>> handler) {
    Process process;
    try {
      process = makeRequest(s);
    } catch (Exception e) {
      handler.handle(Future.failedFuture(e));
      return;
    }
    JobProcess job = new JobProcess(vertx, process);
    handler.handle(Future.succeededFuture(job));
  }

  private Process makeRequest(String s) {
    ListIterator<CliToken> tokens = CliToken.tokenize(s).listIterator();
    while (tokens.hasNext()) {
      CliToken token = tokens.next();
      if (token.isText()) {
        ManagedCommand command = manager.getCommand(token.value());
        if (command == null) {
          throw new IllegalArgumentException(token.value() + ": command not found");
        }
        List<CliToken> remaining = new ArrayList<>();
        while (tokens.hasNext()) {
          remaining.add(tokens.next());
        }
        return command.createProcess(remaining);
      }
    }
    throw new IllegalArgumentException();
  }

  @Override
  public void complete(Completion completion) {
    List<CliToken> tokens = new ArrayList<>(completion.lineTokens());
    if (tokens.stream().filter(CliToken::isText).count() == 1 && tokens.get(tokens.size() - 1).isText()) {
      CliToken last = tokens.get(tokens.size() - 1);
      List<String> names = manager.commands().
          stream().
          map(cmd -> cmd.command().name()).
          filter(name -> name.startsWith(last.value())).
          map(name -> name.substring(last.value().length())).
          collect(Collectors.toList());
      completion.complete(names);
      return;
    } else if (tokens.stream().filter(CliToken::isText).count() >= 1) {
      ListIterator<CliToken> it = tokens.listIterator();
      while (it.hasNext()) {
        CliToken ct = it.next();
        it.remove();
        if (ct.isText()) {
          List<CliToken> newTokens = new ArrayList<>();
          while (it.hasNext()) {
            newTokens.add(it.next());
          }
          StringBuilder tmp = new StringBuilder();
          newTokens.stream().forEach(token -> tmp.append(token.raw()));
          String line = tmp.toString();
          ManagedCommand command = manager.getCommand(ct.value());
          command.complete(new Completion() {
            @Override
            public String line() {
              return line;
            }
            @Override
            public List<CliToken> lineTokens() {
              return newTokens;
            }
            @Override
            public void complete(List<String> candidates) {
              completion.complete(candidates);
            }
            @Override
            public void complete(String value, boolean terminal) {
              completion.complete(value, terminal);
            }
          });
          return;
        }
      }
    }
    completion.complete(Collections.emptyList());
  }
}
