package io.vertx.ext.shell.impl;

import io.termd.core.ssh.SshTtyConnection;
import io.termd.core.tty.TtyConnection;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.net.impl.KeyStoreHelper;
import io.vertx.ext.shell.SSHOptions;
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
import java.util.function.Consumer;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SSHConnector {

  final SSHOptions options;

  public SSHConnector(SSHOptions options) {
    this.options = new SSHOptions(options);
  }

  public void start(VertxInternal vertx, Consumer<TtyConnection> handler) throws Exception {

    KeyStoreHelper keyStoreHelper = KeyStoreHelper.create(vertx, options.getKeyCertOptions());

    // Make this possible in KeyStoreHelper
    Field passwordField = KeyStoreHelper.class.getDeclaredField("password");
    passwordField.setAccessible(true);
    String password = (String) passwordField.get(keyStoreHelper);
    Method loadStore = KeyStoreHelper.class.getDeclaredMethod("loadStore", VertxInternal.class, String.class);
    loadStore.setAccessible(true);
    KeyStore ks = (KeyStore) loadStore.invoke(keyStoreHelper, vertx, password);

    List<KeyPair> keyPairs = new ArrayList<>();
    for (Enumeration<String> it = ks.aliases();it.hasMoreElements();) {
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

    SshServer sshd = SshServer.setUpDefaultServer();
    sshd.setShellFactory(() -> new SshTtyConnection(handler));
    sshd.setHost(options.getHost());
    sshd.setPort(options.getPort());
    sshd.setKeyPairProvider(provider);

    sshd.setPasswordAuthenticator((username, password_, session) -> true);
    sshd.start();
  }
}
