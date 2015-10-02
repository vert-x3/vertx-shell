package io.vertx.ext.shell.command.base;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;
import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.Option;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandBuilder;

import java.util.Iterator;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface LocalMapCommand {

  static Command get() {
    // CLI does not support variable arguments yet
    CommandBuilder cmd = Command.builder("local-map-get");
    cmd.processHandler(process -> {
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
    });
    return cmd.build();
  }

  static Command put() {
    CommandBuilder cmd = Command.builder(CLI.
            create("local-map-put").
            setSummary("Put key/value in a local map").
            addOption(new Option().setArgName("help").setFlag(true).setShortName("h").setLongName("help")).
            addArgument(new Argument().setArgName("map").setIndex(0).setDefaultValue("the local shared map")).
            addArgument(new Argument().setArgName("key").setIndex(1).setDefaultValue("the key to put")).
            addArgument(new Argument().setArgName("value").setIndex(2).setDefaultValue("the value to put"))
    );
    cmd.processHandler(process -> {
      Vertx vertx = process.vertx();
      SharedData sharedData = vertx.sharedData();
      LocalMap<Object, Object> map = sharedData.getLocalMap(process.commandLine().getArgumentValue("map"));
      String key = process.commandLine().getArgumentValue("key");
      String value = process.commandLine().getArgumentValue("value");
      map.put(key, value);
      process.end();
    });
    return cmd.build();
  }

  static Command rm() {
    // CLI does not support variable arguments yet
    CommandBuilder cmd = Command.builder("local-map-rm");
    cmd.processHandler(process -> {
      Iterator<String> it = process.args().iterator();
      if (!it.hasNext()) {
        process.write("usage: local-map-rm map keys...\n");
      } else {
        Vertx vertx = process.vertx();
        SharedData sharedData = vertx.sharedData();
        LocalMap<Object, Object> map = sharedData.getLocalMap(it.next());
        while (it.hasNext()) {
          String key = it.next();
          map.remove(key);
        }
      }
      process.end();
    });
    return cmd.build();
  }
}
