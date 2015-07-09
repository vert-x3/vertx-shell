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

package io.vertx.rxjava.ext.shell.completion;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.completion.Entry original} non RX-ified interface using Vert.x codegen.
 */

public class Entry {

  final io.vertx.ext.shell.completion.Entry delegate;

  public Entry(io.vertx.ext.shell.completion.Entry delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static Entry entry(boolean terminal, String value) { 
    Entry ret= Entry.newInstance(io.vertx.ext.shell.completion.Entry.entry(terminal, value));
    return ret;
  }

  public static Entry terminalEntry(String value) { 
    Entry ret= Entry.newInstance(io.vertx.ext.shell.completion.Entry.terminalEntry(value));
    return ret;
  }

  public static Entry nonTerminalEntry(String value) { 
    Entry ret= Entry.newInstance(io.vertx.ext.shell.completion.Entry.nonTerminalEntry(value));
    return ret;
  }

  public String value() { 
    String ret = this.delegate.value();
    return ret;
  }

  public boolean isTerminal() { 
    boolean ret = this.delegate.isTerminal();
    return ret;
  }


  public static Entry newInstance(io.vertx.ext.shell.completion.Entry arg) {
    return arg != null ? new Entry(arg) : null;
  }
}
