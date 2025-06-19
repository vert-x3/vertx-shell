package io.vertx.ext.shell.term;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link io.vertx.ext.shell.term.TelnetTermOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.term.TelnetTermOptions} original class using Vert.x codegen.
 */
public class TelnetTermOptionsConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, TelnetTermOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "charset":
          if (member.getValue() instanceof String) {
            obj.setCharset((String)member.getValue());
          }
          break;
        case "inBinary":
          if (member.getValue() instanceof Boolean) {
            obj.setInBinary((Boolean)member.getValue());
          }
          break;
        case "intputrc":
          if (member.getValue() instanceof String) {
            obj.setIntputrc((String)member.getValue());
          }
          break;
        case "outBinary":
          if (member.getValue() instanceof Boolean) {
            obj.setOutBinary((Boolean)member.getValue());
          }
          break;
      }
    }
  }

   static void toJson(TelnetTermOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(TelnetTermOptions obj, java.util.Map<String, Object> json) {
    if (obj.getCharset() != null) {
      json.put("charset", obj.getCharset());
    }
    json.put("inBinary", obj.getInBinary());
    if (obj.getIntputrc() != null) {
      json.put("intputrc", obj.getIntputrc());
    }
    json.put("outBinary", obj.getOutBinary());
  }
}
