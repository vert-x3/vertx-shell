/*
 * Copyright 2015 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 *
 *
 * Copyright (c) 2015 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 *
 */

package io.vertx.rxjava.ext.shell.command;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.ext.shell.cli.Completion;
import io.vertx.core.Handler;

/**
 * A build for Vert.x Shell command.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.command.CommandBuilder original} non RX-ified interface using Vert.x codegen.
 */

public class CommandBuilder {

  final io.vertx.ext.shell.command.CommandBuilder delegate;

  public CommandBuilder(io.vertx.ext.shell.command.CommandBuilder delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Set the command process handler, the process handler is called when the command is executed.
   * @param handler the process handler
   * @return this command object
   */
  public CommandBuilder processHandler(Handler<CommandProcess> handler) { 
    this.delegate.processHandler(new Handler<io.vertx.ext.shell.command.CommandProcess>() {
      public void handle(io.vertx.ext.shell.command.CommandProcess event) {
        handler.handle(new CommandProcess(event));
      }
    });
    return this;
  }

  /**
   * Set the command completion handler, the completion handler when the user asks for contextual command line
   * completion, usually hitting the <i>tab</i> key.
   * @param handler the completion handler
   * @return this command object
   */
  public CommandBuilder completionHandler(Handler<Completion> handler) { 
    this.delegate.completionHandler(new Handler<io.vertx.ext.shell.cli.Completion>() {
      public void handle(io.vertx.ext.shell.cli.Completion event) {
        handler.handle(new Completion(event));
      }
    });
    return this;
  }

  /**
   * @return the command
   * @return 
   */
  public Command build() { 
    Command ret= Command.newInstance(this.delegate.build());
    return ret;
  }


  public static CommandBuilder newInstance(io.vertx.ext.shell.command.CommandBuilder arg) {
    return arg != null ? new CommandBuilder(arg) : null;
  }
}
