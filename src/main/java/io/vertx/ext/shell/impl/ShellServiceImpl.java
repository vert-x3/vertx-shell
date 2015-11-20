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

package io.vertx.ext.shell.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.ShellServer;
import io.vertx.ext.shell.term.SSHTermOptions;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.term.TelnetTermOptions;
import io.vertx.ext.shell.term.WebTermOptions;
import io.vertx.ext.shell.term.impl.SSHTermServer;
import io.vertx.ext.shell.term.impl.TelnetTermServer;
import io.vertx.ext.shell.term.impl.WebTermServer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ShellServiceImpl implements ShellService {

  private final Vertx vertx;
  private final ShellServiceOptions options;
  private final ShellServer server;

  public ShellServiceImpl(Vertx vertx, ShellServer server, ShellServiceOptions options) {
    this.vertx = vertx;
    this.options = options;
    this.server = server;
  }

  @Override
  public void start(Handler<AsyncResult<Void>> startHandler) {
    TelnetTermOptions telnetOptions = options.getTelnetOptions();
    SSHTermOptions sshOptions = options.getSSHOptions();
    WebTermOptions webOptions = options.getWebOptions();
    if (telnetOptions != null) {
      server.registerTermServer(new TelnetTermServer(vertx, telnetOptions));
    }
    if (sshOptions != null) {
      server.registerTermServer(new SSHTermServer(vertx, sshOptions));
    }
    if (webOptions != null) {
      server.registerTermServer(new WebTermServer(vertx, webOptions));
    }
    server.listen(startHandler);
  }

  @Override
  public void stop(Handler<AsyncResult<Void>> stopHandler) {
    server.listen(stopHandler);
  }
}
