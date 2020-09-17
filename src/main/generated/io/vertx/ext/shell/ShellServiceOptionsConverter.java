package io.vertx.ext.shell;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter and mapper for {@link io.vertx.ext.shell.ShellServiceOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.ShellServiceOptions} original class using Vert.x codegen.
 */
public class ShellServiceOptionsConverter {


  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, ShellServiceOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "httpOptions":
          if (member.getValue() instanceof JsonObject) {
            obj.setHttpOptions(new io.vertx.ext.shell.term.HttpTermOptions((io.vertx.core.json.JsonObject)member.getValue()));
          }
          break;
        case "sshOptions":
          if (member.getValue() instanceof JsonObject) {
            obj.setSSHOptions(new io.vertx.ext.shell.term.SSHTermOptions((io.vertx.core.json.JsonObject)member.getValue()));
          }
          break;
        case "telnetOptions":
          if (member.getValue() instanceof JsonObject) {
            obj.setTelnetOptions(new io.vertx.ext.shell.term.TelnetTermOptions((io.vertx.core.json.JsonObject)member.getValue()));
          }
          break;
      }
    }
  }

  public static void toJson(ShellServiceOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(ShellServiceOptions obj, java.util.Map<String, Object> json) {
    if (obj.getHttpOptions() != null) {
      json.put("httpOptions", obj.getHttpOptions().toJson());
    }
    if (obj.getTelnetOptions() != null) {
      json.put("telnetOptions", obj.getTelnetOptions().toJson());
    }
  }
}
