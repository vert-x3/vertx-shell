/*
 * Copyright 2014 Red Hat, Inc.
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
 * Copyright (c) 2011-2013 The original author or authors
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

package io.vertx.ext.shell.command.base;

import io.vertx.core.cli.annotations.Argument;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("cd")
@Summary("Change the current working dir")
public class FileSystemCd implements Command {

  private String dir;

  @Argument(index = 0, argName = "dir", required = false)
  @Description("the new working dir")
  public void setDir(String dir) {
    this.dir = dir;
  }

  @Override
  public void process(CommandProcess process) {
    if (process.args().size() > 0) {
      String cwd = process.session().get("cwd");
      new FsHelper().cd(process.vertx().fileSystem(), cwd, dir, ar -> {
        if (ar.succeeded()) {
          process.session().put("cwd", ar.result());
          process.end();
        } else {
          process.write("cd: No such file or directory\n");
          process.end();
        }
      });
    } else {
      process.session().remove("cwd");
      process.end();
    }
  }

  @Override
  public void complete(Completion completion) {
    new FsHelper().completionHandler().handle(completion);
  }
}
