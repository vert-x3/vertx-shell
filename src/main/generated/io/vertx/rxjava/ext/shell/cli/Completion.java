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
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
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

  public String line() { 
    String ret = this.delegate.line();
    return ret;
  }

  public List<CliToken> lineTokens() { 
    List<CliToken> ret = this.delegate.lineTokens().stream().map(CliToken::newInstance).collect(java.util.stream.Collectors.toList());
    return ret;
  }

  public void complete(List<String> candidates) { 
    this.delegate.complete(candidates);
  }

  public void complete(String value, boolean terminal) { 
    this.delegate.complete(value, terminal);
  }


  public static Completion newInstance(io.vertx.ext.shell.cli.Completion arg) {
    return arg != null ? new Completion(arg) : null;
  }
}
