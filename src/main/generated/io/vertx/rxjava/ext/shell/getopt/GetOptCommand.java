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

package io.vertx.rxjava.ext.shell.getopt;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.ext.shell.command.Command;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.getopt.GetOptCommand original} non RX-ified interface using Vert.x codegen.
 */

public class GetOptCommand {

  final io.vertx.ext.shell.getopt.GetOptCommand delegate;

  public GetOptCommand(io.vertx.ext.shell.getopt.GetOptCommand delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public static GetOptCommand create(String name) { 
    GetOptCommand ret= GetOptCommand.newInstance(io.vertx.ext.shell.getopt.GetOptCommand.create(name));
    return ret;
  }

  public void processHandler(Handler<GetOptCommandProcess> handler) { 
    this.delegate.processHandler(new Handler<io.vertx.ext.shell.getopt.GetOptCommandProcess>() {
      public void handle(io.vertx.ext.shell.getopt.GetOptCommandProcess event) {
        handler.handle(new GetOptCommandProcess(event));
      }
    });
  }

  public GetOptCommand addOption(Option option) { 
    this.delegate.addOption((io.vertx.ext.shell.getopt.Option) option.getDelegate());
    return this;
  }

  public Option getOption(String name) { 
    Option ret= Option.newInstance(this.delegate.getOption(name));
    return ret;
  }

  public Command build() { 
    Command ret= Command.newInstance(this.delegate.build());
    return ret;
  }


  public static GetOptCommand newInstance(io.vertx.ext.shell.getopt.GetOptCommand arg) {
    return arg != null ? new GetOptCommand(arg) : null;
  }
}
