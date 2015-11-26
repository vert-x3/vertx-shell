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

package io.vertx.ext.shell.command;

import io.vertx.core.cli.CLI;
import io.vertx.ext.shell.cli.Completion;

import java.util.Collections;
import java.util.List;

/**
 * The base command class that Java annotated command should extend.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class AnnotatedCommand {

  /**
   * @return the command name
   */
  public String name() {
    return null;
  }

  /**
   * @return the command line interface, can be null
   */
  public CLI cli() {
    return null;
  }

  /**
   * Process the command, when the command is done processing it should call the {@link CommandProcess#end()} method.
   *
   * @param process the command process
   */
  public abstract void process(CommandProcess process);

  /**
   * Perform command completion, when the command is done completing it should call {@link Completion#complete(List)}
   * or {@link Completion#complete(String, boolean)} )} method to signal completion is done.
   *
   * @param completion the completion object
   */
  public void complete(Completion completion) {
    completion.complete(Collections.emptyList());
  }

}
