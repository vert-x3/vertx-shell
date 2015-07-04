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

package io.vertx.rxjava.ext.shell.command;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.command.ArgToken original} non RX-ified interface using Vert.x codegen.
 */

public class ArgToken {

  final io.vertx.ext.shell.command.ArgToken delegate;

  public ArgToken(io.vertx.ext.shell.command.ArgToken delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static ArgToken createText(String s) { 
    ArgToken ret= ArgToken.newInstance(io.vertx.ext.shell.command.ArgToken.createText(s));
    return ret;
  }

  public static ArgToken createBlank(String s) { 
    ArgToken ret= ArgToken.newInstance(io.vertx.ext.shell.command.ArgToken.createBlank(s));
    return ret;
  }

  public String raw() { 
    String ret = this.delegate.raw();
    return ret;
  }

  public String value() { 
    String ret = this.delegate.value();
    return ret;
  }

  public boolean isText() { 
    boolean ret = this.delegate.isText();
    return ret;
  }

  public boolean isBlank() { 
    boolean ret = this.delegate.isBlank();
    return ret;
  }

  public static List<ArgToken> tokenize(String s) { 
    List<ArgToken> ret = io.vertx.ext.shell.command.ArgToken.tokenize(s).stream().map(ArgToken::newInstance).collect(java.util.stream.Collectors.toList());
    return ret;
  }


  public static ArgToken newInstance(io.vertx.ext.shell.command.ArgToken arg) {
    return arg != null ? new ArgToken(arg) : null;
  }
}
