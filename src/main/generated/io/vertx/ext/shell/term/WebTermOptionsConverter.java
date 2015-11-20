/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.ext.shell.term;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.shell.term.WebTermOptions}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.term.WebTermOptions} original class using Vert.x codegen.
 */
public class WebTermOptionsConverter {

  public static void fromJson(JsonObject json, WebTermOptions obj) {
    if (json.getValue("httpServerOptions") instanceof JsonObject) {
      obj.setHttpServerOptions(new io.vertx.core.http.HttpServerOptions((JsonObject)json.getValue("httpServerOptions")));
    }
    if (json.getValue("shiroAuthOptions") instanceof JsonObject) {
      obj.setShiroAuthOptions(new io.vertx.ext.auth.shiro.ShiroAuthOptions((JsonObject)json.getValue("shiroAuthOptions")));
    }
    if (json.getValue("sockJSHandlerOptions") instanceof JsonObject) {
      obj.setSockJSHandlerOptions(new io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions((JsonObject)json.getValue("sockJSHandlerOptions")));
    }
    if (json.getValue("sockJSPath") instanceof String) {
      obj.setSockJSPath((String)json.getValue("sockJSPath"));
    }
  }

  public static void toJson(WebTermOptions obj, JsonObject json) {
    if (obj.getSockJSPath() != null) {
      json.put("sockJSPath", obj.getSockJSPath());
    }
  }
}