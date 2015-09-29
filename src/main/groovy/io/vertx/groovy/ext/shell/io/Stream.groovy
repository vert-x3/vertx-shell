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

package io.vertx.groovy.ext.shell.io;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.core.json.JsonObject
import io.vertx.core.Handler
/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
@CompileStatic
public class Stream {
  private final def io.vertx.ext.shell.io.Stream delegate;
  public Stream(Object delegate) {
    this.delegate = (io.vertx.ext.shell.io.Stream) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public static Stream ofString(Handler<String> handler) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.io.Stream.ofString(handler), io.vertx.groovy.ext.shell.io.Stream.class);
    return ret;
  }
  public static Stream ofJson(Handler<Map<String, Object>> handler) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.io.Stream.ofJson(new Handler<JsonObject>() {
      public void handle(JsonObject event) {
        handler.handle((Map<String, Object>)InternalHelper.wrapObject(event));
      }
    }), io.vertx.groovy.ext.shell.io.Stream.class);
    return ret;
  }
  public Stream write(String data) {
    this.delegate.write(data);
    return this;
  }
  public Stream write(Map<String, Object> data) {
    this.delegate.write(data != null ? new io.vertx.core.json.JsonObject(data) : null);
    return this;
  }
}
