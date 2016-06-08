/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.rxjava.ext.shell.cli;

import java.util.Map;
import rx.Observable;
import java.util.List;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.shell.session.Session;

/**
 * The completion object
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.cli.Completion original} non RX-ified interface using Vert.x codegen.
 */

public class Completion {

  final io.vertx.ext.shell.cli.Completion delegate;

  public Completion(io.vertx.ext.shell.cli.Completion delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * @return the current Vert.x instance
   * @return 
   */
  public Vertx vertx() { 
    Vertx ret = Vertx.newInstance(delegate.vertx());
    return ret;
  }

  /**
   * @return the shell current session, useful for accessing data like the current path for file completion, etc...
   * @return 
   */
  public Session session() { 
    Session ret = Session.newInstance(delegate.session());
    return ret;
  }

  /**
   * @return the current line being completed in raw format, i.e without any char escape performed
   * @return 
   */
  public String rawLine() { 
    String ret = delegate.rawLine();
    return ret;
  }

  /**
   * @return the current line being completed as preparsed tokens
   * @return 
   */
  public List<CliToken> lineTokens() { 
    List<CliToken> ret = delegate.lineTokens().stream().map(elt -> CliToken.newInstance(elt)).collect(java.util.stream.Collectors.toList());
    return ret;
  }

  /**
   * End the completion with a list of candidates, these candidates will be displayed by the shell on the console.
   * @param candidates the candidates
   */
  public void complete(List<String> candidates) { 
    delegate.complete(candidates);
  }

  /**
   * End the completion with a value that will be inserted to complete the line.
   * @param value the value to complete with
   * @param terminal true if the value is terminal, i.e can be further completed
   */
  public void complete(String value, boolean terminal) { 
    delegate.complete(value, terminal);
  }


  public static Completion newInstance(io.vertx.ext.shell.cli.Completion arg) {
    return arg != null ? new Completion(arg) : null;
  }
}
