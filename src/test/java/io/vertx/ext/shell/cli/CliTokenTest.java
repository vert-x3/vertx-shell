/*
 * Copyright 2015 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 *
 *
 * Copyright (c) 2015 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 *
 */

package io.vertx.ext.shell.cli;

import io.vertx.ext.shell.cli.CliToken;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CliTokenTest {

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
    assertTokens("\\ ", CliToken.createText(" "));
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
