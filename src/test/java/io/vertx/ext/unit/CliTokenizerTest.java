package io.vertx.ext.unit;

import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.cli.CliTokenKind;
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
    assertTokens("-", CliTokenKind.TEXT.create("-"), CliToken.EOF);
    assertTokens("- ", CliTokenKind.TEXT.create("-"), CliTokenKind.BLANK.create(" "), CliToken.EOF);
    assertTokens("-\t", CliTokenKind.TEXT.create("-"), CliTokenKind.BLANK.create("\t"), CliToken.EOF);
    assertTokens("--", CliTokenKind.TEXT.create("--"), CliToken.EOF);
    assertTokens("-- ", CliTokenKind.TEXT.create("--"), CliTokenKind.BLANK.create(" "), CliToken.EOF);
    assertTokens("--\t", CliTokenKind.TEXT.create("--"), CliTokenKind.BLANK.create("\t"), CliToken.EOF);
    assertTokens("---", CliTokenKind.TEXT.create("---"), CliToken.EOF);
    assertTokens("--- ", CliTokenKind.TEXT.create("---"), CliTokenKind.BLANK.create(" "), CliToken.EOF);
    assertTokens("---\t", CliTokenKind.TEXT.create("---"), CliTokenKind.BLANK.create("\t"), CliToken.EOF);
  }

  @Test
  public void testOpt() {
    assertTokens("-a", CliTokenKind.OPT.create("a"), CliToken.EOF);
    assertTokens("-ab", CliTokenKind.OPT.create("ab"), CliToken.EOF);
  }

  @Test
  public void testLongOpt() {
    assertTokens("--a", CliTokenKind.LONG_OPT.create("a"), CliToken.EOF);
    assertTokens("--ab", CliTokenKind.LONG_OPT.create("ab"), CliToken.EOF);
  }

  @Test
  public void testBlank() {
    assertTokens(" ", CliTokenKind.BLANK.create(" "), CliToken.EOF);
    assertTokens("\t", CliTokenKind.BLANK.create("\t"), CliToken.EOF);
    assertTokens(" \t", CliTokenKind.BLANK.create(" \t"), CliToken.EOF);
    assertTokens("\t ", CliTokenKind.BLANK.create("\t "), CliToken.EOF);
  }

  @Test
  public void testText() {
    assertTokens("a", CliTokenKind.TEXT.create("a"), CliToken.EOF);
  }

  @Test
  public void testEscape() {
    assertTokens("\\a", CliTokenKind.TEXT.create("a"), CliToken.EOF);
    assertTokens("\\\"", CliTokenKind.TEXT.create("\""), CliToken.EOF);
    assertTokens("\\'", CliTokenKind.TEXT.create("'"), CliToken.EOF);
    assertTokens("\"a\"", CliTokenKind.TEXT.create("a"), CliToken.EOF);
    assertTokens("\" \"", CliTokenKind.TEXT.create(" "), CliToken.EOF);
    assertTokens("\"\\a\"", CliTokenKind.TEXT.create("\\a"), CliToken.EOF);
    assertTokens("\"\\\"\"", CliTokenKind.TEXT.create("\""), CliToken.EOF);
    assertTokens("'a'", CliTokenKind.TEXT.create("a"), CliToken.EOF);
    assertTokens("' '", CliTokenKind.TEXT.create(" "), CliToken.EOF);
    assertTokens("'\\'", CliTokenKind.TEXT.create("\\"), CliToken.EOF);
//    assertTokens("'", CliToken.Kind.TEXT.create("\\"), CliToken.EOF);
  }

  private void assertTokens(String s, CliToken... expected) {
    Stream<CliToken> tokens = CliToken.tokenize(s);
    List<CliToken> list = tokens.collect(Collectors.toList());
    assertEquals(Arrays.asList(expected), list);
  }
}
