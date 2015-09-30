package io.vertx.ext.shell.auth;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * A common base object for authentication options.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public abstract class AuthOptions {

  public AuthOptions() {
  }

  public AuthOptions(AuthOptions that) {
  }

  public AuthOptions(JsonObject json) {
  }

  public abstract AuthOptions clone();

}
