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

package io.vertx.groovy.ext.shell;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
/**
 * A shell session.
*/
@CompileStatic
public class Session {
  private final def io.vertx.ext.shell.Session delegate;
  public Session(Object delegate) {
    this.delegate = (io.vertx.ext.shell.Session) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * Put some data in a session
   * @param key the key for the data
   * @param obj the data
   * @return a reference to this, so the API can be used fluently
   */
  public Session put(String key, Object obj) {
    this.delegate.put(key, InternalHelper.unwrapObject(obj));
    return this;
  }
  /**
   * Get some data from the session
   * @param key the key of the data
   * @return the data
   */
  public <T> T get(String key) {
    // This cast is cleary flawed
    def ret = (T) InternalHelper.wrapObject(this.delegate.get(key));
    return ret;
  }
  /**
   * Remove some data from the session
   * @param key the key of the data
   * @return the data that was there or null if none there
   */
  public <T> T remove(String key) {
    // This cast is cleary flawed
    def ret = (T) InternalHelper.wrapObject(this.delegate.remove(key));
    return ret;
  }
}
