package io.vertx.ext.shell;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.ext.shell.ShellServerOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.ShellServerOptions} original class using Vert.x codegen.
 */
public class ShellServerOptionsConverter {

  private static final Base64.Decoder BASE64_DECODER = Base64.getUrlDecoder();
  private static final Base64.Encoder BASE64_ENCODER = Base64.getUrlEncoder().withoutPadding();

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ShellServerOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "welcomeMessage":
          if (member.getValue() instanceof String) {
            obj.setWelcomeMessage((String)member.getValue());
          }
          break;
        case "sessionTimeout":
          if (member.getValue() instanceof Number) {
            obj.setSessionTimeout(((Number)member.getValue()).longValue());
          }
          break;
        case "reaperInterval":
          if (member.getValue() instanceof Number) {
            obj.setReaperInterval(((Number)member.getValue()).longValue());
          }
          break;
      }
    }
  }

   static void toJson(ShellServerOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(ShellServerOptions obj, java.util.Map<String, Object> json) {
    if (obj.getWelcomeMessage() != null) {
      json.put("welcomeMessage", obj.getWelcomeMessage());
    }
    json.put("sessionTimeout", obj.getSessionTimeout());
    json.put("reaperInterval", obj.getReaperInterval());
  }
}
