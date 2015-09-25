package io.vertx.ext.shell.net;

import io.termd.core.ssh.SshTtyConnection;
import io.termd.core.ssh.netty.NettyIoServiceFactoryFactory;
import io.termd.core.ssh.netty.NettyIoSession;
import io.termd.core.tty.SshTtyTestBase;
import io.termd.core.tty.TtyConnection;
import io.vertx.core.Vertx;
import io.vertx.core.impl.ContextInternal;
import io.vertx.ext.shell.net.impl.VertxIoHandlerBridge;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.server.SshServer;
import org.junit.After;
import org.junit.Before;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxSshTtyTest extends SshTtyTestBase {

  private Vertx vertx;
  private ContextInternal context;

  @Before
  public void before() {
    vertx = Vertx.vertx();
    context = (ContextInternal) vertx.getOrCreateContext();
  }

  @After
  public void after() throws Exception {
    super.after();
    CountDownLatch latch = new CountDownLatch(1);
    vertx.close(v -> latch.countDown());
    await(latch);
  }

  @Override
  protected SshServer createServer() {
    SshServer sshd = SshServer.setUpDefaultServer();
    sshd.setIoServiceFactoryFactory(new NettyIoServiceFactoryFactory(context.nettyEventLoop(), new VertxIoHandlerBridge(context)));
    return sshd;
  }

  @Override
  protected SshTtyConnection createConnection(Consumer<TtyConnection> onConnect) {
    assertEquals(context, Vertx.currentContext());
    return new SshTtyConnection(onConnect) {
      @Override
      public void schedule(Runnable task) {
        Session session = this.session.getSession();
        NettyIoSession ioSession = (NettyIoSession) session.getIoSession();
        ioSession.schedule(task);
      }
    };
  }

  @Override
  protected void assertThreading(Thread connThread, Thread schedulerThread) throws Exception {
    assertTrue(connThread.getName().startsWith("vert.x-eventloop-thread"));
    assertEquals(connThread, schedulerThread);
    assertEquals(context, Vertx.currentContext());
  }
}
