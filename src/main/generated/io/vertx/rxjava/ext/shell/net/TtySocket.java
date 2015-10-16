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

package io.vertx.rxjava.ext.shell.net;

import io.vertx.ext.shell.net.Terminal;
import io.vertx.rxjava.ext.shell.io.Stream;
import io.vertx.rxjava.ext.shell.io.Tty;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link Terminal original} non RX-ified interface using Vert.x codegen.
 */

public class TtySocket extends Tty {

  final Terminal delegate;

  public TtySocket(Terminal delegate) {
    super(delegate);
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public TtySocket setStdin(Stream stdin) { 
    ( /* Work around for https://jira.codehaus.org/browse/GROOVY-6970 */ (Terminal) delegate).setStdin((io.vertx.ext.shell.io.Stream) stdin.getDelegate());
    return this;
  }

  public TtySocket setStdin(Handler<String> stdin) { 
    ( /* Work around for https://jira.codehaus.org/browse/GROOVY-6970 */ (Terminal) delegate).setStdin(stdin);
    return this;
  }


  public static TtySocket newInstance(Terminal arg) {
    return arg != null ? new TtySocket(arg) : null;
  }
}
