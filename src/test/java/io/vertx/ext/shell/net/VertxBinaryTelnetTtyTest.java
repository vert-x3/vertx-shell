package io.vertx.ext.shell.net;

import io.termd.core.telnet.TelnetHandler;
import io.termd.core.tty.TelnetTtyTestBase;

import java.io.Closeable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxBinaryTelnetTtyTest extends TelnetTtyTestBase {

  public VertxBinaryTelnetTtyTest() {
    binary = true;
  }

  @Override
  protected Function<Supplier<TelnetHandler>, Closeable> serverFactory() {
    return VertxTelnetTermTest.VERTX_SERVER;
  }

  @Override
  protected void assertThreading(Thread connThread, Thread schedulerThread) throws Exception {
    assertTrue(connThread.getName().startsWith("vert.x-eventloop-thread"));
    assertEquals(connThread, schedulerThread);
  }
}
