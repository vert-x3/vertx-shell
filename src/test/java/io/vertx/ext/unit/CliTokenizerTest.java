package io.vertx.ext.unit;

import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.cli.CliTokenKind;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CliTokenizerTest {

  @Test
  public void testEmpty() {
    assertTokens("");
  }

  @Test
  public void testDashes() {
    assertTokens("-", CliTokenKind.TEXT.create("-"));
    assertTokens("- ", CliTokenKind.TEXT.create("-"), CliTokenKind.BLANK.create(" "));
    assertTokens("-\t", CliTokenKind.TEXT.create("-"), CliTokenKind.BLANK.create("\t"));
    assertTokens("--", CliTokenKind.TEXT.create("--"));
    assertTokens("-- ", CliTokenKind.TEXT.create("--"), CliTokenKind.BLANK.create(" "));
    assertTokens("--\t", CliTokenKind.TEXT.create("--"), CliTokenKind.BLANK.create("\t"));
    assertTokens("---", CliTokenKind.TEXT.create("---"));
    assertTokens("--- ", CliTokenKind.TEXT.create("---"), CliTokenKind.BLANK.create(" "));
    assertTokens("---\t", CliTokenKind.TEXT.create("---"), CliTokenKind.BLANK.create("\t"));
  }

  @Test
  public void testOpt() {
    assertTokens("-a", CliTokenKind.OPT.create("a"));
    assertTokens("-ab", CliTokenKind.OPT.create("ab"));
  }

  @Test
  public void testLongOpt() {
    assertTokens("--a", CliTokenKind.LONG_OPT.create("a"));
    assertTokens("--ab", CliTokenKind.LONG_OPT.create("ab"));
  }

  @Test
  public void testBlank() {
    assertTokens(" ", CliTokenKind.BLANK.create(" "));
    assertTokens("\t", CliTokenKind.BLANK.create("\t"));
    assertTokens(" \t", CliTokenKind.BLANK.create(" \t"));
    assertTokens("\t ", CliTokenKind.BLANK.create("\t "));
  }

  @Test
  public void testText() {
    assertTokens("a", CliTokenKind.TEXT.create("a"));
  }

  @Test
  public void testEscape() {
    assertTokens("\\a", CliTokenKind.TEXT.create("a"));
    assertTokens("\\\"", CliTokenKind.TEXT.create("\""));
    assertTokens("\\'", CliTokenKind.TEXT.create("'"));
    assertTokens("\"a\"", CliTokenKind.TEXT.create("a"));
    assertTokens("\" \"", CliTokenKind.TEXT.create(" "));
    assertTokens("\"\\a\"", CliTokenKind.TEXT.create("\\a"));
    assertTokens("\"\\\"\"", CliTokenKind.TEXT.create("\""));
    assertTokens("'a'", CliTokenKind.TEXT.create("a"));
    assertTokens("' '", CliTokenKind.TEXT.create(" "));
    assertTokens("'\\'", CliTokenKind.TEXT.create("\\"));
//    assertTokens("'", CliToken.Kind.TEXT.create("\\"));
  }

  private void assertTokens(String s, CliToken... expected) {
    List<CliToken> tokens = CliToken.tokenize(s);
    assertEquals(Arrays.asList(expected), tokens);
  }
}
