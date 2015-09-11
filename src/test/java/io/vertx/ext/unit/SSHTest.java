package io.vertx.ext.unit;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.shell.SSHOptions;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.auth.ShiroAuthOptions;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.InputStream;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class SSHTest {

  ShellService service;
  Vertx vertx;

  @Before
  public void before() {
    vertx = Vertx.vertx();
  }

  @After
  public void after() {
    if (service != null) {
      service.close();
    }
    vertx.close();
  }

  private void startShell() throws Exception {

    if (service != null) {
      throw new IllegalStateException();
    }

    service = ShellService.create(vertx, new ShellServiceOptions().
        setWelcomeMessage("").
        setSSH(new SSHOptions().setPort(5000).setHost("localhost").setKeyStoreOptions(
            new JksOptions().setPath("src/test/resources/server-keystore.jks").setPassword("wibble")).
            setShiroAuthOptions(new ShiroAuthOptions().setType(ShiroAuthRealmType.PROPERTIES).setConfig(
                new JsonObject().put("properties_path", "classpath:test-auth.properties")))));


    service.start();
  }

  private Session createSession(String username, String password) throws Exception {
    JSch jsch= new JSch();
    Session session = jsch.getSession(username, "localhost", 5000);
    session.setPassword(password);
    session.setUserInfo(new UserInfo() {
      @Override
      public String getPassphrase() {
        return null;
      }

      @Override
      public String getPassword() {
        return null;
      }

      @Override
      public boolean promptPassword(String s) {
        return false;
      }

      @Override
      public boolean promptPassphrase(String s) {
        return false;
      }

      @Override
      public boolean promptYesNo(String s) {
        // Accept all server keys
        return true;
      }

      @Override
      public void showMessage(String s) {

      }
    });
    return session;
  }

  @Test
  public void testAuthenticate() throws Exception {
    startShell();
    Session session = createSession("paulo", "secret");
    session.connect();
    Channel channel = session.openChannel("shell");
    channel.connect();
    InputStream in = channel.getInputStream();
    byte[] out = new byte[2];
    assertEquals(2, in.read(out));
    assertEquals("% ", new String(out));
    channel.disconnect();
    session.disconnect();
  }

  @Test
  public void testAuthenticationFail() throws Exception {
    startShell();
    Session session = createSession("paulo", "secret_");
    try {
      session.connect();
      fail();
    } catch (JSchException e) {
      assertEquals("Auth cancel", e.getMessage());
    }
  }
}
