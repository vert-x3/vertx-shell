package io.vertx.ext.shell.net.impl;

import io.termd.core.ssh.SshTtyConnection;
import io.termd.core.ssh.netty.AsyncAuth;
import io.termd.core.ssh.netty.AsyncUserAuthServiceFactory;
import io.termd.core.ssh.netty.NettyIoServiceFactoryFactory;
import io.termd.core.tty.TtyConnection;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.ContextInternal;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.core.net.KeyCertOptions;
import io.vertx.core.net.PfxOptions;
import io.vertx.core.net.impl.KeyStoreHelper;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.shiro.ShiroAuth;
import io.vertx.ext.shell.auth.ShiroAuthOptions;
import io.vertx.ext.shell.net.SSHOptions;
import org.apache.sshd.common.keyprovider.AbstractKeyPairProvider;
import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.session.ServerConnectionServiceFactory;

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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Encapsulate the SSH server setup.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SSHServer {

  private static final int STATUS_STOPPED = 0, STATUS_STARTING = 1, STATUS_STARTED = 2, STATUS_STOPPING = 3;

  private final Vertx vertx;
  private final SSHOptions options;
  private Consumer<TtyConnection> handler;
  private SshServer nativeServer;
  private final AtomicInteger status = new AtomicInteger(STATUS_STOPPED);
  private ContextInternal listenContext;

  public SSHServer(Vertx vertx, SSHOptions options) {
    this.vertx = vertx;
    this.options = new SSHOptions(options);
  }

  public SSHOptions getOptions() {
    return options;
  }

  public Consumer<TtyConnection> getHandler() {
    return handler;
  }

  public SSHServer setHandler(Consumer<TtyConnection> handler) {
    this.handler = handler;
    return this;
  }

  /**
   * @return the underlying native server
   */
  public SshServer getNativeServer() {
    return nativeServer;
  }

  public void listen(Handler<AsyncResult<Void>> listenHandler) {
    if (!status.compareAndSet(STATUS_STOPPED, STATUS_STARTING)) {
      listenHandler.handle(Future.failedFuture("Invalid state:" + status.get()));
      return;
    }
    listenContext = (ContextInternal) vertx.getOrCreateContext();
    vertx.executeBlocking(fut -> {

      try {
        KeyCertOptions ksOptions = options.getKeyCertOptions();
        KeyStoreHelper ksHelper = KeyStoreHelper.create((VertxInternal) vertx, ksOptions);
        KeyStore ks = ksHelper.loadStore((VertxInternal) vertx);

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
          public Iterable<KeyPair> loadKeys() {
            return keyPairs;
          }
        };

        nativeServer = SshServer.setUpDefaultServer();
        nativeServer.setShellFactory(() -> new SshTtyConnection(handler));
        nativeServer.setHost(options.getHost());
        nativeServer.setPort(options.getPort());
        nativeServer.setKeyPairProvider(provider);
        nativeServer.setIoServiceFactoryFactory(new NettyIoServiceFactoryFactory(listenContext.nettyEventLoop(), new VertxIoHandlerBridge(listenContext)));
        nativeServer.setServiceFactories(Arrays.asList(ServerConnectionServiceFactory.INSTANCE, AsyncUserAuthServiceFactory.INSTANCE));

        //
        AuthProvider authProvider;
        if (options.getAuthOptions() instanceof ShiroAuthOptions) {
          ShiroAuthOptions authOptions = (ShiroAuthOptions) options.getAuthOptions();
          authProvider = ShiroAuth.create(
              vertx,
              authOptions.getType(),
              authOptions.getConfig()
          );
        } else {
          authProvider = null;
        }

        Context context = vertx.getOrCreateContext();
        nativeServer.setPasswordAuthenticator((username, userpass, session) -> {
          if (authProvider != null) {
            AsyncAuth auth = new AsyncAuth();
            context.runOnContext(v -> {
              authProvider.authenticate(new JsonObject().put("username", username).put("password", userpass), ar -> {
                System.out.println("Authenticating with " + username + " " + userpass + " " + ar.succeeded());
                auth.setAuthed(ar.succeeded());
              });
            });
            throw auth;
          }
          return false;
        });

        //
        nativeServer.start();
        status.set(STATUS_STARTED);
        fut.complete(null);
      } catch (Exception e) {
        status.set(STATUS_STOPPED);
        fut.fail(e);
      }
    }, listenHandler);
  }

  public void close(Handler<AsyncResult<Void>> closeHandler) {
    if (!status.compareAndSet(STATUS_STARTED, STATUS_STOPPING)) {
      closeHandler.handle(Future.failedFuture("Invalid state:" + status.get()));
      return;
    }
    vertx.executeBlocking(fut-> {
      try {
        SshServer server = this.nativeServer;
        this.nativeServer = null;
        server.close();
        closeHandler.handle(Future.succeededFuture());
      } catch (Exception t) {
        closeHandler.handle(Future.failedFuture(t));
      } finally {
        status.set(STATUS_STOPPED);
      }
    }, closeHandler);
  }
}
