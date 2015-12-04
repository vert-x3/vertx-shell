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

package io.vertx.ext.shell.system;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter for {@link io.vertx.ext.shell.system.ProcessStatus}.
 *
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.system.ProcessStatus} original class using Vert.x codegen.
 */
public class ProcessStatusConverter {

  public static void fromJson(JsonObject json, ProcessStatus obj) {
    if (json.getValue("execStatus") instanceof String) {
      obj.setExecStatus(io.vertx.ext.shell.system.ExecStatus.valueOf((String)json.getValue("execStatus")));
    }
    if (json.getValue("exitCode") instanceof Number) {
      obj.setExitCode(((Number)json.getValue("exitCode")).intValue());
    }
    if (json.getValue("foreground") instanceof Boolean) {
      obj.setForeground((Boolean)json.getValue("foreground"));
    }
  }

  public static void toJson(ProcessStatus obj, JsonObject json) {
    if (obj.getExecStatus() != null) {
      json.put("execStatus", obj.getExecStatus().name());
    }
    json.put("exitCode", obj.getExitCode());
    json.put("foreground", obj.isForeground());
  }
}