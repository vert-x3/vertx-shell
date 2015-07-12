package io.vertx.ext.unit;

import io.vertx.ext.shell.cli.CliToken;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ArgTokenTest {

  @Test
  public void testEmpty() {
    assertTokens("");
  }

  @Test
  public void testDashes() {
    assertTokens("-", CliToken.createText("-"));
    assertTokens("- ", CliToken.createText("-"), CliToken.createBlank(" "));
    assertTokens("-\t", CliToken.createText("-"), CliToken.createBlank("\t"));
    assertTokens("--", CliToken.createText("--"));
    assertTokens("-- ", CliToken.createText("--"), CliToken.createBlank(" "));
    assertTokens("--\t", CliToken.createText("--"), CliToken.createBlank("\t"));
    assertTokens("---", CliToken.createText("---"));
    assertTokens("--- ", CliToken.createText("---"), CliToken.createBlank(" "));
    assertTokens("---\t", CliToken.createText("---"), CliToken.createBlank("\t"));
  }

  @Test
  public void testBlank() {
    assertTokens(" ", CliToken.createBlank(" "));
    assertTokens("\t", CliToken.createBlank("\t"));
    assertTokens(" \t", CliToken.createBlank(" \t"));
    assertTokens("\t ", CliToken.createBlank("\t "));
  }

  @Test
  public void testText() {
    assertTokens("a", CliToken.createText("a"));
  }

  @Test
  public void testEscape() {
    assertTokens("\\a", CliToken.createText("a"));
    assertTokens("\\\"", CliToken.createText("\""));
    assertTokens("\\'", CliToken.createText("'"));
    assertTokens("\"a\"", CliToken.createText("a"));
    assertTokens("\" \"", CliToken.createText(" "));
    assertTokens("\"\\a\"", CliToken.createText("\\a"));
    assertTokens("\"\\\"\"", CliToken.createText("\""));
    assertTokens("'a'", CliToken.createText("a"));
    assertTokens("' '", CliToken.createText(" "));
    assertTokens("'\\'", CliToken.createText("\\"));
//    assertTokens("'", CliToken.Kind.TEXT.create("\\"));
  }

  private void assertTokens(String s, CliToken... expected) {
    List<CliToken> tokens = CliToken.tokenize(s);
    assertEquals(Arrays.asList(expected), tokens);
  }
}
