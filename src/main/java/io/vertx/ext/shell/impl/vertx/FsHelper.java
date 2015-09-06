package io.vertx.ext.shell.impl.vertx;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import io.vertx.ext.shell.cli.Completion;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class FsHelper {

  private final Path root;

  public FsHelper() {
    root = new File(System.getProperty("vertx.cwd", ".")).getAbsoluteFile().toPath().normalize();
  }

  public String getRootPath() {
    return root.toString();
  }

  public void cd(FileSystem fs, String currentPath, String pathArg, Handler<AsyncResult<String>> pathHandler) {
    Path base = currentPath != null ? new File(currentPath).toPath() : root;
    String path = base.resolve(pathArg).toAbsolutePath().normalize().toString();
    fs.props(path, ar -> {
      if (ar.succeeded() && ar.result().isDirectory()) {
        pathHandler.handle(Future.succeededFuture(path));
      } else {
        pathHandler.handle(Future.failedFuture(path + ": No such file or directory"));
      }
    });
  }

  public void ls(Vertx vertx, String currentPath, String pathArg, Handler<AsyncResult<Map<String, FileProps>>> filesHandler) {
    Path base = currentPath != null ? new File(currentPath).toPath() : root;
    String path = base.resolve(pathArg).toAbsolutePath().normalize().toString();
    vertx.executeBlocking(fut -> {
      FileSystem fs = vertx.fileSystem();
      if (fs.propsBlocking(path).isDirectory()) {
        LinkedHashMap<String, FileProps> result = new LinkedHashMap<>();
        for (String file : fs.readDirBlocking(path)) {
          result.put(file, fs.propsBlocking(file));
        }
        fut.complete(result);
      } else {
        throw new RuntimeException(path + ": No such file or directory");
      }
    }, filesHandler);
  }

  public Handler<Completion> completionHandler() {
    return completion -> {
      String last;
      int s = completion.lineTokens().size();
      if (s > 0 && completion.lineTokens().get(s - 1).isText()) {
        last = completion.lineTokens().get(s - 1).value();
      } else {
        last = "";
      }
      complete(completion.vertx(), completion.session().get("path"), last, result -> {
        if (result.succeeded()) {
          Map<String, Boolean> matches = result.result();
          switch (matches.size()) {
            case 0:
              completion.complete(Collections.emptyList());
              break;
            case 1:
              Map.Entry<String, Boolean> match = matches.entrySet().iterator().next();
              completion.complete(match.getKey(), match.getValue());
              break;
            default:
              completion.complete(new ArrayList<>(matches.keySet()));
              break;
          }
        } else {
          completion.complete(Collections.emptyList());
        }
      });
    };
  }

  public void complete(Vertx vertx, String currentPath, String _prefix, Handler<AsyncResult<Map<String, Boolean>>> handler) {
    vertx.executeBlocking(fut -> {

      FileSystem fs = vertx.fileSystem();
      Path base = (currentPath != null ? new File(currentPath).toPath() : root);

      int index = _prefix.lastIndexOf('/');
      String prefix;
      if (index == 0) {
        handler.handle(Future.failedFuture("todo"));
        return;
      } else if (index > 0) {
        base = base.resolve(_prefix.substring(0, index));
        prefix = _prefix.substring(index + 1);
      } else {
        prefix = _prefix;
      }

      LinkedHashMap<String, Boolean> matches = new LinkedHashMap<>();
      for (String path : fs.readDirBlocking(base.toAbsolutePath().normalize().toString())) {
        String name = path.substring(path.lastIndexOf('/') + 1);
        if (name.startsWith(prefix)) {
          FileProps props = fs.propsBlocking(path);
          matches.put(name.substring(prefix.length()) + (props.isDirectory() ? "/" : ""), props.isRegularFile());
        }
      }

      if (matches.size() > 1) {
        String common = Completion.findLongestCommonPrefix(matches.keySet());
        if (common.length() > 0) {
          matches.clear();
          matches.put(common, false);
        } else {
          LinkedHashMap<String, Boolean> tmp = new LinkedHashMap<>();
          matches.forEach((suffix, terminal) -> {
            tmp.put(prefix + suffix, terminal);
          });
          matches = tmp;
        }
      }


      fut.complete(matches);
    }, handler);


  }
}
