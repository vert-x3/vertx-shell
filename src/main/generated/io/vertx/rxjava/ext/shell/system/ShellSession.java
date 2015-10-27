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

package io.vertx.rxjava.ext.shell.system;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import java.util.List;
import java.util.Set;
import io.vertx.rxjava.ext.shell.cli.CliToken;
import io.vertx.rxjava.ext.shell.session.Session;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.system.ShellSession original} non RX-ified interface using Vert.x codegen.
 */

public class ShellSession {

  final io.vertx.ext.shell.system.ShellSession delegate;

  public ShellSession(io.vertx.ext.shell.system.ShellSession delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public Session session() { 
    Session ret= Session.newInstance(this.delegate.session());
    return ret;
  }

  public Set<Job> jobs() { 
    Set<Job> ret = this.delegate.jobs().stream().map(Job::newInstance).collect(java.util.stream.Collectors.toSet());
    return ret;
  }

  public Job getJob(int id) { 
    Job ret= Job.newInstance(this.delegate.getJob(id));
    return ret;
  }

  public Job createJob(List<CliToken> args) { 
    Job ret= Job.newInstance(this.delegate.createJob(args.stream().map(element -> (io.vertx.ext.shell.cli.CliToken)element.getDelegate()).collect(java.util.stream.Collectors.toList())));
    return ret;
  }


  public static ShellSession newInstance(io.vertx.ext.shell.system.ShellSession arg) {
    return arg != null ? new ShellSession(arg) : null;
  }
}
