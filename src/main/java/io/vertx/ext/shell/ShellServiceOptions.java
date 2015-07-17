package io.vertx.ext.shell;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@DataObject
public class ShellServiceOptions {

  public static final String DEFAULT_WELCOME_MESSAGE = "Welcome to Vert.x Shell\r\n\r\n";
  public static final List<ConnectorOptions> DEFAULT_CONNECTORS = Collections.emptyList();

  private List<ConnectorOptions> connectors;
  private String welcomeMessage;

  public ShellServiceOptions() {
    connectors = new ArrayList<>(DEFAULT_CONNECTORS);
    welcomeMessage = DEFAULT_WELCOME_MESSAGE;
  }

  public ShellServiceOptions(ShellServiceOptions that) {
    this.connectors = new ArrayList<>(that.connectors);
    this.welcomeMessage = that.welcomeMessage;
  }

  public ShellServiceOptions(JsonObject json) {
    welcomeMessage = json.getString("welcomeMessage", DEFAULT_WELCOME_MESSAGE);
    connectors = json.getJsonArray("connectors", new JsonArray()).stream().map(obj -> (JsonObject)obj).map(TelnetOptions::new).collect(Collectors.toList());
  }

  public List<ConnectorOptions> getConnectors() {
    return connectors;
  }

  public ShellServiceOptions addConnector(ConnectorOptions connector) {
    connectors.add(connector);
    return this;
  }

  public String getWelcomeMessage() {
    return welcomeMessage;
  }

  public ShellServiceOptions setWelcomeMessage(String welcomeMessage) {
    this.welcomeMessage = welcomeMessage;
    return this;
  }
}
