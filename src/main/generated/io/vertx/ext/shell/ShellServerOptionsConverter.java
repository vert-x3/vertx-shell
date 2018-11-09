package io.vertx.ext.shell;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link io.vertx.ext.shell.ShellServerOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.ShellServerOptions} original class using Vert.x codegen.
 */
public class ShellServerOptionsConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ShellServerOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "reaperInterval":
          if (member.getValue() instanceof Number) {
            obj.setReaperInterval(((Number)member.getValue()).longValue());
          }
          break;
        case "sessionTimeout":
          if (member.getValue() instanceof Number) {
            obj.setSessionTimeout(((Number)member.getValue()).longValue());
          }
          break;
        case "welcomeMessage":
          if (member.getValue() instanceof String) {
            obj.setWelcomeMessage((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(ShellServerOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ShellServerOptions obj, java.util.Map<String, Object> json) {
    json.put("reaperInterval", obj.getReaperInterval());
    json.put("sessionTimeout", obj.getSessionTimeout());
    if (obj.getWelcomeMessage() != null) {
      json.put("welcomeMessage", obj.getWelcomeMessage());
    }
  }
}
