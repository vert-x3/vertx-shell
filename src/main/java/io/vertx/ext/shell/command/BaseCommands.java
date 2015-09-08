package io.vertx.ext.shell.command;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.impl.HttpServerImpl;
import io.vertx.core.impl.Deployment;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.net.impl.NetServerImpl;
import io.vertx.core.net.impl.ServerID;
import io.vertx.core.shareddata.LocalMap;
import io.vertx.core.shareddata.SharedData;
import io.vertx.core.spi.VerticleFactory;
import io.vertx.ext.shell.getopt.GetOptCommand;
import io.vertx.ext.shell.getopt.GetOptCommandProcess;
import io.vertx.ext.shell.impl.vertx.FsHelper;
import io.vertx.ext.shell.registry.CommandRegistration;
import io.vertx.ext.shell.registry.CommandRegistry;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface BaseCommands {

  static Command verticle_ls() {
    Command deployments = Command.command("verticle-ls");
    deployments.processHandler(process -> {
      VertxInternal vertx = (VertxInternal) process.vertx();
      for (String id : vertx.deploymentIDs()) {
        Deployment deployment = vertx.getDeployment(id);
        process.write(id + ": " + deployment.verticleIdentifier() + ", options=" + deployment.deploymentOptions().toJson() + "\n");

      }
      process.end();

    });
    return deployments;
  }

  static Command verticle_deploy() {
    Command deploy = Command.command("verticle-deploy");
    deploy.processHandler(process -> {
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
    return deploy;
  }

  static Command verticle_undeploy() {
    Command deploy = Command.command("verticle-undeploy");
    deploy.processHandler(process -> {
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
    return deploy;
  }

  static Command verticle_factories() {
    Command deploy = Command.command("verticle-factories");
    deploy.processHandler(process -> {
      VertxInternal vertx = (VertxInternal) process.vertx();
      for (VerticleFactory factory : vertx.verticleFactories()) {
        process.write(factory.getClass().getName() + ": prefix=" + factory.prefix() + ", order=" + factory.order() + ", requiresResolve=" + factory.requiresResolve() + "\n");
      }
      process.end();
    });
    return deploy;
  }

  static Command server_ls() {
    Command server_ls = Command.command("server-ls");
    server_ls.processHandler(process -> {
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
    return server_ls;
  }

  static Command local_map_get() {
    Command shared_data = Command.command("local-map-get");
    shared_data.processHandler(process -> {
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
    return shared_data;
  }

  static Command local_map_put() {
    Command shared_data = Command.command("local-map-put");
    shared_data.processHandler(process -> {
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
    return shared_data;
  }

  static Command local_map_rm() {
    Command shared_data = Command.command("local-map-rm");
    shared_data.processHandler(process -> {
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
    return shared_data;
  }

  static Command bus_send() {
    Command shared_data = Command.command("bus-send");
    shared_data.processHandler(process -> {
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
    return shared_data;
  }

  static Command bus_tail() {
    Command shared_data = Command.command("bus-tail");
    shared_data.processHandler(process -> {
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
    return shared_data;
  }

  static Command fs_cd() {
    Command cmd = Command.command("cd");
    cmd.completeHandler(new FsHelper().completionHandler());
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
    Command cmd = Command.command("ls");
    cmd.completeHandler(new FsHelper().completionHandler());
    cmd.processHandler(process -> {
      new FsHelper().ls(process.vertx(),
          process.session().get("path"),
          process.args().size() > 0 ? process.args().get(0) : ".",
          ar -> {
            if (ar.succeeded()) {
              ar.result().forEach((file, props) -> {
                String name = file.substring(file.lastIndexOf('/') + 1);
                process.write(name + "\n");
              });
            } else {
              process.write("ls:" + ar.cause().getMessage() + "\n");
            }
            process.end();
          });
    });
    return cmd;
  }

  static Command sleep() {
    class SleepImpl {

      void run(GetOptCommandProcess process) {
        if (process.arguments().isEmpty()) {
          process.write("usage: sleep seconds\n");
          process.end();
        } else {
          String arg = process.arguments().get(0);
          int seconds = -1;
          try {
            seconds = Integer.parseInt(arg);
          } catch (NumberFormatException ignore) {
          }
          scheduleSleep(process, seconds * 1000);
        }
      }

      void scheduleSleep(GetOptCommandProcess process, long millis) {
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
    GetOptCommand sleepCmd = GetOptCommand.create("sleep");
    sleepCmd.processHandler(sleep::run);
    return sleepCmd.build();
  }

  static Command echo() {
    Command echo = Command.command("echo");
    echo.processHandler(process -> {
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
    return echo;
  }

  static Command help() {
    Command help = Command.command("help");
    help.processHandler(process -> {
      CommandRegistry manager = CommandRegistry.get(process.vertx());
      manager.registrations();
      process.write("available commands:\n");
      for (CommandRegistration command : manager.registrations()) {
        process.write(command.command().name()).write("\n");
      }
      process.end();
    });
    return help;
  }
}
