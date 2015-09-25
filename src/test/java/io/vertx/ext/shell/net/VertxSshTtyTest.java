package io.vertx.ext.shell.net;

import io.termd.core.ssh.SshTtyConnection;
import io.termd.core.ssh.netty.NettyIoHandlerBridge;
import io.termd.core.ssh.netty.NettyIoServiceFactoryFactory;
import io.termd.core.ssh.netty.NettyIoSession;
import io.termd.core.tty.SshTtyTestBase;
import io.termd.core.tty.TtyConnection;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.impl.ContextImpl;
import org.apache.sshd.common.io.IoHandler;
import org.apache.sshd.common.io.IoSession;
import org.apache.sshd.common.session.Session;
import org.apache.sshd.common.util.Readable;
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
  private Context context;

  @Before
  public void before() {
    vertx = Vertx.vertx();
    context = vertx.getOrCreateContext();
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

    NettyIoHandlerBridge bridge = new NettyIoHandlerBridge() {
      @Override
      public void sessionCreated(IoHandler handler, IoSession session) throws Exception {
        ((ContextImpl)context).executeFromIO(() -> {
          super.sessionCreated(handler, session);
        });
      }
      @Override
      public void sessionClosed(IoHandler handler, IoSession session) throws Exception {
        ((ContextImpl)context).executeFromIO(() -> {
          super.sessionClosed(handler, session);
        });
      }
      @Override
      public void messageReceived(IoHandler handler, IoSession session, Readable message) throws Exception {
        ((ContextImpl)context).executeFromIO(() -> {
          super.messageReceived(handler, session, message);
        });
      }
    };

    sshd.setIoServiceFactoryFactory(new NettyIoServiceFactoryFactory(context.nettyEventLoop(), bridge));
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
