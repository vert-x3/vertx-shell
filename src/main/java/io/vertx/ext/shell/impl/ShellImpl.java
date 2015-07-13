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
import java.util.LinkedList;
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
    LinkedList<CliToken> tokens = new LinkedList<>(completion.lineTokens());

    // Remove any leading white space
    while (tokens.size() > 0 && tokens.getFirst().isBlank()) {
      tokens.removeFirst();
    }

    // > 1 means it's a text token followed by something else
    if (tokens.size() > 1) {
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
          if (command != null) {
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
          } else {
            completion.complete(Collections.emptyList());
          }
        }
      }
    } else {
      String prefix = tokens.size() > 0 ? tokens.getFirst().value() : "";
      List<String> names = manager.commands().
          stream().
          map(cmd -> cmd.command().name()).
          filter(name -> name.startsWith(prefix)).
          collect(Collectors.toList());
      if (names.size() == 1) {
        completion.complete(names.get(0).substring(prefix.length()), true);
      } else {
        String commonPrefix = Completion.findLongestCommonPrefix(names);
        if (commonPrefix.length() > prefix.length()) {
          completion.complete(commonPrefix.substring(prefix.length()), false);
        } else {
          completion.complete(names);
        }
      }
    }
  }
}
