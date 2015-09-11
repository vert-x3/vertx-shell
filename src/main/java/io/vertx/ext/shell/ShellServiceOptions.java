package io.vertx.ext.shell;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.shell.net.SSHOptions;
import io.vertx.ext.shell.net.TelnetOptions;

import java.util.Collections;
import java.util.List;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class ShellServiceOptions {

  public static final String DEFAULT_WELCOME_MESSAGE = "Welcome to Vert.x Shell\n\n";
  public static final List<ConnectorOptions> DEFAULT_CONNECTORS = Collections.emptyList();

  private String welcomeMessage;
  private TelnetOptions telnet;
  private SSHOptions ssh;

  public ShellServiceOptions() {
    welcomeMessage = DEFAULT_WELCOME_MESSAGE;
  }

  public ShellServiceOptions(ShellServiceOptions that) {
    this.telnet = that.telnet != null ? new TelnetOptions(that.telnet) : null;
    this.welcomeMessage = that.welcomeMessage;
  }

  public ShellServiceOptions(JsonObject json) {
    welcomeMessage = json.getString("welcomeMessage", DEFAULT_WELCOME_MESSAGE);
    telnet = json.getJsonObject("telnet") != null ? new TelnetOptions(json.getJsonObject("telnet")) : null;
    ssh = json.getJsonObject("ssh") != null ? new SSHOptions(json.getJsonObject("ssh")) : null;
  }

  public String getWelcomeMessage() {
    return welcomeMessage;
  }

  public ShellServiceOptions setWelcomeMessage(String welcomeMessage) {
    this.welcomeMessage = welcomeMessage;
    return this;
  }

  public TelnetOptions getTelnet() {
    return telnet;
  }

  public ShellServiceOptions setTelnet(TelnetOptions telnet) {
    this.telnet = telnet;
    return this;
  }

  public SSHOptions getSSH() {
    return ssh;
  }

  public ShellServiceOptions setSSH(SSHOptions ssh) {
    this.ssh = ssh;
    return this;
  }
}
