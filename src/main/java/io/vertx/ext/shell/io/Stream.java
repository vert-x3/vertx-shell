package io.vertx.ext.shell.io;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@VertxGen
@FunctionalInterface
public interface Stream {

  static Stream ofString(Handler<String> handler) {
    return new Stream() {
      @Override
      public Stream write(Object data) {
        if (data instanceof String) {
          handler.handle((String) data);
        } else if (data instanceof JsonObject) {
          handler.handle(((JsonObject) data).encode());
        } else {
          handler.handle(data != null ? data.toString() : null);
        }
        return this;
      }
    };
  }

  static Stream ofJson(Handler<JsonObject> handler) {
    return new Stream() {
      @Override
      public Stream write(Object data) {
        if (data instanceof JsonObject) {
          handler.handle((JsonObject) data);
        } else {
          handler.handle(data != null ? new JsonObject(data.toString()) : null);
        }
        return this;
      }
    };
  }

  @GenIgnore
  static Stream ofObject(Handler<Object> handler) {
    return new Stream() {
      @Override
      public Stream write(Object data) {
        handler.handle(data);
        return this;
      }
    };
  }

  @Fluent
  default Stream write(String data) {
    return write((Object) data);
  }

  @Fluent
  default Stream write(JsonObject data) {
    return write((Object) data);
  }

  @GenIgnore
  Stream write(Object data);

}
