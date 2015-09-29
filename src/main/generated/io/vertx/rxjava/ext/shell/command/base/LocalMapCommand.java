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

package io.vertx.rxjava.ext.shell.command.base;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.ext.shell.command.Command;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.command.base.LocalMapCommand original} non RX-ified interface using Vert.x codegen.
 */

public class LocalMapCommand {

  final io.vertx.ext.shell.command.base.LocalMapCommand delegate;

  public LocalMapCommand(io.vertx.ext.shell.command.base.LocalMapCommand delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static Command get() { 
    Command ret= Command.newInstance(io.vertx.ext.shell.command.base.LocalMapCommand.get());
    return ret;
  }

  public static Command put() { 
    Command ret= Command.newInstance(io.vertx.ext.shell.command.base.LocalMapCommand.put());
    return ret;
  }

  public static Command rm() { 
    Command ret= Command.newInstance(io.vertx.ext.shell.command.base.LocalMapCommand.rm());
    return ret;
  }


  public static LocalMapCommand newInstance(io.vertx.ext.shell.command.base.LocalMapCommand arg) {
    return arg != null ? new LocalMapCommand(arg) : null;
  }
}
