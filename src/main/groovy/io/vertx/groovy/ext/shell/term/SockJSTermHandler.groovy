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

package io.vertx.groovy.ext.shell.term;
import groovy.transform.CompileStatic
import io.vertx.lang.groovy.InternalHelper
import io.vertx.core.json.JsonObject
import io.vertx.groovy.ext.web.handler.sockjs.SockJSSocket
import io.vertx.groovy.core.buffer.Buffer
import io.vertx.groovy.core.Vertx
import io.vertx.core.Handler
/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
*/
@CompileStatic
public class SockJSTermHandler implements Handler<SockJSSocket> {
  private final def io.vertx.ext.shell.term.SockJSTermHandler delegate;
  public SockJSTermHandler(Object delegate) {
    this.delegate = (io.vertx.ext.shell.term.SockJSTermHandler) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  public void handle(SockJSSocket arg0) {
    ((io.vertx.core.Handler) this.delegate).handle((io.vertx.ext.web.handler.sockjs.SockJSSocket)arg0.getDelegate());
  }
  public static Buffer defaultTermScriptResource() {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.term.SockJSTermHandler.defaultTermScriptResource(), io.vertx.groovy.core.buffer.Buffer.class);
    return ret;
  }
  public static Buffer defaultTermMarkupResource() {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.term.SockJSTermHandler.defaultTermMarkupResource(), io.vertx.groovy.core.buffer.Buffer.class);
    return ret;
  }
  public static SockJSTermHandler create(Vertx vertx) {
    def ret= InternalHelper.safeCreate(io.vertx.ext.shell.term.SockJSTermHandler.create((io.vertx.core.Vertx)vertx.getDelegate()), io.vertx.groovy.ext.shell.term.SockJSTermHandler.class);
    return ret;
  }
  public SockJSTermHandler termHandler(Handler<Term> handler) {
    this.delegate.termHandler(new Handler<io.vertx.ext.shell.term.Term>() {
      public void handle(io.vertx.ext.shell.term.Term event) {
        handler.handle(new io.vertx.groovy.ext.shell.term.Term(event));
      }
    });
    return this;
  }
}
