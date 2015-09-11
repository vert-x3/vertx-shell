package io.vertx.ext.shell.impl;

import io.termd.core.ssh.SshTtyConnection;
import io.termd.core.tty.TtyConnection;
import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.impl.KeyStoreHelper;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.shiro.ShiroAuth;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.shell.SSHOptions;
import io.vertx.ext.shell.auth.ShiroAuthOptions;
import org.apache.sshd.common.keyprovider.AbstractKeyPairProvider;
import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.server.SshServer;

import java.io.IOException;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Encapsulate the SSH server setup.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SSHServer {

  private final SSHOptions options;
  private Consumer<TtyConnection> handler;
  private SshServer sshd;

  public SSHServer(SSHOptions options) {
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

  public void listen(Vertx vertx) throws Exception {

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
    sshd.start();
  }

  public void close() {
    if (sshd != null && sshd.isOpen()) {
      try {
        sshd.close();
      } catch (IOException ignore) {
      }
    }
  }
}
