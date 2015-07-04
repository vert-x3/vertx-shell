package io.vertx.ext.shell.cli.impl;

import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.cli.CliTokenKind;

import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CliTokenImpl implements CliToken {

  final CliTokenKind kind;
  final String raw;
  final String value;

  public CliTokenImpl(CliTokenKind kind, String value) {
    this(kind, value, value);
  }

  public CliTokenImpl(CliTokenKind kind, String raw, String value) {
    this.kind = kind;
    this.raw = raw;
    this.value = value;
  }

  public CliTokenKind getKind() {
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
    if (obj instanceof CliTokenImpl) {
      CliTokenImpl that = (CliTokenImpl) obj;
      return kind == that.kind && value.equals(that.value);
    }
    return false;
  }

  @Override
  public String toString() {
    return "CliToken[kind=" + kind + ",value=" + value + "]";
  }

  public static List<CliToken> tokenize(String s) {

    List<CliToken> tokens = new LinkedList<>();

    tokenize(s, 0, tokens);

    return tokens;

  }

  private static void tokenize(String s, int index, List<CliToken> builder) {
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

  private static int textToken(String s, int index, List<CliToken> builder) {
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
    builder.add(new CliTokenImpl(CliTokenKind.TEXT, s.substring(from, index), value.toString()));
    return index;
  }

  private static int blankToken(String s, int index, List<CliToken> builder) {
    int from = index;
    while (index < s.length() && isBlank(s.charAt(index))) {
      index++;
    }
    builder.add(new CliTokenImpl(CliTokenKind.BLANK, s.substring(from, index)));
    return index;
  }

  private static int optToken(String s, int index, List<CliToken> builder) {
    if (index < s.length()) {
      char c = s.charAt(index);
      if (c == '-') {
        index = longOptToken(s, index + 1, builder);
      } else if (isBlank(c)) {
        builder.add(new CliTokenImpl(CliTokenKind.TEXT, "-"));
      } else {
        int from = index;
        while (index < s.length() && !isBlank(s.charAt(index))) {
          index++;
        }
        builder.add(new CliTokenImpl(CliTokenKind.OPT, s.substring(from, index)));
      }
    } else {
      builder.add(new CliTokenImpl(CliTokenKind.TEXT, "-"));
    }
    return index;
  }

  private static int longOptToken(String s, int index, List<CliToken> builder) {
    if (index < s.length()) {
      char c = s.charAt(index);
      if (c == '-') {
        index = textToken(s, index - 2, builder);
      } else if (isBlank(c)) {
        builder.add(new CliTokenImpl(CliTokenKind.TEXT, "--"));
      } else {
        int from = index;
        while (index < s.length() && !isBlank(s.charAt(index))) {
          index++;
        }
        builder.add(new CliTokenImpl(CliTokenKind.LONG_OPT, s.substring(from, index)));
      }
    } else {
      builder.add(new CliTokenImpl(CliTokenKind.TEXT, "--"));
    }
    return index;
  }

  private static boolean isBlank(char c) {
    return c == ' ' || c == '\t';
  }
}
