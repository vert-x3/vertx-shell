package io.vertx.ext.shell.getopt.impl;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class OptRequest {

  final Map<String, List<String>> options;
  final List<String> arguments;

  public OptRequest(Map<String, List<String>> options, List<String> arguments) {
    this.options = options;
    this.arguments = arguments;
  }

  public Map<String, List<String>> getOptions() {
    return options;
  }

  public List<String> getArguments() {
    return arguments;
  }
}
