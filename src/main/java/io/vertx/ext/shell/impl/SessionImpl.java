package io.vertx.ext.shell.impl;

import io.vertx.ext.shell.Session;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SessionImpl implements Session {

  private Map<String, Object> data = new HashMap<>();

  @Override
  public Session put(String key, Object obj) {
    if (obj == null) {
      data.remove(key);
    } else {
      data.put(key, obj);
    }
    return this;
  }

  @Override
  public <T> T get(String key) {
    return (T) data.get(key);
  }

  @Override
  public <T> T remove(String key) {
    return (T) data.remove(key);
  }
}
