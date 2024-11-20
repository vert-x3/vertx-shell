/*
 * Copyright 2015 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 *
 *
 * Copyright (c) 2015 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *     The Eclipse Public License is available at
 *     http://www.eclipse.org/legal/epl-v10.html
 *
 *     The Apache License v2.0 is available at
 *     http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 *
 */

package io.vertx.ext.shell.term.impl;

import io.termd.core.readline.Keymap;
import io.termd.core.ssh.TtyCommand;
import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.KeyCertOptions;
import io.vertx.core.net.KeyStoreOptionsBase;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.PfxOptions;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.shell.impl.ShellAuth;
import io.vertx.ext.shell.term.SSHTermOptions;
import io.vertx.ext.shell.term.Term;
import io.vertx.ext.shell.term.TermServer;
import org.apache.sshd.common.keyprovider.AbstractKeyPairProvider;
import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.common.session.SessionContext;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.shell.ShellFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.sshd.server.SshServer.DEFAULT_SERVICE_FACTORIES;

/**
 * Encapsulate the SSH server setup.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SSHServer implements TermServer {

  private static final int STATUS_STOPPED = 0, STATUS_STARTING = 1, STATUS_STARTED = 2, STATUS_STOPPING = 3;

  private final Vertx vertx;
  private final SSHTermOptions options;
  private Handler<Term> termHandler;
  private SshServer nativeServer;
  private final AtomicInteger status = new AtomicInteger(STATUS_STOPPED);
  private ContextInternal listenContext;
  private AuthenticationProvider authProvider;
  private Handler<SSHExec> execHandler;

  public SSHServer(Vertx vertx, SSHTermOptions options) {
    this.vertx = vertx;
    this.options = new SSHTermOptions(options);
  }

  public SSHTermOptions getOptions() {
    return options;
  }

  /**
   * @return the underlying native server
   */
  public SshServer getNativeServer() {
    return nativeServer;
  }

  public Handler<SSHExec> getExecHandler() {
    return execHandler;
  }

  public TermServer setExecHandler(Handler<SSHExec> execHandler) {
    this.execHandler = execHandler;
    return this;
  }

  @Override
  public TermServer termHandler(Handler<Term> handler) {
    termHandler = handler;
    return this;
  }

  @Override
  public TermServer authenticationProvider(AuthenticationProvider provider) {
    authProvider = provider;
    return this;
  }

  public SSHServer listen(Handler<AsyncResult<Void>> listenHandler) {
    if (!status.compareAndSet(STATUS_STOPPED, STATUS_STARTING)) {
      listenHandler.handle(Future.failedFuture("Invalid state:" + status.get()));
      return this;
    }
    if (options.getAuthOptions() != null) {
      authProvider = ShellAuth.load(vertx, options.getAuthOptions());
    }
    Charset defaultCharset = Charset.forName(options.getDefaultCharset());
    listenContext = (ContextInternal) vertx.getOrCreateContext();
    vertx.executeBlocking(fut -> {

      try {
        KeyCertOptions ksOptions = options.getKeyPairOptions();
        KeyStore ks;
        if (ksOptions instanceof KeyStoreOptionsBase) {
          ks = ((KeyStoreOptionsBase)ksOptions).loadKeyStore(vertx);
        } else if (ksOptions instanceof PemKeyCertOptions) {
          ks = ((PemKeyCertOptions)ksOptions).loadKeyStore(vertx);
        } else {
          ks = null;
        }
        if (ks == null) {
          throw new VertxException("No key pair store configured");
        }

        String kpPassword = "";
        if (ksOptions instanceof JksOptions) {
          kpPassword = ((JksOptions) ksOptions).getPassword();
        } else if (ksOptions instanceof PfxOptions) {
          kpPassword = ((PfxOptions) ksOptions).getPassword();
        }

        List<KeyPair> keyPairs = new ArrayList<>();
        for (Enumeration<String> it = ks.aliases(); it.hasMoreElements(); ) {
          String alias = it.nextElement();
          Key key = ks.getKey(alias, kpPassword.toCharArray());
          if (key instanceof PrivateKey) {
            Certificate cert = ks.getCertificate(alias);
            PublicKey publicKey = cert.getPublicKey();
            keyPairs.add(new KeyPair(publicKey, (PrivateKey) key));
          }
        }
        KeyPairProvider provider = new AbstractKeyPairProvider() {
          @Override
          public Iterable<KeyPair> loadKeys(SessionContext session) {
            return keyPairs;
          }
        };

        Buffer inputrc = Helper.loadResource(vertx.fileSystem(), options.getIntputrc());
        if (inputrc == null) {
          throw new VertxException("Could not load inputrc from " + options.getIntputrc());
        }
        Keymap keymap = new Keymap(new ByteArrayInputStream(inputrc.getBytes()));
        TermConnectionHandler connectionHandler = new TermConnectionHandler(vertx, keymap, termHandler, listenContext);

        nativeServer = SshServer.setUpDefaultServer();
        nativeServer.setShellFactory(channel -> new TtyCommand(defaultCharset, connectionHandler::handle));
        Handler<SSHExec> execHandler = this.execHandler;
        if (execHandler != null) {
          nativeServer.setCommandFactory((channel, command) -> new TtyCommand(defaultCharset, conn -> listenContext.dispatch(new SSHExec(command, conn), execHandler)));
        }
        nativeServer.setHost(options.getHost());
        nativeServer.setPort(options.getPort());
        nativeServer.setKeyPairProvider(provider);
        nativeServer.setIoServiceFactoryFactory(new org.apache.sshd.netty.NettyIoServiceFactoryFactory(listenContext.nettyEventLoop()));
        nativeServer.setServiceFactories(DEFAULT_SERVICE_FACTORIES);

        //
        if (authProvider == null) {
          throw new VertxException("No authenticator");
        }

        nativeServer.setPasswordAuthenticator((username, userpass, session) -> {
          AsyncAuthException auth = new AsyncAuthException();
          listenContext.runOnContext(v -> {
            authProvider.authenticate(new JsonObject().put("username", username).put("password", userpass), ar -> {
              auth.setAuthed(ar.succeeded());
            });
          });
          throw auth;
        });

        //
        nativeServer.start();
        status.set(STATUS_STARTED);
        fut.complete();
      } catch (Exception e) {
        status.set(STATUS_STOPPED);
        fut.fail(e);
      }
    }, listenHandler);
    return this;
  }

  @Override
  public int actualPort() {
    return nativeServer.getPort();
  }

  public void close(Handler<AsyncResult<Void>> completionHandler) {
    if (!status.compareAndSet(STATUS_STARTED, STATUS_STOPPING)) {
      completionHandler.handle(Future.failedFuture("Invalid state:" + status.get()));
      return;
    }
    vertx.executeBlocking(fut-> {
      try {
        SshServer server = this.nativeServer;
        this.nativeServer = null;
        server.close();
        completionHandler.handle(Future.succeededFuture());
      } catch (Exception t) {
        completionHandler.handle(Future.failedFuture(t));
      } finally {
        status.set(STATUS_STOPPED);
      }
    }, completionHandler);
  }
}
