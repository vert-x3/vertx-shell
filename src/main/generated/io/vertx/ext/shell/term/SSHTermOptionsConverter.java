package io.vertx.ext.shell.term;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter and mapper for {@link io.vertx.ext.shell.term.SSHTermOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.term.SSHTermOptions} original class using Vert.x codegen.
 */
public class SSHTermOptionsConverter {

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, SSHTermOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "host":
          if (member.getValue() instanceof String) {
            obj.setHost((String)member.getValue());
          }
          break;
        case "port":
          if (member.getValue() instanceof Number) {
            obj.setPort(((Number)member.getValue()).intValue());
          }
          break;
        case "keyPairOptions":
          if (member.getValue() instanceof JsonObject) {
            obj.setKeyPairOptions(new io.vertx.core.net.JksOptions((io.vertx.core.json.JsonObject)member.getValue()));
          }
          break;
        case "pfxKeyPairOptions":
          if (member.getValue() instanceof JsonObject) {
            obj.setPfxKeyPairOptions(new io.vertx.core.net.PfxOptions((io.vertx.core.json.JsonObject)member.getValue()));
          }
          break;
        case "pemKeyPairOptions":
          if (member.getValue() instanceof JsonObject) {
            obj.setPemKeyPairOptions(new io.vertx.core.net.PemKeyCertOptions((io.vertx.core.json.JsonObject)member.getValue()));
          }
          break;
        case "authOptions":
          if (member.getValue() instanceof JsonObject) {
            obj.setAuthOptions(((JsonObject)member.getValue()).copy());
          }
          break;
        case "defaultCharset":
          if (member.getValue() instanceof String) {
            obj.setDefaultCharset((String)member.getValue());
          }
          break;
        case "intputrc":
          if (member.getValue() instanceof String) {
            obj.setIntputrc((String)member.getValue());
          }
          break;
      }
    }
  }

   static void toJson(SSHTermOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(SSHTermOptions obj, java.util.Map<String, Object> json) {
    if (obj.getHost() != null) {
      json.put("host", obj.getHost());
    }
    json.put("port", obj.getPort());
    if (obj.getAuthOptions() != null) {
      json.put("authOptions", obj.getAuthOptions());
    }
    if (obj.getDefaultCharset() != null) {
      json.put("defaultCharset", obj.getDefaultCharset());
    }
    if (obj.getIntputrc() != null) {
      json.put("intputrc", obj.getIntputrc());
    }
  }
}
