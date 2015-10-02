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

import io.vertx.core.Vertx;
import io.vertx.core.cli.CLI;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

import java.util.Iterator;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LocalMapRm implements Command {

  @Override
  public String name() {
    return "local-map-rm";
  }

  @Override
  public CLI cli() {
    // CLI does not support variable arguments yet
    return null;
  }

  @Override
  public void process(CommandProcess process) {
    Iterator<String> it = process.args().iterator();
    if (!it.hasNext()) {
      process.write("usage: local-map-rm map keys...\n");
    } else {
      Vertx vertx = process.vertx();
      SharedData sharedData = vertx.sharedData();
      LocalMap<Object, Object> map = sharedData.getLocalMap(it.next());
      while (it.hasNext()) {
        String key = it.next();
        map.remove(key);
      }
    }
    process.end();
  }
}
