package io.vertx.ext.shell;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class SSHOptions implements ConnectorOptions {

  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 4000;

  private String host;
  private int port;

  public SSHOptions() {
    host = DEFAULT_HOST;
    port = DEFAULT_PORT;
  }

  public SSHOptions(SSHOptions that) {
    this.host = that.host;
    this.port = that.port;
  }

  public SSHOptions(JsonObject json) {
    this.host = json.getString("host", DEFAULT_HOST);
    this.port = json.getInteger("port", DEFAULT_PORT);
  }

  public String getHost() {
    return host;
  }

  public SSHOptions setHost(String host) {
    this.host = host;
    return this;
  }

  public int getPort() {
    return port;
  }

  public SSHOptions setPort(int port) {
    this.port = port;
    return this;
  }
}
