package io.vertx.ext.shell.command.base;

import io.vertx.core.cli.annotations.Argument;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("cd")
@Summary("Change the current working dir")
public class FileSystemCd implements Command {

  private String dir;

  @Argument(index = 0, argName = "dir", required = false)
  @Description("the new working dir")
  public void setDir(String dir) {
    this.dir = dir;
  }

  @Override
  public void process(CommandProcess process) {
    if (process.args().size() > 0) {
      String cwd = process.session().get("cwd");
      new FsHelper().cd(process.vertx().fileSystem(), cwd, dir, ar -> {
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
  }

  @Override
  public void complete(Completion completion) {
    new FsHelper().completionHandler().handle(completion);
  }
}
