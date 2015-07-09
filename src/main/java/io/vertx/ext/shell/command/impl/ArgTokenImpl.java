package io.vertx.ext.shell.command.impl;

import io.termd.core.readline.Quote;
import io.termd.core.readline.QuoteResult;
import io.termd.core.readline.Quoter;
import io.vertx.ext.shell.command.ArgToken;

import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ArgTokenImpl implements ArgToken {

  final boolean text;
  final String raw;
  final String value;

  public ArgTokenImpl(boolean text, String value) {
    this(text, value, value);
  }

  public ArgTokenImpl(boolean text, String raw, String value) {
    this.text = text;
    this.raw = raw;
    this.value = value;
  }

  @Override
  public boolean isText() {
    return text;
  }

  @Override
  public boolean isBlank() {
    return !text;
  }

  public String raw() {
    return raw;
  }

  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ArgTokenImpl) {
      ArgTokenImpl that = (ArgTokenImpl) obj;
      return text == that.text && value.equals(that.value);
    }
    return false;
  }

  @Override
  public String toString() {
    return "CliToken[text=" + text + ",value=" + value + "]";
  }



  public static List<ArgToken> tokenize(String s) {

    List<ArgToken> tokens = new LinkedList<>();

    tokenize(s, 0, tokens);

    return tokens;

  }

  private static void tokenize(String s, int index, List<ArgToken> builder) {
    while (index < s.length()) {
      char c = s.charAt(index);
      switch (c) {
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

  private static int textToken(String s, int index, List<ArgToken> builder) {
    StringBuilder value = new StringBuilder();
    Quoter quoter = new Quoter();
    boolean escaped = false;
    int from = index;
    while (index < s.length()) {
      char c = s.charAt(index);
      QuoteResult result = quoter.update(c);
      if (quoter.getQuote() == Quote.NONE && isBlank(c)) {
        break;
      }
      switch (result) {
        case UPDATED:
          break;
        case ESC:
          escaped = true;
          break;
        case CODE_POINT:
          if (escaped) {
            if (quoter.getQuote() == Quote.WEAK) {
              if (c != '"' && c != '\\') {
                value.append('\\');
              }
            }
            escaped = false;
          }
          value.append(c);
          break;
      }
      index++;
    }
/*
    if (quoter.getQuote() != Quote.NONE || escaped) {
      // Todo
      throw new UnsupportedOperationException();
    }
*/
    builder.add(new ArgTokenImpl(true, s.substring(from, index), value.toString()));
    return index;
  }

  private static int blankToken(String s, int index, List<ArgToken> builder) {
    int from = index;
    while (index < s.length() && isBlank(s.charAt(index))) {
      index++;
    }
    builder.add(new ArgTokenImpl(false, s.substring(from, index)));
    return index;
  }

  private static boolean isBlank(char c) {
    return c == ' ' || c == '\t';
  }
}
