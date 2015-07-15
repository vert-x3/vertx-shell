package io.vertx.ext.unit;

import io.termd.core.tty.TtyConnection;
import io.termd.core.tty.TtyEvent;
import io.termd.core.util.Dimension;
import io.termd.core.util.Helper;

import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class TestTtyConnection implements TtyConnection {

  private Consumer<String> termHandler;
  private Consumer<Dimension> sizeHandler;
  private Consumer<TtyEvent> eventHandler;
  private Consumer<int[]> readHandler;
  private Consumer<Void> closeHandler;
  public final StringBuilder out = new StringBuilder();

  @Override
  public Consumer<String> termHandler() {
    return termHandler;
  }

  @Override
  public void setTermHandler(Consumer<String> handler) {
    termHandler = handler;
  }

  @Override
  public Consumer<Dimension> sizeHandler() {
    return sizeHandler;
  }

  @Override
  public void setSizeHandler(Consumer<Dimension> handler) {
    sizeHandler = handler;
  }

  @Override
  public Consumer<TtyEvent> eventHandler() {
    return eventHandler;
  }

  @Override
  public void setEventHandler(Consumer<TtyEvent> handler) {
    eventHandler = handler;
  }

  @Override
  public Consumer<int[]> readHandler() {
    return readHandler;
  }

  @Override
  public void setReadHandler(Consumer<int[]> handler) {
    readHandler = handler;
  }

  @Override
  public Consumer<int[]> writeHandler() {
    return codePoints -> {
      synchronized (TestTtyConnection.this) {
        Helper.appendCodePoints(out, codePoints);
        notify();
      }
    };
  }

  @Override
  public void setCloseHandler(Consumer<Void> handler) {
    closeHandler = handler;
  }

  @Override
  public Consumer<Void> closeHandler() {
    return closeHandler;
  }

  @Override
  public void close() {

  }

  @Override
  public void schedule(Runnable task) {
    task.run();
  }

  public void sendEvent(TtyEvent event) {
    eventHandler.accept(event);
  }

  public void read(String s) {
    readHandler.accept(Helper.toCodePoints(s));
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
