package io.vertx.ext.unit;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.Dimension;
import io.vertx.ext.shell.Stream;
import io.vertx.ext.shell.process.ProcessContext;
import io.vertx.ext.shell.Tty;

import java.util.HashMap;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class TestProcessContext implements ProcessContext, Tty {

  private Handler<Integer> endHandler;
  final Context context = Vertx.currentContext();
  final HashMap<String, Handler<Void>> eventHandlers = new HashMap<>();
  Dimension windowSize;
  Stream stdin;
  private Stream stdout;

  @Override
  public Tty tty() {
    return this;
  }

  @Override
  public void end(int status) {
    if (endHandler != null) {
      endHandler.handle(status);
    }
  }

  public TestProcessContext endHandler(Handler<Integer> handler) {
    endHandler = context != null ? status -> context.runOnContext(v -> handler.handle(status) ) : handler;
    return this;
  }

  @Override
  public Dimension windowSize() {
    return windowSize;
  }

  public void setWindowSize(Dimension size) {
    windowSize = size;
    sendEvent("SIGWINCH");
  }

  @Override
  public void setStdin(Stream stdin) {
    this.stdin = stdin;
  }

  @Override
  public Stream stdout() {
    return stdout;
  }

  public TestProcessContext setStdout(Stream stream) {
    stdout = context != null ? txt -> context.runOnContext(v -> stream.handle(txt) ) : stream;
    return this;
  }

  @Override
  public void eventHandler(String event, Handler<Void> handler) {
    if (handler != null) {
      eventHandlers.put(event, handler);
    } else {
      eventHandlers.remove(event);
    }
  }

  public boolean sendEvent(String event) {
    Handler<Void> handler = eventHandlers.get(event);
    if (handler != null) {
      handler.handle(null);
      return true;
    } else {
      return false;
    }
  }
}
