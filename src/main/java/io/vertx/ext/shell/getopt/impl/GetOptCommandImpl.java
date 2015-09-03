package io.vertx.ext.shell.getopt.impl;

import io.vertx.core.Handler;
import io.vertx.ext.shell.Stream;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.getopt.Option;
import io.vertx.ext.shell.getopt.GetOptCommand;
import io.vertx.ext.shell.getopt.GetOptCommandProcess;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class GetOptCommandImpl implements GetOptCommand {

  final String name;
  final HashMap<String, Option> options = new HashMap<>();
  volatile Handler<GetOptCommandProcess> handler;

  public GetOptCommandImpl(String name) {
    this.name = name;
  }

  @Override
  public void processHandler(Handler<GetOptCommandProcess> handler) {
    this.handler = handler;
  }

  @Override
  public GetOptCommandImpl addOption(Option option) {
    options.put(option.name(), option);
    return this;
  }

  @Override
  public Option getOption(String name) {
    return options.get(name);
  }

  @Override
  public Command build() {
    Command command = Command.command(name);
    Handler<GetOptCommandProcess> cp = handler;
    if (cp != null) {

      command.processHandler(a -> {
        List<OptToken> tokens = OptToken.tokenize(a.args());
        OptParser parser = new OptParser(options.values());
        OptRequest req = parser.parse(tokens.listIterator());
        cp.handle(new GetOptCommandProcess() {
          @Override
          public List<CliToken> args() {
            return a.args();
          }
          @Override
          public List<String> arguments() {
            return req.getArguments();
          }
          @Override
          public List<String> getOption(String name) {
            return req.getOptions().get(name);
          }
          @Override
          public int width() {
            return a.width();
          }
          @Override
          public int height() {
            return a.height();
          }
          @Override
          public GetOptCommandProcess setStdin(Stream stdin) {
            a.setStdin(stdin);
            return this;
          }
          @Override
          public GetOptCommandProcess eventHandler(String event, Handler<Void> handler) {
            a.eventHandler(event, handler);
            return this;
          }
          @Override
          public Stream stdout() {
            return a.stdout();
          }
          @Override
          public GetOptCommandProcess write(String text) {
            a.write(text);
            return this;
          }
          @Override
          public void end() {
            a.end();
          }
          @Override
          public void end(int status) {
            a.end(status);
          }
        });
      });

      // For now we complete with empty
      command.completeHandler(completion -> {
        completion.complete(Collections.emptyList());
      });
    }
    return command;
  }
}
