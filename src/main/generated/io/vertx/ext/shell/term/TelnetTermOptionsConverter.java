package io.vertx.ext.shell.term;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import io.vertx.core.spi.json.JsonCodec;

/**
 * Converter and Codec for {@link io.vertx.ext.shell.term.TelnetTermOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.term.TelnetTermOptions} original class using Vert.x codegen.
 */
public class TelnetTermOptionsConverter implements JsonCodec<TelnetTermOptions, JsonObject> {

  public static final TelnetTermOptionsConverter INSTANCE = new TelnetTermOptionsConverter();

  @Override public JsonObject encode(TelnetTermOptions value) { return (value != null) ? value.toJson() : null; }

  @Override public TelnetTermOptions decode(JsonObject value) { return (value != null) ? new TelnetTermOptions(value) : null; }

  @Override public Class<TelnetTermOptions> getTargetClass() { return TelnetTermOptions.class; }

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, TelnetTermOptions obj) {
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

  public static void toJson(TelnetTermOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(TelnetTermOptions obj, java.util.Map<String, Object> json) {
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
