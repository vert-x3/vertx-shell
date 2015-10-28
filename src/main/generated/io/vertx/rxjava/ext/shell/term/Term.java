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

package io.vertx.rxjava.ext.shell.term;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.ext.shell.io.Stream;
import io.vertx.core.Handler;

/**
 * The remote terminal.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.term.Term original} non RX-ified interface using Vert.x codegen.
 */

public class Term extends Tty {

  final io.vertx.ext.shell.term.Term delegate;

  public Term(io.vertx.ext.shell.term.Term delegate) {
    super(delegate);
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public Term resizehandler(Handler<Void> handler) { 
    ( /* Work around for https://jira.codehaus.org/browse/GROOVY-6970 */ (io.vertx.ext.shell.term.Term) delegate).resizehandler(handler);
    return this;
  }

  public Term setStdin(Stream stdin) { 
    ( /* Work around for https://jira.codehaus.org/browse/GROOVY-6970 */ (io.vertx.ext.shell.term.Term) delegate).setStdin((io.vertx.ext.shell.io.Stream) stdin.getDelegate());
    return this;
  }

  /**
   * Set a handler that will be called when the terminal is closed.
   * @param handler the handler
   * @return a reference to this, so the API can be used fluently
   */
  public Term closeHandler(Handler<Void> handler) { 
    this.delegate.closeHandler(handler);
    return this;
  }

  /**
   * Close the remote terminal.
   */
  public void close() { 
    this.delegate.close();
  }


  public static Term newInstance(io.vertx.ext.shell.term.Term arg) {
    return arg != null ? new Term(arg) : null;
  }
}
