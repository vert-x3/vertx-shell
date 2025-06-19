package io.vertx.ext.shell;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.ext.shell.ShellServerOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.ShellServerOptions} original class using Vert.x codegen.
 */
public class ShellServerOptionsConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ShellServerOptions obj) {
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

   static void toJson(ShellServerOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(ShellServerOptions obj, java.util.Map<String, Object> json) {
    json.put("reaperInterval", obj.getReaperInterval());
    json.put("sessionTimeout", obj.getSessionTimeout());
    if (obj.getWelcomeMessage() != null) {
      json.put("welcomeMessage", obj.getWelcomeMessage());
    }
  }
}
