package io.vertx.ext.unit;

import io.termd.core.tty.TtyConnection;
import io.termd.core.tty.TtyEvent;
import io.termd.core.util.Helper;
import io.termd.core.util.Vector;

import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestTtyConnection implements TtyConnection {

  private Consumer<String> termHandler;
  private Consumer<Vector> sizeHandler;
  private Consumer<TtyEvent> eventHandler;
  private Consumer<int[]> stdinHandler;
  private Consumer<Void> closeHandler;
  public final StringBuilder out = new StringBuilder();

  @Override
  public Vector size() {
    return new Vector(40, 20);
  }

  @Override
  public Consumer<String> getTermHandler() {
    return termHandler;
  }

  @Override
  public void setTermHandler(Consumer<String> handler) {
    termHandler = handler;
  }

  @Override
  public Consumer<Vector> getSizeHandler() {
    return sizeHandler;
  }

  @Override
  public void setSizeHandler(Consumer<Vector> handler) {
    sizeHandler = handler;
  }

  @Override
  public Consumer<TtyEvent> getEventHandler() {
    return eventHandler;
  }

  @Override
  public void setEventHandler(Consumer<TtyEvent> handler) {
    eventHandler = handler;
  }

  @Override
  public Consumer<int[]> getStdinHandler() {
    return stdinHandler;
  }

  @Override
  public void setStdinHandler(Consumer<int[]> consumer) {
    this.stdinHandler = consumer;
  }


  @Override
  public Consumer<int[]> stdoutHandler() {
    return codePoints -> {
      synchronized (TestTtyConnection.this) {
        Helper.appendCodePoints(out, codePoints);
        notify();
      }
    };
  }

  @Override
  public Consumer<Void> getCloseHandler() {
    return closeHandler;
  }

  @Override
  public void setCloseHandler(Consumer<Void> handler) {
    closeHandler = handler;
  }

  @Override
  public void close() {

  }

  private boolean reading = false;

  @Override
  public void schedule(Runnable task) {
    if (reading) {
      throw new AssertionError();
    }
    task.run();
  }

  public void sendEvent(TtyEvent event) {
    eventHandler.accept(event);
  }

  public void read(String s) {
    reading = true;
    stdinHandler.accept(Helper.toCodePoints(s));
    reading = false;
  }

  public synchronized void assertWritten(String s) {
    while (true) {
      int l = Math.min(s.length(), out.length());
      String actual = out.substring(0, l);
      String expected = s.substring(0, l);
      if (actual.equals(expected)) {
        out.replace(0, l, "");
        s = s.substring(l);
      } else {
        throw new AssertionError("Was expecting <" + actual + "> to be equals to <" + expected + ">");
      }
      if (s.length() == 0) {
        break;
      } else {
        try {
          wait(5000);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          throw new AssertionError(e);
        }
      }
    }
  }
}
