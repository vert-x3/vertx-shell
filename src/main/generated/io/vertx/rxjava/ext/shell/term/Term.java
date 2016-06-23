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
import rx.Observable;
import io.vertx.rxjava.ext.shell.cli.Completion;
import io.vertx.core.Handler;
import io.vertx.rxjava.ext.shell.session.Session;

/**
 * The terminal.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.term.Term original} non RX-ified interface using Vert.x codegen.
 */

public class Term extends Tty {

  final io.vertx.ext.shell.term.Term delegate;

  public Term(io.vertx.ext.shell.term.Term delegate) {
    super(delegate);
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  public Term resizehandler(Handler<Void> handler) { 
    ((io.vertx.ext.shell.term.Term) delegate).resizehandler(handler);
    return this;
  }

  public Term stdinHandler(Handler<String> handler) { 
    ((io.vertx.ext.shell.term.Term) delegate).stdinHandler(handler);
    return this;
  }

  public Term write(String data) { 
    ((io.vertx.ext.shell.term.Term) delegate).write(data);
    return this;
  }

  /**
   * @return the last time this term received input
   * @return 
   */
  public long lastAccessedTime() { 
    long ret = delegate.lastAccessedTime();
    return ret;
  }

  /**
   * Echo some text in the terminal, escaped if necessary.<p/>
   * @param text the text to echo
   * @return a reference to this, so the API can be used fluently
   */
  public Term echo(String text) { 
    delegate.echo(text);
    return this;
  }

  /**
   * Associate the term with a session.
   * @param session the session to set
   * @return a reference to this, so the API can be used fluently
   */
  public Term setSession(Session session) { 
    Term ret = Term.newInstance(delegate.setSession((io.vertx.ext.shell.session.Session)session.getDelegate()));
    return ret;
  }

  /**
   * Set an interrupt signal handler on the term.
   * @param handler the interrupt handler
   * @return a reference to this, so the API can be used fluently
   */
  public Term interruptHandler(SignalHandler handler) { 
    delegate.interruptHandler((io.vertx.ext.shell.term.SignalHandler)handler.getDelegate());
    return this;
  }

  /**
   * Set a suspend signal handler on the term.
   * @param handler the suspend handler
   * @return a reference to this, so the API can be used fluently
   */
  public Term suspendHandler(SignalHandler handler) { 
    delegate.suspendHandler((io.vertx.ext.shell.term.SignalHandler)handler.getDelegate());
    return this;
  }

  /**
   * Prompt the user a line of text.
   * @param prompt the displayed prompt
   * @param lineHandler the line handler called with the line
   */
  public void readline(String prompt, Handler<String> lineHandler) { 
    delegate.readline(prompt, lineHandler);
  }

  /**
   * Prompt the user a line of text, providing a completion handler to handle user's completion.
   * @param prompt the displayed prompt
   * @param lineHandler the line handler called with the line
   * @param completionHandler the completion handler
   */
  public void readline(String prompt, Handler<String> lineHandler, Handler<Completion> completionHandler) { 
    delegate.readline(prompt, lineHandler, new Handler<io.vertx.ext.shell.cli.Completion>() {
      public void handle(io.vertx.ext.shell.cli.Completion event) {
        completionHandler.handle(Completion.newInstance(event));
      }
    });
  }

  /**
   * Set a handler that will be called when the terminal is closed.
   * @param handler the handler
   * @return a reference to this, so the API can be used fluently
   */
  public Term closeHandler(Handler<Void> handler) { 
    delegate.closeHandler(handler);
    return this;
  }

  /**
   * Close the connection to terminal.
   */
  public void close() { 
    delegate.close();
  }


  public static Term newInstance(io.vertx.ext.shell.term.Term arg) {
    return arg != null ? new Term(arg) : null;
  }
}
