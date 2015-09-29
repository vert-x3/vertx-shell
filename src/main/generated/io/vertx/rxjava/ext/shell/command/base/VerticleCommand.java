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
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.command.base.VerticleCommand original} non RX-ified interface using Vert.x codegen.
 */

public class VerticleCommand {

  final io.vertx.ext.shell.command.base.VerticleCommand delegate;

  public VerticleCommand(io.vertx.ext.shell.command.base.VerticleCommand delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static Command ls() { 
    Command ret= Command.newInstance(io.vertx.ext.shell.command.base.VerticleCommand.ls());
    return ret;
  }

  public static Command deploy() { 
    Command ret= Command.newInstance(io.vertx.ext.shell.command.base.VerticleCommand.deploy());
    return ret;
  }

  public static Command undeploy() { 
    Command ret= Command.newInstance(io.vertx.ext.shell.command.base.VerticleCommand.undeploy());
    return ret;
  }

  public static Command factories() { 
    Command ret= Command.newInstance(io.vertx.ext.shell.command.base.VerticleCommand.factories());
    return ret;
  }


  public static VerticleCommand newInstance(io.vertx.ext.shell.command.base.VerticleCommand arg) {
    return arg != null ? new VerticleCommand(arg) : null;
  }
}
