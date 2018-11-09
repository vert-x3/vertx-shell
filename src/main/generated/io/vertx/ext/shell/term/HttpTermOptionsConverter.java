package io.vertx.ext.shell.term;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link io.vertx.ext.shell.term.HttpTermOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.term.HttpTermOptions} original class using Vert.x codegen.
 */
public class HttpTermOptionsConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, HttpTermOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "authOptions":
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
        case "shellHtmlResource":
          if (member.getValue() instanceof String) {
            obj.setShellHtmlResource(io.vertx.core.buffer.Buffer.buffer(java.util.Base64.getDecoder().decode((String)member.getValue())));
          }
          break;
        case "sockJSHandlerOptions":
          if (member.getValue() instanceof JsonObject) {
            obj.setSockJSHandlerOptions(new io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions((JsonObject)member.getValue()));
          }
          break;
        case "sockJSPath":
          if (member.getValue() instanceof String) {
            obj.setSockJSPath((String)member.getValue());
          }
          break;
        case "termJsResource":
          if (member.getValue() instanceof String) {
            obj.setTermJsResource(io.vertx.core.buffer.Buffer.buffer(java.util.Base64.getDecoder().decode((String)member.getValue())));
          }
          break;
        case "vertsShellJsResource":
          if (member.getValue() instanceof String) {
            obj.setVertsShellJsResource(io.vertx.core.buffer.Buffer.buffer(java.util.Base64.getDecoder().decode((String)member.getValue())));
          }
          break;
      }
    }
  }

  public static void toJson(HttpTermOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(HttpTermOptions obj, java.util.Map<String, Object> json) {
    if (obj.getCharset() != null) {
      json.put("charset", obj.getCharset());
    }
    if (obj.getIntputrc() != null) {
      json.put("intputrc", obj.getIntputrc());
    }
    if (obj.getShellHtmlResource() != null) {
      json.put("shellHtmlResource", java.util.Base64.getEncoder().encodeToString(obj.getShellHtmlResource().getBytes()));
    }
    if (obj.getSockJSPath() != null) {
      json.put("sockJSPath", obj.getSockJSPath());
    }
    if (obj.getTermJsResource() != null) {
      json.put("termJsResource", java.util.Base64.getEncoder().encodeToString(obj.getTermJsResource().getBytes()));
    }
    if (obj.getVertsShellJsResource() != null) {
      json.put("vertsShellJsResource", java.util.Base64.getEncoder().encodeToString(obj.getVertsShellJsResource().getBytes()));
    }
  }
}
