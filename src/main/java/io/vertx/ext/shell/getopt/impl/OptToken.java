package io.vertx.ext.shell.getopt.impl;

import io.vertx.ext.shell.cli.CliToken;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class OptToken {

  final OptTokenKind kind;
  final String raw;
  final String value;

  public OptToken(OptTokenKind kind, String value) {
    this(kind, value, value);
  }

  public OptToken(OptTokenKind kind, String raw, String value) {
    this.kind = kind;
    this.raw = raw;
    this.value = value;
  }

  public OptTokenKind getKind() {
    return kind;
  }

  public String getRaw() {
    return raw;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof OptToken) {
      OptToken that = (OptToken) obj;
      return kind == that.kind && value.equals(that.value);
    }
    return false;
  }

  @Override
  public String toString() {
    return "OptToken[kind=" + kind + ",value=" + value + "]";
  }

  public static List<OptToken> tokenize(String s) {
    return tokenize(CliToken.tokenize(s));
  }

  public static List<OptToken> tokenize(List<CliToken> tokens) {
    return tokens.stream().map(t -> {
      OptTokenKind kind;
      String val = t.value();
      if (t.isBlank()) {
        kind = OptTokenKind.BLANK;
      } else {
        kind = OptTokenKind.TEXT;
        if (val.startsWith("-")) {
          if (val.startsWith("--")) {
            if (val.length() > 2 && val.charAt(2) != '-') {
              kind = OptTokenKind.LONG_OPT;
              val = val.substring(2);
            }
          } else {
            if (val.length() > 1 && val.charAt(1) != '-') {
              kind = OptTokenKind.OPT;
              val = val.substring(1);
            }
          }
        }
      }
      return new OptToken(kind, t.raw(), val);
    }).collect(Collectors.toList());
  }

  private static void tokenize(String s, int index, List<OptToken> builder) {
    while (index < s.length()) {
      char c = s.charAt(index);
      switch (c) {
        case '-':
          index = optToken(s, index + 1, builder);
          break;
        case ' ':
        case '\t':
          index = blankToken(s, index, builder);
          break;
        default:
          index = textToken(s, index, builder);
          break;
      }
    }
  }

  private static int textToken(String s, int index, List<OptToken> builder) {
    StringBuilder value = new StringBuilder();
    boolean escaped = false;
    int status = 0;
    int from = index;
    while (index < s.length()) {
      char c = s.charAt(index);
      if (!escaped) {
        if (c == '\\') {
          if (status != 2) {
            escaped = true;
          } else {
            value.append('\\');
          }
        } else {
          if (status == 0) {
            if (isBlank(c)) {
              break;
            } else if (c == '"') {
              status = 1;
            } else if (c == '\'') {
              status = 2;
            } else {
              value.append(c);
            }
          } else if (status == 1) {
            if (c == '"') {
              status = 0;
            } else {
              value.append(c);
            }
          } else {
            if (c == '\'') {
              status = 0;
            } else {
              value.append(c);
            }
          }
        }
      } else {
        if (status == 0) {
          value.append(c);
        } else {
          if (c == '"') {
            value.append('"');
          } else {
            value.append('\\').append(c);
          }
        }
        escaped = false;
      }
      index++;
    }
    if (escaped || status > 0) {
      // Todo
      throw new UnsupportedOperationException();
    }
    builder.add(new OptToken(OptTokenKind.TEXT, s.substring(from, index), value.toString()));
    return index;
  }

  private static int blankToken(String s, int index, List<OptToken> builder) {
    int from = index;
    while (index < s.length() && isBlank(s.charAt(index))) {
      index++;
    }
    builder.add(new OptToken(OptTokenKind.BLANK, s.substring(from, index)));
    return index;
  }

  private static int optToken(String s, int index, List<OptToken> builder) {
    if (index < s.length()) {
      char c = s.charAt(index);
      if (c == '-') {
        index = longOptToken(s, index + 1, builder);
      } else if (isBlank(c)) {
        builder.add(new OptToken(OptTokenKind.TEXT, "-"));
      } else {
        int from = index;
        while (index < s.length() && !isBlank(s.charAt(index))) {
          index++;
        }
        builder.add(new OptToken(OptTokenKind.OPT, s.substring(from, index)));
      }
    } else {
      builder.add(new OptToken(OptTokenKind.TEXT, "-"));
    }
    return index;
  }

  private static int longOptToken(String s, int index, List<OptToken> builder) {
    if (index < s.length()) {
      char c = s.charAt(index);
      if (c == '-') {
        index = textToken(s, index - 2, builder);
      } else if (isBlank(c)) {
        builder.add(new OptToken(OptTokenKind.TEXT, "--"));
      } else {
        int from = index;
        while (index < s.length() && !isBlank(s.charAt(index))) {
          index++;
        }
        builder.add(new OptToken(OptTokenKind.LONG_OPT, s.substring(from, index)));
      }
    } else {
      builder.add(new OptToken(OptTokenKind.TEXT, "--"));
    }
    return index;
  }

  private static boolean isBlank(char c) {
    return c == ' ' || c == '\t';
  }
}
