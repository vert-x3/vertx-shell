package io.vertx.ext.shell.net.impl;

import io.termd.core.ssh.netty.NettyIoHandlerBridge;
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
    context.executeFromIO(() -> {
      super.sessionCreated(handler, session);
    });
  }

  @Override
  public void sessionClosed(IoHandler handler, IoSession session) throws Exception {
    context.executeFromIO(() -> {
      super.sessionClosed(handler, session);
    });
  }

  @Override
  public void messageReceived(IoHandler handler, IoSession session, org.apache.sshd.common.util.Readable message) throws Exception {
    context.executeFromIO(() -> {
      super.messageReceived(handler, session, message);
    });
  }
}
