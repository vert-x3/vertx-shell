package io.vertx.ext.shell.term;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonDecoder;

/**
 * Converter and Codec for {@link io.vertx.ext.shell.term.SSHTermOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.term.SSHTermOptions} original class using Vert.x codegen.
 */
public class SSHTermOptionsConverter implements JsonDecoder<SSHTermOptions, JsonObject> {

  public static final SSHTermOptionsConverter INSTANCE = new SSHTermOptionsConverter();

  @Override public SSHTermOptions decode(JsonObject value) { return (value != null) ? new SSHTermOptions(value) : null; }

  @Override public Class<SSHTermOptions> getTargetClass() { return SSHTermOptions.class; }

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, SSHTermOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "defaultCharset":
          if (member.getValue() instanceof String) {
            obj.setDefaultCharset((String)member.getValue());
          }
          break;
        case "host":
          if (member.getValue() instanceof String) {
            obj.setHost((String)member.getValue());
          }
          break;
        case "intputrc":
          if (member.getValue() instanceof String) {
            obj.setIntputrc((String)member.getValue());
          }
          break;
        case "keyPairOptions":
          if (member.getValue() instanceof JsonObject) {
            obj.setKeyPairOptions(new io.vertx.core.net.JksOptions((JsonObject)member.getValue()));
          }
          break;
        case "pemKeyPairOptions":
          if (member.getValue() instanceof JsonObject) {
            obj.setPemKeyPairOptions(new io.vertx.core.net.PemKeyCertOptions((JsonObject)member.getValue()));
          }
          break;
        case "pfxKeyPairOptions":
          if (member.getValue() instanceof JsonObject) {
            obj.setPfxKeyPairOptions(new io.vertx.core.net.PfxOptions((JsonObject)member.getValue()));
          }
          break;
        case "port":
          if (member.getValue() instanceof Number) {
            obj.setPort(((Number)member.getValue()).intValue());
          }
          break;
      }
    }
  }

  public static void toJson(SSHTermOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(SSHTermOptions obj, java.util.Map<String, Object> json) {
    if (obj.getDefaultCharset() != null) {
      json.put("defaultCharset", obj.getDefaultCharset());
    }
    if (obj.getHost() != null) {
      json.put("host", obj.getHost());
    }
    if (obj.getIntputrc() != null) {
      json.put("intputrc", obj.getIntputrc());
    }
    json.put("port", obj.getPort());
  }
}
