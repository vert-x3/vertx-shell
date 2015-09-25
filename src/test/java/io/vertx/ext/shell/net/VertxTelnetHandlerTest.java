package io.vertx.ext.shell.net;

import io.termd.core.telnet.TelnetHandler;
import io.termd.core.telnet.TelnetHandlerTest;

import java.io.Closeable;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * See <a href="http://commons.apache.org/proper/commons-net/examples/telnet/TelnetClientExample.java>for more possibilities</a>
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class VertxTelnetHandlerTest extends TelnetHandlerTest {

  @Override
  protected Function<Supplier<TelnetHandler>, Closeable> serverFactory() {
    return VertxTelnetTermTest.VERTX_SERVER;
  }
}
