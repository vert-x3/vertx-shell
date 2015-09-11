package io.vertx.ext.shell.net.impl;

import io.termd.core.ssh.SshTtyConnection;
import io.termd.core.tty.TtyConnection;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.impl.KeyStoreHelper;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.shiro.ShiroAuth;
import io.vertx.ext.shell.auth.ShiroAuthOptions;
import io.vertx.ext.shell.net.SSHOptions;
import org.apache.sshd.common.keyprovider.AbstractKeyPairProvider;
import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.server.SshServer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
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
  private SshServer sshd;
  private final AtomicInteger status = new AtomicInteger(STATUS_STOPPED);

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

  public void listen(Handler<AsyncResult<Void>> listenHandler) {
    if (!status.compareAndSet(STATUS_STOPPED, STATUS_STARTING)) {
      listenHandler.handle(Future.failedFuture("Invalid state:" + status.get()));
      return;
    }
    vertx.executeBlocking(fut -> {

      try {
        KeyStoreHelper keyStoreHelper = KeyStoreHelper.create((VertxInternal) vertx, options.getKeyCertOptions());

        // Make this possible in KeyStoreHelper
        Field passwordField = KeyStoreHelper.class.getDeclaredField("password");
        passwordField.setAccessible(true);
        String password = (String) passwordField.get(keyStoreHelper);
        Method loadStore = KeyStoreHelper.class.getDeclaredMethod("loadStore", VertxInternal.class, String.class);
        loadStore.setAccessible(true);
        KeyStore ks = (KeyStore) loadStore.invoke(keyStoreHelper, vertx, password);

        List<KeyPair> keyPairs = new ArrayList<>();
        for (Enumeration<String> it = ks.aliases(); it.hasMoreElements(); ) {
          String alias = it.nextElement();
          Key key = ks.getKey(alias, password.toCharArray());
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

        sshd = SshServer.setUpDefaultServer();
        sshd.setShellFactory(() -> new SshTtyConnection(handler));
        sshd.setHost(options.getHost());
        sshd.setPort(options.getPort());
        sshd.setKeyPairProvider(provider);

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
        sshd.setPasswordAuthenticator((username, userpass, session) -> {
          CountDownLatch latch = new CountDownLatch(1);
          AtomicReference<AsyncResult<User>> ref = new AtomicReference<>();
          // That's not a Vert.x thread here
          context.runOnContext(v -> {
            if (authProvider != null) {
              authProvider.authenticate(new JsonObject().put("username", username).put("password", userpass), ar -> {
                ref.set(ar);
                latch.countDown();
              });
            }
          });
          try {
            latch.await();
            AsyncResult<User> ar = ref.get();
            if (ar.succeeded()) {
              return true;
            }
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
          return false;
        });

        //
        sshd.start();
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
        SshServer server = sshd;
        sshd = null;
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
