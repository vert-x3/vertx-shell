/*
 * Copyright 2014 Red Hat, Inc.
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
 * Copyright (c) 2011-2013 The original author or authors
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

import io.vertx.core.cli.annotations.Argument;
import io.vertx.core.cli.annotations.DefaultValue;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Option;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.core.file.FileProps;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandProcess;

import java.util.AbstractMap;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("ls")
@Summary("List directory content")
public class FileSystemLs implements Command {

  private boolean ell;
  private String file;
  private boolean all;

  @Option(shortName = "l", flag = true)
  @Description("list in long format")
  public void setEll(boolean ell) {
    this.ell = ell;
  }

  @Argument(index = 0, argName = "file", required = false)
  @Description("the file to list")
  @DefaultValue(".")
  public void setFile(String file) {
    this.file = file;
  }

  @Option(longName = "all", shortName = "a", required = false)
  @Description("include files that begins with .")
  public void setAll(boolean all) {
    this.all = all;
  }

  @Override
  public void process(CommandProcess process) {
    new FsHelper().ls(process.vertx(),
        process.session().get("cwd"),
        file,
        ar -> {
          if (ar.succeeded()) {
            Map<String, FileProps> result = ar.result();
            if (result.size() > 0) {
              Stream<Map.Entry<String, FileProps>> entries = result.entrySet().stream();

              // Keep only name
              entries = entries.map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey().substring(entry.getKey().lastIndexOf('/') + 1), entry.getValue()));

              // Filter -a option
              if (!all) {
                entries = entries.filter(entry -> !entry.getKey().startsWith("."));
              }

              // Format name
              Function<Map.Entry<String, FileProps>, String> formatter;
              if (ell) {
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
  }

  @Override
  public void complete(Completion completion) {
    new FsHelper().completionHandler().handle(completion);
  }
}
