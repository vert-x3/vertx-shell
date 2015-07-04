package io.vertx.ext.unit;

import io.vertx.ext.shell.getopt.impl.OptToken;
import io.vertx.ext.shell.getopt.impl.OptTokenKind;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class OptTokenTest {

  @Test
  public void testEmpty() {
    assertTokens("");
  }

  @Test
  public void testDashes() {
    assertTokens("-", OptTokenKind.TEXT.create("-"));
    assertTokens("- ", OptTokenKind.TEXT.create("-"), OptTokenKind.BLANK.create(" "));
    assertTokens("-\t", OptTokenKind.TEXT.create("-"), OptTokenKind.BLANK.create("\t"));
    assertTokens("--", OptTokenKind.TEXT.create("--"));
    assertTokens("-- ", OptTokenKind.TEXT.create("--"), OptTokenKind.BLANK.create(" "));
    assertTokens("--\t", OptTokenKind.TEXT.create("--"), OptTokenKind.BLANK.create("\t"));
    assertTokens("---", OptTokenKind.TEXT.create("---"));
    assertTokens("--- ", OptTokenKind.TEXT.create("---"), OptTokenKind.BLANK.create(" "));
    assertTokens("---\t", OptTokenKind.TEXT.create("---"), OptTokenKind.BLANK.create("\t"));
  }

  @Test
  public void testOpt() {
    assertTokens("-a", OptTokenKind.OPT.create("a"));
    assertTokens("-ab", OptTokenKind.OPT.create("ab"));
  }

  @Test
  public void testLongOpt() {
    assertTokens("--a", OptTokenKind.LONG_OPT.create("a"));
    assertTokens("--ab", OptTokenKind.LONG_OPT.create("ab"));
  }

  @Test
  public void testBlank() {
    assertTokens(" ", OptTokenKind.BLANK.create(" "));
    assertTokens("\t", OptTokenKind.BLANK.create("\t"));
    assertTokens(" \t", OptTokenKind.BLANK.create(" \t"));
    assertTokens("\t ", OptTokenKind.BLANK.create("\t "));
  }

  @Test
  public void testText() {
    assertTokens("a", OptTokenKind.TEXT.create("a"));
  }

  @Test
  public void testEscape() {
    assertTokens("\\a", OptTokenKind.TEXT.create("a"));
    assertTokens("\\\"", OptTokenKind.TEXT.create("\""));
    assertTokens("\\'", OptTokenKind.TEXT.create("'"));
    assertTokens("\"a\"", OptTokenKind.TEXT.create("a"));
    assertTokens("\" \"", OptTokenKind.TEXT.create(" "));
    assertTokens("\"\\a\"", OptTokenKind.TEXT.create("\\a"));
    assertTokens("\"\\\"\"", OptTokenKind.TEXT.create("\""));
    assertTokens("'a'", OptTokenKind.TEXT.create("a"));
    assertTokens("' '", OptTokenKind.TEXT.create(" "));
    assertTokens("'\\'", OptTokenKind.TEXT.create("\\"));
//    assertTokens("'", OptToken.Kind.TEXT.create("\\"));
  }

  private void assertTokens(String s, OptToken... expected) {
    List<OptToken> tokens = OptToken.tokenize(s);
    assertEquals(Arrays.asList(expected), tokens);
  }
}
