package io.vertx.ext.unit;

import io.vertx.ext.shell.command.ArgToken;
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
    assertTokens("-", ArgToken.createText("-"));
    assertTokens("- ", ArgToken.createText("-"), ArgToken.createBlank(" "));
    assertTokens("-\t", ArgToken.createText("-"), ArgToken.createBlank("\t"));
    assertTokens("--", ArgToken.createText("--"));
    assertTokens("-- ", ArgToken.createText("--"), ArgToken.createBlank(" "));
    assertTokens("--\t", ArgToken.createText("--"), ArgToken.createBlank("\t"));
    assertTokens("---", ArgToken.createText("---"));
    assertTokens("--- ", ArgToken.createText("---"), ArgToken.createBlank(" "));
    assertTokens("---\t", ArgToken.createText("---"), ArgToken.createBlank("\t"));
  }

  @Test
  public void testBlank() {
    assertTokens(" ", ArgToken.createBlank(" "));
    assertTokens("\t", ArgToken.createBlank("\t"));
    assertTokens(" \t", ArgToken.createBlank(" \t"));
    assertTokens("\t ", ArgToken.createBlank("\t "));
  }

  @Test
  public void testText() {
    assertTokens("a", ArgToken.createText("a"));
  }

  @Test
  public void testEscape() {
    assertTokens("\\a", ArgToken.createText("a"));
    assertTokens("\\\"", ArgToken.createText("\""));
    assertTokens("\\'", ArgToken.createText("'"));
    assertTokens("\"a\"", ArgToken.createText("a"));
    assertTokens("\" \"", ArgToken.createText(" "));
    assertTokens("\"\\a\"", ArgToken.createText("\\a"));
    assertTokens("\"\\\"\"", ArgToken.createText("\""));
    assertTokens("'a'", ArgToken.createText("a"));
    assertTokens("' '", ArgToken.createText(" "));
    assertTokens("'\\'", ArgToken.createText("\\"));
//    assertTokens("'", CliToken.Kind.TEXT.create("\\"));
  }

  private void assertTokens(String s, ArgToken... expected) {
    List<ArgToken> tokens = ArgToken.tokenize(s);
    assertEquals(Arrays.asList(expected), tokens);
  }
}
