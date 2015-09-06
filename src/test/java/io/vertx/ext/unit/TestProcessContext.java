package io.vertx.ext.unit;

import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.Session;
import io.vertx.ext.shell.Stream;
import io.vertx.ext.shell.impl.SessionImpl;
import io.vertx.ext.shell.process.ProcessContext;
import io.vertx.ext.shell.Tty;

import java.util.HashMap;

/**
* @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
class TestProcessContext implements ProcessContext, Tty {

  final SessionImpl session = new SessionImpl();
  private Handler<Integer> endHandler;
  final Context context = Vertx.currentContext();
  final HashMap<String, Handler<Void>> eventHandlers = new HashMap<>();
  int width, height;
  Handler<String> stdin;
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
  public int width() {
    return width;
  }

  @Override
  public int height() {
    return height;
  }

  public void setWindowSize(int width, int height) {
    this.width = width;
    this.height = height;
    sendEvent("SIGWINCH");
  }

  @Override
  public TestProcessContext setStdin(Handler<String> stdin) {
    this.stdin = stdin;
    return this;
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
  public TestProcessContext eventHandler(String event, Handler<Void> handler) {
    if (handler != null) {
      eventHandlers.put(event, handler);
    } else {
      eventHandlers.remove(event);
    }
    return this;
  }

  @Override
  public Session session() {
    return session;
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
