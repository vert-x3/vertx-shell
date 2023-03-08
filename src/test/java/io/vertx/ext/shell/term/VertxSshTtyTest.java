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

package io.vertx.ext.shell.term;

import io.termd.core.ssh.TtyCommand;
import io.termd.core.ssh.netty.NettyIoServiceFactoryFactory;
import io.termd.core.ssh.netty.NettyIoSession;
import io.termd.core.tty.SshTtyTestBase;
import io.termd.core.tty.TtyConnection;
import io.vertx.core.Vertx;
import io.vertx.core.impl.ContextInternal;
import io.vertx.ext.shell.term.impl.VertxIoHandlerBridge;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.server.SshServer;
import org.junit.After;
import org.junit.Before;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxSshTtyTest extends SshTtyTestBase {

  private Vertx vertx;
  private ContextInternal context;

  @Before
  public void before() {
    super.before();
    vertx = Vertx.vertx();
    context = (ContextInternal) vertx.getOrCreateContext();
  }

  @After
  public void after() throws Exception {
    super.after();
    CountDownLatch latch = new CountDownLatch(1);
    vertx.close()
      .onComplete(v -> latch.countDown());
    await(latch);
  }

  @Override
  protected SshServer createServer() {
    SshServer sshd = SshServer.setUpDefaultServer();
    sshd.setIoServiceFactoryFactory(new NettyIoServiceFactoryFactory(context.nettyEventLoop(), new VertxIoHandlerBridge(context)));
    return sshd;
  }

  @Override
  protected TtyCommand createConnection(Consumer<TtyConnection> onConnect) {
    assertEquals(context, Vertx.currentContext());
    return new TtyCommand(charset, onConnect) {
      @Override
      public void execute(Runnable task) {
        Session session = this.session.getSession();
        NettyIoSession ioSession = (NettyIoSession) session.getIoSession();
        ioSession.execute(task);
      }
      @Override
      public void schedule(Runnable task, long delay, TimeUnit unit) {
        Session session = this.session.getSession();
        NettyIoSession ioSession = (NettyIoSession) session.getIoSession();
        ioSession.schedule(task, delay, unit);
      }
    };
  }

  @Override
  protected void assertThreading(Thread connThread, Thread schedulerThread) throws Exception {
    assertTrue(connThread.getName().startsWith("vert.x-eventloop-thread"));
    assertEquals(connThread, schedulerThread);
  }
}
