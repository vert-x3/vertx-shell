package io.vertx.ext.shell.cli;

import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.Option;
import io.vertx.ext.shell.command.impl.CommandImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CliParser {

  final Command command;

  public CliParser(CommandImpl command) {
    this.command = command;
  }

  public CliRequest parse(String s) {
    return parse(CliToken.tokenize(s).collect(Collectors.toList()).listIterator());
  }

  public CliRequest parse(ListIterator<CliToken> tokens) {
    HashMap<String, List<String>> optionMap = new HashMap<>();
    while (tokens.hasNext()) {
      CliToken token = tokens.next();
      CliTokenKind kind = token.getKind();
      if (kind == CliTokenKind.OPT || kind == CliTokenKind.LONG_OPT) {
        String optionName = token.getValue();
        Option option = command.getOption(optionName);
        if (option == null) {
          throw new IllegalArgumentException("Unrecognized option " + optionName);
        }
        List<String> values = parseOption(tokens, option);
        optionMap.put(optionName, values);
      } else if (kind == CliTokenKind.TEXT) {
        tokens.previous();
        break;
      }
    }
    ArrayList<String> arguments = new ArrayList<>();
    while (tokens.hasNext()) {
      CliToken token = tokens.next();
      switch (token.getKind()) {
        case LONG_OPT:
          arguments.add("--" + token.getValue());
          break;
        case OPT:
          arguments.add("-" + token.getValue());
          break;
        case TEXT:
          arguments.add(token.getValue());
          break;
      }
    }
    return new CliRequest(optionMap, arguments);
  }

  private List<String> parseOption(Iterator<CliToken> tokens, Option option) {
    List<String> values = new ArrayList<>();
    int arity = option.arity();
    while (arity > 0) {
      if (!tokens.hasNext()) {
        throw new IllegalArgumentException("Missing option value " + option.name());
      }
      CliToken token = tokens.next();
      switch (token.getKind()) {
        case TEXT:
          values.add(token.getValue());
          arity--;
          break;
        case BLANK:
          break;
        default:
          throw new IllegalArgumentException("Missing option value " + option.name());
      }
    }
    return values;
  }

}
