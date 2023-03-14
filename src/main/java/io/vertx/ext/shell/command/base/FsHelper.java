/*
 * Copyright 2015 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 *
 *
 * Copyright (c) 2015 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 *
 */

package io.vertx.ext.shell.command.base;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.file.FileProps;
import io.vertx.core.file.FileSystem;
import io.vertx.ext.shell.cli.Completion;

import java.io.File;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
class FsHelper {

  private final Path rootDir;

  FsHelper() {
    rootDir = new File(System.getProperty("vertx.cwd", ".")).getAbsoluteFile().toPath().normalize();
  }

  String rootDir() {
    return rootDir.toString();
  }

  void cd(FileSystem fs, String currentPath, String pathArg, Handler<AsyncResult<String>> pathHandler) {
    Path base = currentPath != null ? new File(currentPath).toPath() : rootDir;
    String path = base.resolve(pathArg).toAbsolutePath().normalize().toString();
    fs.props(path)
      .onComplete(ar -> {
      if (ar.succeeded() && ar.result().isDirectory()) {
        pathHandler.handle(Future.succeededFuture(path));
      } else {
        pathHandler.handle(Future.failedFuture(new NoSuchFileException(path)));
      }
    });
  }

  void ls(Vertx vertx, String currentFile, String pathArg, Handler<AsyncResult<Map<String, FileProps>>> filesHandler) {
    Path base = currentFile != null ? new File(currentFile).toPath() : rootDir;
    String path = base.resolve(pathArg).toAbsolutePath().normalize().toString();
    vertx.<Map<String, FileProps>>executeBlocking(fut -> {
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
    }).onComplete(filesHandler);
  }

  Handler<Completion> completionHandler() {
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

  void complete(Vertx vertx, String currentPath, String _prefix, Handler<AsyncResult<Map<String, Boolean>>> handler) {
    vertx.<Map<String, Boolean>>executeBlocking(fut -> {

      FileSystem fs = vertx.fileSystem();
      Path base = (currentPath != null ? new File(currentPath).toPath() : rootDir);

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
          matches.forEach((suffix, terminal) -> tmp.put(prefix + suffix, terminal));
          matches = tmp;
        }
      }

      fut.complete(matches);
    }).onComplete(handler);


  }
}
