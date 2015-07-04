package io.vertx.ext.shell.cli;

import java.util.stream.Stream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CliToken {

  final CliTokenKind kind;
  final String raw;
  final String value;

  public CliToken(CliTokenKind kind, String value) {
    this(kind, value, value);
  }

  public CliToken(CliTokenKind kind, String raw, String value) {
    this.kind = kind;
    this.raw = raw;
    this.value = value;
  }

  public static final CliToken EOF = new CliToken(CliTokenKind.EOF, "");

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
    if (obj instanceof CliToken) {
      CliToken that = (CliToken) obj;
      return kind == that.kind && value.equals(that.value);
    }
    return false;
  }

  @Override
  public String toString() {
    return "CliToken[kind=" + kind + ",value=" + value + "]";
  }

  public static Stream<CliToken> tokenize(String s) {

    Stream.Builder<CliToken> builder = Stream.builder();

    tokenize(s, 0, builder);

    return builder.build();

  }

  private static void tokenize(String s, int index, Stream.Builder<CliToken> builder) {
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
    builder.accept(EOF);
  }

  private static int textToken(String s, int index, Stream.Builder<CliToken> builder) {
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
    builder.accept(new CliToken(CliTokenKind.TEXT, s.substring(from, index), value.toString()));
    return index;
  }

  private static int blankToken(String s, int index, Stream.Builder<CliToken> builder) {
    int from = index;
    while (index < s.length() && isBlank(s.charAt(index))) {
      index++;
    }
    builder.accept(new CliToken(CliTokenKind.BLANK, s.substring(from, index)));
    return index;
  }

  private static int optToken(String s, int index, Stream.Builder<CliToken> builder) {
    if (index < s.length()) {
      char c = s.charAt(index);
      if (c == '-') {
        index = longOptToken(s, index + 1, builder);
      } else if (isBlank(c)) {
        builder.accept(new CliToken(CliTokenKind.TEXT, "-"));
      } else {
        int from = index;
        while (index < s.length() && !isBlank(s.charAt(index))) {
          index++;
        }
        builder.accept(new CliToken(CliTokenKind.OPT, s.substring(from, index)));
      }
    } else {
      builder.accept(new CliToken(CliTokenKind.TEXT, "-"));
    }
    return index;
  }

  private static int longOptToken(String s, int index, Stream.Builder<CliToken> builder) {
    if (index < s.length()) {
      char c = s.charAt(index);
      if (c == '-') {
        index = textToken(s, index - 2, builder);
      } else if (isBlank(c)) {
        builder.accept(new CliToken(CliTokenKind.TEXT, "--"));
      } else {
        int from = index;
        while (index < s.length() && !isBlank(s.charAt(index))) {
          index++;
        }
        builder.accept(new CliToken(CliTokenKind.LONG_OPT, s.substring(from, index)));
      }
    } else {
      builder.accept(new CliToken(CliTokenKind.TEXT, "--"));
    }
    return index;
  }

  private static boolean isBlank(char c) {
    return c == ' ' || c == '\t';
  }
}
