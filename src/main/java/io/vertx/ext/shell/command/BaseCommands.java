package io.vertx.ext.shell.command;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;
import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.Option;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.file.FileProps;
import io.vertx.core.http.impl.HttpServerImpl;
import io.vertx.core.impl.Deployment;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.net.impl.NetServerImpl;
import io.vertx.core.net.impl.ServerID;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import io.vertx.core.spi.VerticleFactory;
import io.vertx.ext.shell.impl.vertx.FsHelper;
import io.vertx.ext.shell.registry.CommandRegistration;
import io.vertx.ext.shell.registry.CommandRegistry;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface BaseCommands {

  static Command verticle_ls() {
    Command cmd = Command.command("verticle-ls");
    cmd.processHandler(process -> {
      VertxInternal vertx = (VertxInternal) process.vertx();
      for (String id : vertx.deploymentIDs()) {
        Deployment deployment = vertx.getDeployment(id);
        process.write(id + ": " + deployment.verticleIdentifier() + ", options=" + deployment.deploymentOptions().toJson() + "\n");

      }
      process.end();

    });
    return cmd;
  }

  static Command verticle_deploy() {
    Command cmd = Command.command("verticle-deploy");
    cmd.processHandler(process -> {
      if (process.args().isEmpty()) {
        process.write("no verticle name specified\n").end();
        return;
      }
      String name = process.args().get(0);
      process.vertx().deployVerticle(name, ar -> {
        if (ar.succeeded()) {
          process.write("Deployed " + ar.result() + "\n").end();
        } else {
          process.write("Could not deploy " + name + "\n");
          StringWriter buffer = new StringWriter();
          PrintWriter writer = new PrintWriter(buffer);
          ar.cause().printStackTrace(writer);
          process.write(buffer.toString()).end();
        }
      });
    });
    return cmd;
  }

  static Command verticle_undeploy() {
    Command cmd = Command.command("verticle-undeploy");
    cmd.processHandler(process -> {
      if (process.args().isEmpty()) {
        process.write("no deployment ID specified\n").end();
        return;
      }
      String id = process.args().get(0);
      process.vertx().undeploy(id, ar -> {
        if (ar.succeeded()) {
          process.write("Undeployed " + id + "\n").end();
        } else {
          process.write("Could not undeploy " + id + "\n");
          StringWriter buffer = new StringWriter();
          PrintWriter writer = new PrintWriter(buffer);
          ar.cause().printStackTrace(writer);
          process.write(buffer.toString()).end();
        }
      });
    });
    return cmd;
  }

  static Command verticle_factories() {
    Command cmd = Command.command("verticle-factories");
    cmd.processHandler(process -> {
      VertxInternal vertx = (VertxInternal) process.vertx();
      for (VerticleFactory factory : vertx.verticleFactories()) {
        process.write(factory.getClass().getName() + ": prefix=" + factory.prefix() + ", order=" + factory.order() + ", requiresResolve=" + factory.requiresResolve() + "\n");
      }
      process.end();
    });
    return cmd;
  }

  static Command server_ls() {
    Command cmd = Command.command("server-ls");
    cmd.processHandler(process -> {
      VertxInternal vertx = (VertxInternal) process.vertx();
      process.write("\nNet Servers:\n");
      for (Map.Entry<ServerID, NetServerImpl> server : vertx.sharedNetServers().entrySet()) {
        process.write(server.getKey().host + ":" + server.getKey().port + "\n");
      }
      process.write("\nHttp Servers:\n");
      for (Map.Entry<ServerID, HttpServerImpl> server : vertx.sharedHttpServers().entrySet()) {
        process.write(server.getKey().host + ":" + server.getKey().port + "\n");
      }
      process.end();
    });
    return cmd;
  }

  static Command local_map_get() {
    Command cmd = Command.command("local-map-get");
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
    return cmd;
  }

  static Command local_map_put() {
    Command cmd = Command.command("local-map-put");
    cmd.processHandler(process -> {
      List<String> args = process.args();
      if (args.size() < 3) {
        process.write("usage: local-map-put map key value\n");
      } else {
        Vertx vertx = process.vertx();
        SharedData sharedData = vertx.sharedData();
        LocalMap<Object, Object> map = sharedData.getLocalMap(args.get(0));
        String key = args.get(1);
        String value = args.get(2);
        map.put(key, value);
      }
      process.end();
    });
    return cmd;
  }

  static Command local_map_rm() {
    Command cmd = Command.command("local-map-rm");
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
    return cmd;
  }

  static Command bus_send() {
    Command cmd = Command.command("bus-send");
    cmd.processHandler(process -> {
      List<String> args = process.args();
      if (args.size() < 2) {
        process.write("usage: bus-send address message\n");
      } else {
        String address = args.get(0);
        String msg = args.get(1);
        process.vertx().eventBus().send(address, msg);
      }
      process.end();
    });
    return cmd;
  }

  static Command bus_tail() {
    Command cmd = Command.command("bus-tail");
    cmd.processHandler(process -> {
      List<String> args = process.args();
      if (args.size() < 1) {
        process.write("usage: bus-tail address\n").end();
      } else {
        String address = args.get(0);
        MessageConsumer<Object> consumer = process.vertx().eventBus().consumer(address, msg -> {
          process.write("" + msg.body() + "\n");
        });
        process.eventHandler("SIGINT", done -> {
          consumer.unregister();
          process.end();
        });
      }
    });
    return cmd;
  }

  static Command fs_cd() {
    Command cmd = Command.command("cd");
    cmd.completionHandler(new FsHelper().completionHandler());
    cmd.processHandler(process -> {
      if (process.args().size() > 0) {
        String pathArg = process.args().get(0);
        String path = process.session().get("path");
        new FsHelper().cd(process.vertx().fileSystem(), path, pathArg, ar -> {
          if (ar.succeeded()) {
            process.session().put("path", ar.result());
            process.end();
          } else {
            process.write(ar.result() + ": No such file or directory");
            process.end();
          }
        });
      } else {
        process.session().put("path", "/");
        process.end();
      }
    });
    return cmd;
  }

  static Command fs_pwd() {
    Command cmd = Command.command("pwd");
    cmd.processHandler(process -> {
      String path = process.session().get("path");
      if (path == null) {
        path = new FsHelper().getRootPath();
      }
      process.write(path).write("\n").end();
    });
    return cmd;
  }

  static Command fs_ls() {
    Option ELL = new Option().setArgName("ell").setFlag(true).setShortName("l").setDescription("List in long format");
    Argument FILE = new Argument().setArgName("dir").setRequired(false).setDescription("The dir to list");
    Option HELP = new Option().setArgName("help").setFlag(true).setShortName("h").setLongName("help").setDescription("This help");
    Option ALL = new Option().setArgName("all").setFlag(true).setShortName("a").setDescription("Include files that begins with .");
    Command cmd = Command.command(CLI.create("ls").
        addOption(HELP).
        addOption(ELL).
        addOption(ALL).
        addArgument(FILE).
        setDescription("List directory content"));
    cmd.completionHandler(new FsHelper().completionHandler());
    cmd.processHandler(process -> {
      String fileArg = process.commandLine().getArgumentValue(0);
      new FsHelper().ls(process.vertx(),
          process.session().get("path"),
          fileArg != null ? fileArg : ".",
          ar -> {
            if (ar.succeeded()) {
              Map<String, FileProps> result = ar.result();
              if (result.size() > 0) {
                Stream<Map.Entry<String, FileProps>> entries = result.entrySet().stream();

                // Keep only name
                entries = entries.map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey().substring(entry.getKey().lastIndexOf('/') + 1), entry.getValue()));

                // Filter -a option
                if (!process.commandLine().isSeenInCommandLine(ALL)) {
                  entries = entries.filter(entry -> !entry.getKey().startsWith("."));
                }

                // Format name
                Function<Map.Entry<String, FileProps>, String> formatter;
                if (process.commandLine().isSeenInCommandLine(ELL)) {
                  int width = result.values().stream().map(FileProps::size).max(Long::compare).get().toString().length();
                  String format = "%1$s %2$" + width + "s %3$tb %3$2te %3$tH:%3$tM %4$s";
                  formatter = entry -> {
                    FileProps props = entry.getValue();
                    String a;
                    if (props.isDirectory()) {
                      a = "d";
                    } else if (props.isSymbolicLink()) {
                      a = "l";
                    } else {
                      a = "-";
                    }
                    return String.format(format, a, props.size(), new Date(props.lastModifiedTime()), entry.getKey());
                  };
                } else {
                  formatter = Map.Entry::getKey;
                }

                // Here we go
                entries.map(formatter).forEach(file -> {
                  process.write(file + "\n");
                });
              } else {
                process.write("ls:" + ar.cause().getMessage() + "\n");
              }
            }
            process.end();
          });
    });
    return cmd;
  }

  static Command sleep() {
    class SleepImpl {

      void run(CommandProcess process) {
        if (process.commandLine().getArgumentValue(0) == null) {
          process.write("usage: sleep seconds\n");
          process.end();
        } else {
          String arg = process.commandLine().getArgumentValue(0);
          int seconds = -1;
          try {
            seconds = Integer.parseInt(arg);
          } catch (NumberFormatException ignore) {
          }
          scheduleSleep(process, seconds * 1000);
        }
      }

      void scheduleSleep(CommandProcess process, long millis) {
        Vertx vertx = process.vertx();
        if (millis > 0) {
          System.out.println("Scheduling timer " + millis);
          long now = System.currentTimeMillis();
          AtomicLong remaining = new AtomicLong(-1);
          long id = process.vertx().setTimer(millis, v -> {
            process.end();
          });
          process.eventHandler("SIGINT", v -> {
            if (vertx.cancelTimer(id)) {
              System.out.println("Cancelling timer");
              process.end();
            }
          });
          process.eventHandler("SIGTSTP", v -> {
            if (vertx.cancelTimer(id)) {
              remaining.set(millis - (System.currentTimeMillis() - now));
              System.out.println("Suspending timer " + remaining.get());
            }
          });
          process.eventHandler("SIGCONT", v -> {
            scheduleSleep(process, remaining.get());
          });
        } else {
          process.end();
        }
      }
    }

    SleepImpl sleep = new SleepImpl();
    Command sleepCmd = Command.command(CLI.create("sleep").
        addArgument(new Argument().setArgName("seconds")).
        addOption(new Option().setArgName("help").setFlag(true).setShortName("h").setLongName("help")));
    sleepCmd.processHandler(sleep::run);
    return sleepCmd;
  }

  static Command echo() {
    Command cmd = Command.command("echo");
    cmd.processHandler(process -> {
      boolean first = true;
      for (String token : process.args()) {
        if (!first) {
          process.write(" ");
        }
        process.write(token);
        first = false;
      }
      process.write("\n");
      process.end();
    });
    return cmd;
  }

  static Command help() {
    Command cmd = Command.command("help");
    cmd.processHandler(process -> {
      CommandRegistry manager = CommandRegistry.get(process.vertx());
      manager.registrations();
      process.write("available commands:\n");
      for (CommandRegistration command : manager.registrations()) {
        process.write(command.command().name()).write("\n");
      }
      process.end();
    });
    return cmd;
  }
}
