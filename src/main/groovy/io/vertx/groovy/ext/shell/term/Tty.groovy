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
import io.vertx.core.Handler
/**
 * Provide interactions with the Shell TTY.
*/
@CompileStatic
public class Tty {
  private final def io.vertx.ext.shell.term.Tty delegate;
  public Tty(Object delegate) {
    this.delegate = (io.vertx.ext.shell.term.Tty) delegate;
  }
  public Object getDelegate() {
    return delegate;
  }
  /**
   * @return the declared tty type, for instance , ,  etc... it can be null
   * when the tty does not have declared its type.
   * @return 
   */
  public String type() {
    def ret = this.delegate.type();
    return ret;
  }
  /**
   * @return the current width, i.e the number of rows or  if unknown
   * @return 
   */
  public int width() {
    def ret = this.delegate.width();
    return ret;
  }
  /**
   * @return the current height, i.e the number of columns or  if unknown
   * @return 
   */
  public int height() {
    def ret = this.delegate.height();
    return ret;
  }
  /**
   * Set a stream handler on the standard input to read the data.
   * @param handler the standard input
   * @return this object
   */
  public Tty stdinHandler(Handler<String> handler) {
    this.delegate.stdinHandler(handler);
    return this;
  }
  /**
   * Write data to the standard output.
   * @param data the data to write
   * @return this object
   */
  public Tty write(String data) {
    this.delegate.write(data);
    return this;
  }
  /**
   * Set a resize handler, the handler is called when the tty size changes.
   * @param handler the resize handler
   * @return this object
   */
  public Tty resizehandler(Handler<Void> handler) {
    this.delegate.resizehandler(handler);
    return this;
  }
}
