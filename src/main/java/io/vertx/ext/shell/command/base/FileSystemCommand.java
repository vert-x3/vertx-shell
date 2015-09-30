package io.vertx.ext.shell.command.base;

import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.Option;
import io.vertx.core.file.FileProps;
import io.vertx.ext.shell.command.Command;

import java.util.AbstractMap;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
public interface FileSystemCommand {

  static Command cd() {
    Command cmd = Command.command(CLI.
            create("cd").
            setSummary("Change the current working dir").
            addOption(new Option().setArgName("help").setFlag(true).setShortName("h").setLongName("help")).
            addArgument(new Argument().setArgName("dir").setRequired(false).setDefaultValue("the new working dir"))
    );
    cmd.completionHandler(new FsHelper().completionHandler());
    cmd.processHandler(process -> {
      if (process.args().size() > 0) {
        String dirArg = process.commandLine().getArgumentValue("dir");
        String cwd = process.session().get("cwd");
        new FsHelper().cd(process.vertx().fileSystem(), cwd, dirArg, ar -> {
          if (ar.succeeded()) {
            process.session().put("cwd", ar.result());
            process.end();
          } else {
            process.write("cd: No such file or directory\n");
            process.end();
          }
        });
      } else {
        process.session().remove("cwd");
        process.end();
      }
    });
    return cmd;
  }

  static Command pwd() {
    Command cmd = Command.command(CLI.
            create("pwd").
            setSummary("Print the current working dir").
            addOption(new Option().setArgName("help").setFlag(true).setShortName("h").setLongName("help"))
    );
    cmd.processHandler(process -> {
      String cwd = process.session().get("cwd");
      if (cwd == null) {
        cwd = new FsHelper().rootDir();
      }
      process.write(cwd).write("\n").end();
    });
    return cmd;
  }

  static Command ls() {
    Option ELL = new Option().setArgName("ell").setFlag(true).setShortName("l").setDescription("List in long format");
    Argument FILE = new Argument().setArgName("file").setRequired(false).setDescription("The file to list");
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
      String fileArg = process.commandLine().getArgumentValue("file");
      new FsHelper().ls(process.vertx(),
          process.session().get("cwd"),
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
            } else {
              process.write("ls: " + ar.cause().getMessage() + "\n");
            }
            process.end();
          });
    });
    return cmd;
  }
}
