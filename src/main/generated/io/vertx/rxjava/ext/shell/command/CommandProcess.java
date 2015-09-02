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
import io.vertx.rxjava.ext.shell.cli.CliToken;
import io.vertx.core.Handler;
import io.vertx.rxjava.ext.shell.Stream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.command.CommandProcess original} non RX-ified interface using Vert.x codegen.
 */

public class CommandProcess {

  final io.vertx.ext.shell.command.CommandProcess delegate;

  public CommandProcess(io.vertx.ext.shell.command.CommandProcess delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public List<CliToken> args() { 
    List<CliToken> ret = this.delegate.args().stream().map(CliToken::newInstance).collect(java.util.stream.Collectors.toList());
    return ret;
  }

  public int width() { 
    int ret = this.delegate.width();
    return ret;
  }

  public int height() { 
    int ret = this.delegate.height();
    return ret;
  }

  public CommandProcess setStdin(Stream stdin) { 
    this.delegate.setStdin((io.vertx.ext.shell.Stream) stdin.getDelegate());
    return this;
  }

  public CommandProcess eventHandler(String event, Handler<Void> handler) { 
    this.delegate.eventHandler(event, handler);
    return this;
  }

  public Stream stdout() { 
    Stream ret= Stream.newInstance(this.delegate.stdout());
    return ret;
  }

  public CommandProcess write(String text) { 
    this.delegate.write(text);
    return this;
  }

  public void end(int code) { 
    this.delegate.end(code);
  }


  public static CommandProcess newInstance(io.vertx.ext.shell.command.CommandProcess arg) {
    return arg != null ? new CommandProcess(arg) : null;
  }
}
