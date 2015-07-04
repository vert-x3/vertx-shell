package io.vertx.ext.shell.getopt.impl;

import io.vertx.ext.shell.getopt.Option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class OptParser {

  final Map<String, Option> optionMap;

  public OptParser(Option... options) {
    this(Arrays.asList(options));
  }

  public OptParser(Collection<Option> options) {
    this.optionMap = options.stream().collect(Collectors.toMap(Option::name, o -> o));
  }

  public OptRequest parse(String s) {
    return parse(OptToken.tokenize(s).listIterator());
  }

  public OptRequest parse(ListIterator<OptToken> tokens) {
    HashMap<String, List<String>> optionMap = new HashMap<>();
    while (tokens.hasNext()) {
      OptToken token = tokens.next();
      OptTokenKind kind = token.getKind();
      if (kind == OptTokenKind.OPT || kind == OptTokenKind.LONG_OPT) {
        String optionName = token.getValue();
        Option option = this.optionMap.get(optionName);
        if (option == null) {
          throw new IllegalArgumentException("Unrecognized option " + optionName);
        }
        List<String> values = parseOption(tokens, option);
        optionMap.put(optionName, values);
      } else if (kind == OptTokenKind.TEXT) {
        tokens.previous();
        break;
      }
    }
    ArrayList<String> arguments = new ArrayList<>();
    while (tokens.hasNext()) {
      OptToken token = tokens.next();
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
    return new OptRequest(optionMap, arguments);
  }

  private List<String> parseOption(Iterator<OptToken> tokens, Option option) {
    List<String> values = new ArrayList<>();
    int arity = option.arity();
    while (arity > 0) {
      if (!tokens.hasNext()) {
        throw new IllegalArgumentException("Missing option value " + option.name());
      }
      OptToken token = tokens.next();
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
