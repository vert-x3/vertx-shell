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

package io.vertx.rxjava.ext.shell.command;

import java.util.Map;
import io.vertx.lang.rxjava.InternalHelper;
import rx.Observable;
import java.util.List;
import io.vertx.rxjava.core.cli.CommandLine;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.ext.shell.cli.CliToken;
import io.vertx.core.Handler;
import io.vertx.rxjava.ext.shell.term.Tty;
import io.vertx.rxjava.ext.shell.session.Session;

/**
 * The command process provides interaction with the process of the command provided by Vert.x Shell.
 *
 * <p/>
 * NOTE: This class has been automatically generated from the {@link io.vertx.ext.shell.command.CommandProcess original} non RX-ified interface using Vert.x codegen.
 */

public class CommandProcess extends Tty {

  final io.vertx.ext.shell.command.CommandProcess delegate;

  public CommandProcess(io.vertx.ext.shell.command.CommandProcess delegate) {
    super(delegate);
    this.delegate = delegate;
  }

  public Object getDelegate() {
    return delegate;
  }

  /**
   * @return the current Vert.x instance
   * @return 
   */
  public Vertx vertx() { 
    Vertx ret= Vertx.newInstance(this.delegate.vertx());
    return ret;
  }

  /**
   * @return the unparsed arguments tokens
   * @return 
   */
  public List<CliToken> argsTokens() { 
    List<CliToken> ret = this.delegate.argsTokens().stream().map(CliToken::newInstance).collect(java.util.stream.Collectors.toList());
    return ret;
  }

  /**
   * @return the actual string arguments of the command
   * @return 
   */
  public List<String> args() { 
    List<String> ret = this.delegate.args();
;
    return ret;
  }

  /**
   * @return the command line object or null
   * @return 
   */
  public CommandLine commandLine() { 
    CommandLine ret= CommandLine.newInstance(this.delegate.commandLine());
    return ret;
  }

  /**
   * @return the shell session
   * @return 
   */
  public Session session() { 
    Session ret= Session.newInstance(this.delegate.session());
    return ret;
  }

  /**
   * @return true if the command is running in foreground
   * @return 
   */
  public boolean isForeground() { 
    boolean ret = this.delegate.isForeground();
    return ret;
  }

  public CommandProcess stdinHandler(Handler<String> handler) { 
    ( /* Work around for https://jira.codehaus.org/browse/GROOVY-6970 */ (io.vertx.ext.shell.command.CommandProcess) delegate).stdinHandler(handler);
    return this;
  }

  /**
   * Set an interrupt handler, this handler is called when the command is interrupted, for instance user
   * press <code>Ctrl-C</code>.
   * @param handler the interrupt handler
   * @return this command
   */
  public CommandProcess interruptHandler(Handler<Void> handler) { 
    this.delegate.interruptHandler(handler);
    return this;
  }

  /**
   * Set a suspend handler, this handler is called when the command is suspended, for instance user
   * press <code>Ctrl-Z</code>.
   * @param handler the interrupt handler
   * @return this command
   */
  public CommandProcess suspendHandler(Handler<Void> handler) { 
    this.delegate.suspendHandler(handler);
    return this;
  }

  /**
   * Set a resume handler, this handler is called when the command is resumed, for instance user
   * types <code>bg</code> or <code>fg</code> to resume the command.
   * @param handler the interrupt handler
   * @return this command
   */
  public CommandProcess resumeHandler(Handler<Void> handler) { 
    this.delegate.resumeHandler(handler);
    return this;
  }

  /**
   * Set an end handler, this handler is called when the command is ended, for instance the command is running
   * and the shell closes.
   * @param handler the end handler
   * @return a reference to this, so the API can be used fluently
   */
  public CommandProcess endHandler(Handler<Void> handler) { 
    this.delegate.endHandler(handler);
    return this;
  }

  /**
   * Write some text to the standard output.
   * @param data the text
   * @return a reference to this, so the API can be used fluently
   */
  public CommandProcess write(String data) { 
    ( /* Work around for https://jira.codehaus.org/browse/GROOVY-6970 */ (io.vertx.ext.shell.command.CommandProcess) delegate).write(data);
    return this;
  }

  /**
   * Set a background handler, this handler is called when the command is running and put to background.
   * @param handler the background handler
   * @return this command
   */
  public CommandProcess backgroundHandler(Handler<Void> handler) { 
    this.delegate.backgroundHandler(handler);
    return this;
  }

  /**
   * Set a foreground handler, this handler is called when the command is running and put to foreground.
   * @param handler the foreground handler
   * @return this command
   */
  public CommandProcess foregroundHandler(Handler<Void> handler) { 
    this.delegate.foregroundHandler(handler);
    return this;
  }

  public CommandProcess resizehandler(Handler<Void> handler) { 
    ( /* Work around for https://jira.codehaus.org/browse/GROOVY-6970 */ (io.vertx.ext.shell.command.CommandProcess) delegate).resizehandler(handler);
    return this;
  }

  /**
   * End the process with the exit status 
   */
  public void end() { 
    this.delegate.end();
  }

  /**
   * End the process.
   * @param status the exit status.
   */
  public void end(int status) { 
    this.delegate.end(status);
  }


  public static CommandProcess newInstance(io.vertx.ext.shell.command.CommandProcess arg) {
    return arg != null ? new CommandProcess(arg) : null;
  }
}
