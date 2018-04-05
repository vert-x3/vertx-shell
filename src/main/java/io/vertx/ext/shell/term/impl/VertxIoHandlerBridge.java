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

import io.termd.core.ssh.netty.NettyIoHandlerBridge;
import io.vertx.core.VertxException;
import io.vertx.core.impl.ContextInternal;
import org.apache.sshd.common.io.IoHandler;
import org.apache.sshd.common.io.IoSession;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxIoHandlerBridge extends NettyIoHandlerBridge {

  private final ContextInternal context;

  public VertxIoHandlerBridge(ContextInternal context) {
    this.context = context;
  }

  @Override
  public void sessionCreated(IoHandler handler, IoSession session) throws Exception {
    context.executeFromIO(v -> {
      try {
        super.sessionCreated(handler, session);
      } catch (Exception e) {
        throw new VertxException(e);
      }
    });
  }

  @Override
  public void sessionClosed(IoHandler handler, IoSession session) throws Exception {
    context.executeFromIO(v -> {
      try {
        super.sessionClosed(handler, session);
      } catch (Exception e) {
        throw new VertxException(e);
      }
    });
  }

  @Override
  public void messageReceived(IoHandler handler, IoSession session, org.apache.sshd.common.util.Readable message) throws Exception {
    context.executeFromIO(v -> {
      try {
        super.messageReceived(handler, session, message);
      } catch (Exception e) {
        throw new VertxException(e);
      }
    });
  }
}
