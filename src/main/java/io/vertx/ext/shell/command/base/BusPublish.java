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

import io.vertx.core.cli.annotations.Argument;
import io.vertx.core.cli.annotations.Description;
import io.vertx.core.cli.annotations.Name;
import io.vertx.core.cli.annotations.Option;
import io.vertx.core.cli.annotations.Summary;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.ext.shell.command.AnnotatedCommand;
import io.vertx.ext.shell.command.CommandProcess;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Name("bus-publish")
@Summary("Publish a message to the event bus")
public class BusPublish extends AnnotatedCommand {

  private Pattern HEADER_PATTERN = Pattern.compile("(.*):(.*)");

  protected boolean verbose;
  protected String address;
  protected String body;
  protected DeliveryOptions options = new DeliveryOptions();
  protected ObjectType type = ObjectType.STRING;

  @Argument(index =  0, argName = "address")
  @Description("the bus address destination")
  public void setAddress(String address) {
    this.address = address;
  }

  @Argument(index =  1, argName = "body", required = false)
  @Description("the message body")
  public void setBody(String body) {
    this.body = body;
  }

  @Option(longName = "header", acceptMultipleValues = true)
  @Description("add an header formatted as header_name:header_value")
  public void setHeaders(List<String> headers) {
    for (String header : headers) {
      Matcher matcher = HEADER_PATTERN.matcher(header);
      if (!matcher.matches()) {
        throw new IllegalArgumentException("Invalid header value, use: --header foo:bar");
      }
      options.addHeader(matcher.group(1), matcher.group(2));
    }
  }

  @Option(longName = "verbose", flag = true)
  @Description("verbose output")
  public void setVerbose(boolean verbose) {
    this.verbose = verbose;
  }

  @Option(longName = "type")
  @Description("the object type")
  public void setType(ObjectType type) {
    this.type = type;
  }

  protected Object parseBody() {
    return body != null ? type.parser.apply(this.body) : null;
  }

  @Override
  public void process(CommandProcess process) {
    Object body = parseBody();
    process.vertx().eventBus().publish(address, body, options);
    process.end();
  }
}
