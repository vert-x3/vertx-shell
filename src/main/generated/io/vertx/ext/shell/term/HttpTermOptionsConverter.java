package io.vertx.ext.shell.term;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;

/**
 * Converter and mapper for {@link io.vertx.ext.shell.term.HttpTermOptions}.
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.term.HttpTermOptions} original class using Vert.x codegen.
 */
public class HttpTermOptionsConverter {

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, HttpTermOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "sockJSHandlerOptions":
          if (member.getValue() instanceof JsonObject) {
            obj.setSockJSHandlerOptions(new io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions((io.vertx.core.json.JsonObject)member.getValue()));
          }
          break;
        case "sockJSPath":
          if (member.getValue() instanceof String) {
            obj.setSockJSPath((String)member.getValue());
          }
          break;
        case "authOptions":
          if (member.getValue() instanceof JsonObject) {
            obj.setAuthOptions(((JsonObject)member.getValue()).copy());
          }
          break;
        case "vertsShellJsResource":
          if (member.getValue() instanceof String) {
            obj.setVertsShellJsResource(io.vertx.core.buffer.Buffer.fromJson((String)member.getValue()));
          }
          break;
        case "termJsResource":
          if (member.getValue() instanceof String) {
            obj.setTermJsResource(io.vertx.core.buffer.Buffer.fromJson((String)member.getValue()));
          }
          break;
        case "shellHtmlResource":
          if (member.getValue() instanceof String) {
            obj.setShellHtmlResource(io.vertx.core.buffer.Buffer.fromJson((String)member.getValue()));
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

   static void toJson(HttpTermOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(HttpTermOptions obj, java.util.Map<String, Object> json) {
    if (obj.getSockJSPath() != null) {
      json.put("sockJSPath", obj.getSockJSPath());
    }
    if (obj.getAuthOptions() != null) {
      json.put("authOptions", obj.getAuthOptions());
    }
    if (obj.getVertsShellJsResource() != null) {
      json.put("vertsShellJsResource", obj.getVertsShellJsResource().toJson());
    }
    if (obj.getTermJsResource() != null) {
      json.put("termJsResource", obj.getTermJsResource().toJson());
    }
    if (obj.getShellHtmlResource() != null) {
      json.put("shellHtmlResource", obj.getShellHtmlResource().toJson());
    }
    if (obj.getCharset() != null) {
      json.put("charset", obj.getCharset());
    }
    if (obj.getIntputrc() != null) {
      json.put("intputrc", obj.getIntputrc());
    }
  }
}
