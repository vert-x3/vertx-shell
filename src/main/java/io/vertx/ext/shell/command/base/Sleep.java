package io.vertx.ext.shell.command.base;

import io.vertx.core.Vertx;
import io.vertx.core.cli.annotations.Argument;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.io.EventType;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("sleep")
@Summary("Suspend execution for an interval of time")
public class Sleep implements Command {

  private String seconds;

  @Description("the number of seconds to wait")
  @Argument(index = 0, argName = "seconds")
  public void setSeconds(String seconds) {
    this.seconds = seconds;
  }

  @Override
  public void process(CommandProcess process) {
    int timeout = -1;
    try {
      timeout = Integer.parseInt(seconds);
    } catch (NumberFormatException ignore) {
    }
    scheduleSleep(process, timeout * 1000L);
  }

  private void scheduleSleep(CommandProcess process, long millis) {
    Vertx vertx = process.vertx();
    if (millis > 0) {
      long now = System.currentTimeMillis();
      AtomicLong remaining = new AtomicLong(-1);
      long id = process.vertx().setTimer(millis, v -> {
        process.end();
      });
      process.eventHandler(EventType.SIGINT, v -> {
        if (vertx.cancelTimer(id)) {
          process.end();
        }
      });
      process.eventHandler(EventType.SIGTSTP, v -> {
        if (vertx.cancelTimer(id)) {
          remaining.set(millis - (System.currentTimeMillis() - now));
        }
      });
      process.eventHandler(EventType.SIGCONT, v -> {
        scheduleSleep(process, remaining.get());
      });
    } else {
      process.end();
    }
  }
}
