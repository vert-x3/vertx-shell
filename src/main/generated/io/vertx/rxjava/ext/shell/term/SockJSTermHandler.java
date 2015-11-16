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

package io.vertx.rxjava.ext.shell.term;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import io.vertx.rxjava.ext.web.handler.sockjs.SockJSSocket;
import io.vertx.rxjava.core.Vertx;
import io.vertx.core.Handler;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.term.SockJSTermHandler original} non RX-ified interface using Vert.x codegen.
 */

public class SockJSTermHandler implements Handler<SockJSSocket> {

  final io.vertx.ext.shell.term.SockJSTermHandler delegate;

  public SockJSTermHandler(io.vertx.ext.shell.term.SockJSTermHandler delegate) {
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public void handle(SockJSSocket arg0) { 
    this.delegate.handle((io.vertx.ext.web.handler.sockjs.SockJSSocket) arg0.getDelegate());
  }

  public static SockJSTermHandler create(Vertx vertx) { 
    SockJSTermHandler ret= SockJSTermHandler.newInstance(io.vertx.ext.shell.term.SockJSTermHandler.create((io.vertx.core.Vertx) vertx.getDelegate()));
    return ret;
  }

  public SockJSTermHandler termHandler(Handler<Term> handler) { 
    this.delegate.termHandler(new Handler<io.vertx.ext.shell.term.Term>() {
      public void handle(io.vertx.ext.shell.term.Term event) {
        handler.handle(new Term(event));
      }
    });
    return this;
  }


  public static SockJSTermHandler newInstance(io.vertx.ext.shell.term.SockJSTermHandler arg) {
    return arg != null ? new SockJSTermHandler(arg) : null;
  }
}
