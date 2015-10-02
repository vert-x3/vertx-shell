package io.vertx.ext.shell.command.base;

import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("pwd")
@Summary("Print the current working dir")
public class FileSystemPwd implements Command {

  @Override
  public void process(CommandProcess process) {
    String cwd = process.session().get("cwd");
    if (cwd == null) {
      cwd = new FsHelper().rootDir();
    }
    process.write(cwd).write("\n").end();
  }
}
