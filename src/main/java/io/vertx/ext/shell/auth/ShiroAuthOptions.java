package io.vertx.ext.shell.auth;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject()
public class ShiroAuthOptions extends AuthOptions {

  private ShiroAuthRealmType type;
  private JsonObject config;

  public ShiroAuthOptions() {
  }

  public ShiroAuthOptions(ShiroAuthOptions that) {
    type = that.type;
    config = that.config != null ? that.config.copy() : null;
  }

  public ShiroAuthOptions(JsonObject json) {
    type = json.getString("type") != null ? ShiroAuthRealmType.valueOf(json.getString("type")) : null;
    config = json.getJsonObject("config") != null ? json.getJsonObject("config").copy() : null;
  }

  public ShiroAuthRealmType getType() {
    return type;
  }

  public ShiroAuthOptions setType(ShiroAuthRealmType type) {
    this.type = type;
    return this;
  }

  public JsonObject getConfig() {
    return config;
  }

  public ShiroAuthOptions setConfig(JsonObject config) {
    this.config = config;
    return this;
  }

  @Override
  public AuthOptions clone() {
    return new ShiroAuthOptions(this);
  }
}
