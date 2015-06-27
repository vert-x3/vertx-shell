package io.vertx.ext.unit;

import io.vertx.ext.shell.command.impl.CliToken;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CliTokenizerTest {

  @Test
  public void testEmpty() {
    assertTokens("", CliToken.EOF);
  }

  @Test
  public void testDashes() {
    assertTokens("-", CliToken.Kind.TEXT.create("-"), CliToken.EOF);
    assertTokens("- ", CliToken.Kind.TEXT.create("-"), CliToken.Kind.BLANK.create(" "), CliToken.EOF);
    assertTokens("-\t", CliToken.Kind.TEXT.create("-"), CliToken.Kind.BLANK.create("\t"), CliToken.EOF);
    assertTokens("--", CliToken.Kind.TEXT.create("--"), CliToken.EOF);
    assertTokens("-- ", CliToken.Kind.TEXT.create("--"), CliToken.Kind.BLANK.create(" "), CliToken.EOF);
    assertTokens("--\t", CliToken.Kind.TEXT.create("--"), CliToken.Kind.BLANK.create("\t"), CliToken.EOF);
    assertTokens("---", CliToken.Kind.TEXT.create("---"), CliToken.EOF);
    assertTokens("--- ", CliToken.Kind.TEXT.create("---"), CliToken.Kind.BLANK.create(" "), CliToken.EOF);
    assertTokens("---\t", CliToken.Kind.TEXT.create("---"), CliToken.Kind.BLANK.create("\t"), CliToken.EOF);
  }

  @Test
  public void testOpt() {
    assertTokens("-a", CliToken.Kind.OPT.create("a"), CliToken.EOF);
    assertTokens("-ab", CliToken.Kind.OPT.create("ab"), CliToken.EOF);
  }

  @Test
  public void testLongOpt() {
    assertTokens("--a", CliToken.Kind.LONG_OPT.create("a"), CliToken.EOF);
    assertTokens("--ab", CliToken.Kind.LONG_OPT.create("ab"), CliToken.EOF);
  }

  @Test
  public void testBlank() {
    assertTokens(" ", CliToken.Kind.BLANK.create(" "), CliToken.EOF);
    assertTokens("\t", CliToken.Kind.BLANK.create("\t"), CliToken.EOF);
    assertTokens(" \t", CliToken.Kind.BLANK.create(" \t"), CliToken.EOF);
    assertTokens("\t ", CliToken.Kind.BLANK.create("\t "), CliToken.EOF);
  }

  @Test
  public void testText() {
    assertTokens("a", CliToken.Kind.TEXT.create("a"), CliToken.EOF);
  }

  @Test
  public void testEscape() {
    assertTokens("\\a", CliToken.Kind.TEXT.create("a"), CliToken.EOF);
    assertTokens("\\\"", CliToken.Kind.TEXT.create("\""), CliToken.EOF);
    assertTokens("\\'", CliToken.Kind.TEXT.create("'"), CliToken.EOF);
    assertTokens("\"a\"", CliToken.Kind.TEXT.create("a"), CliToken.EOF);
    assertTokens("\" \"", CliToken.Kind.TEXT.create(" "), CliToken.EOF);
    assertTokens("\"\\a\"", CliToken.Kind.TEXT.create("\\a"), CliToken.EOF);
    assertTokens("\"\\\"\"", CliToken.Kind.TEXT.create("\""), CliToken.EOF);
    assertTokens("'a'", CliToken.Kind.TEXT.create("a"), CliToken.EOF);
    assertTokens("' '", CliToken.Kind.TEXT.create(" "), CliToken.EOF);
    assertTokens("'\\'", CliToken.Kind.TEXT.create("\\"), CliToken.EOF);
//    assertTokens("'", CliToken.Kind.TEXT.create("\\"), CliToken.EOF);
  }

  private void assertTokens(String s, CliToken... expected) {
    Stream<CliToken> tokens = CliToken.tokenize(s);
    List<CliToken> list = tokens.collect(Collectors.toList());
    assertEquals(Arrays.asList(expected), list);
  }
}
