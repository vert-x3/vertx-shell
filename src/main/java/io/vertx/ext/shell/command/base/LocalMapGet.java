package io.vertx.ext.shell.command.base;

import io.vertx.core.Vertx;
import io.vertx.core.cli.CLI;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

import java.util.Iterator;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class LocalMapGet implements Command {

  @Override
  public String name() {
    return "local-map-get";
  }

  @Override
  public CLI cli() {
    // CLI does not support variable arguments yet
    return null;
  }

  @Override
  public void process(CommandProcess process) {
    Iterator<String> it = process.args().iterator();
    if (!it.hasNext()) {
      process.write("usage: local-map-get map keys...\n");
    } else {
      Vertx vertx = process.vertx();
      SharedData sharedData = vertx.sharedData();
      LocalMap<Object, Object> map = sharedData.getLocalMap(it.next());
      while (it.hasNext()) {
        String key = it.next();
        Object value = map.get(key);
        process.write(key + ": " + value + "\n");
      }
    }
    process.end();
  }
}
