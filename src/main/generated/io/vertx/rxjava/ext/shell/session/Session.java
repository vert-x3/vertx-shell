/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.vertx.rxjava.ext.shell.session;

import java.util.Map;
import rx.Observable;

/**
 * A shell session.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.session.Session original} non RX-ified interface using Vert.x codegen.
 */

public class Session {

  final io.vertx.ext.shell.session.Session delegate;

  public Session(io.vertx.ext.shell.session.Session delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * Create a new empty session.
   * @return the created session
   */
  public static Session create() { 
    Session ret = Session.newInstance(io.vertx.ext.shell.session.Session.create());
    return ret;
  }

  /**
   * Put some data in a session
   * @param key the key for the data
   * @param obj the data
   * @return a reference to this, so the API can be used fluently
   */
  public Session put(String key, Object obj) { 
    delegate.put(key, obj);
    return this;
  }

  /**
   * Get some data from the session
   * @param key the key of the data
   * @return the data
   */
  public <T> T get(String key) { 
    T ret = (T) delegate.get(key);
    return ret;
  }

  /**
   * Remove some data from the session
   * @param key the key of the data
   * @return the data that was there or null if none there
   */
  public <T> T remove(String key) { 
    T ret = (T) delegate.remove(key);
    return ret;
  }


  public static Session newInstance(io.vertx.ext.shell.session.Session arg) {
    return arg != null ? new Session(arg) : null;
  }
}
