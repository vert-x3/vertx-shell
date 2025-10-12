package io.vertx.ext.shell.term;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter and mapper for {@link io.vertx.ext.shell.term.TelnetTermOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.term.TelnetTermOptions} original class using Vert.x codegen.
 */
public class TelnetTermOptionsConverter {

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, TelnetTermOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "outBinary":
          if (member.getValue() instanceof Boolean) {
            obj.setOutBinary((Boolean)member.getValue());
          }
          break;
        case "inBinary":
          if (member.getValue() instanceof Boolean) {
            obj.setInBinary((Boolean)member.getValue());
          }
          break;
        case "charset":
          if (member.getValue() instanceof String) {
            obj.setCharset((String)member.getValue());
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

   static void toJson(TelnetTermOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(TelnetTermOptions obj, java.util.Map<String, Object> json) {
    json.put("outBinary", obj.getOutBinary());
    json.put("inBinary", obj.getInBinary());
    if (obj.getCharset() != null) {
      json.put("charset", obj.getCharset());
    }
    if (obj.getIntputrc() != null) {
      json.put("intputrc", obj.getIntputrc());
    }
  }
}
