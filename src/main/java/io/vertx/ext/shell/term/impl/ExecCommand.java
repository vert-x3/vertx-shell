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

package io.vertx.ext.shell.term.impl;

import io.vertx.core.Handler;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.SessionAware;
import org.apache.sshd.server.session.ServerSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class ExecCommand implements Command, SessionAware, SSHExec {

  private final String command;
  private final Handler<SSHExec> handler;
  private ExitCallback exitCallback;

  public ExecCommand(Handler<SSHExec> handler, String command) {
    this.handler = handler;
    this.command = command;
  }

  @Override
  public void start(Environment env) throws IOException {
    if (handler != null) {
      handler.handle(this);
    } else {
      exitCallback.onExit(0);
    }
  }

  @Override
  public String command() {
    return command;
  }

  @Override
  public void end(int status) {
    exitCallback.onExit(status);
  }

  @Override public void setExitCallback(ExitCallback callback) {
    this.exitCallback = callback;
  }

  @Override public void setInputStream(InputStream in) {}
  @Override public void setOutputStream(OutputStream out) {}
  @Override public void setErrorStream(OutputStream err) {}
  @Override public void destroy() throws Exception {}
  @Override public void setSession(ServerSession session) {}

}
