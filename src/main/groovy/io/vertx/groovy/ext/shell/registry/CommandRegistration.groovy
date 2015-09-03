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

package io.vertx.groovy.ext.shell.registry;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.groovy.ext.shell.command.Command
import java.util.List
import io.vertx.groovy.ext.shell.cli.Completion
import io.vertx.groovy.ext.shell.cli.CliToken
import io.vertx.groovy.ext.shell.process.Process
/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
@CompileStatic
public class CommandRegistration {
  final def io.vertx.ext.shell.registry.CommandRegistration delegate;
  public CommandRegistration(io.vertx.ext.shell.registry.CommandRegistration delegate) {
    this.delegate = delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public Command command() {
    def ret= InternalHelper.safeCreate(this.delegate.command(), io.vertx.ext.shell.command.Command.class, io.vertx.groovy.ext.shell.command.Command.class);
    return ret;
  }
  public void complete(Completion completion) {
    this.delegate.complete((io.vertx.ext.shell.cli.Completion)completion.getDelegate());
  }
  public Process createProcess(List<CliToken> args) {
    def ret= InternalHelper.safeCreate(this.delegate.createProcess((List<io.vertx.ext.shell.cli.CliToken>)(args.collect({underpants -> underpants.getDelegate()}))), io.vertx.ext.shell.process.Process.class, io.vertx.groovy.ext.shell.process.Process.class);
    return ret;
  }
}
