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
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.command.Command original} non RX-ified interface using Vert.x codegen.
 */

public class Command {

  final io.vertx.ext.shell.command.Command delegate;

  public Command(io.vertx.ext.shell.command.Command delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static Command create(String name) { 
    Command ret= Command.newInstance(io.vertx.ext.shell.command.Command.create(name));
    return ret;
  }

  public String name() { 
    String ret = this.delegate.name();
    return ret;
  }

  public void processHandler(Handler<CommandProcess> handler) { 
    this.delegate.processHandler(new Handler<io.vertx.ext.shell.command.CommandProcess>() {
      public void handle(io.vertx.ext.shell.command.CommandProcess event) {
        handler.handle(new CommandProcess(event));
      }
    });
  }

  public void unregister() { 
    this.delegate.unregister();
  }


  public static Command newInstance(io.vertx.ext.shell.command.Command arg) {
    return arg != null ? new Command(arg) : null;
  }
}
