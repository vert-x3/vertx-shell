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

package examples;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.cli.Argument;
import io.vertx.core.cli.CLI;
import io.vertx.core.cli.CommandLine;
import io.vertx.core.cli.Option;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.auth.jdbc.JDBCAuthOptions;
import io.vertx.ext.auth.mongo.MongoAuthOptions;
import io.vertx.ext.auth.shiro.ShiroAuthOptions;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.shell.ShellServer;
import io.vertx.ext.shell.command.CommandResolver;
import io.vertx.ext.shell.term.HttpTermOptions;
import io.vertx.ext.shell.term.Pty;
import io.vertx.ext.shell.term.Tty;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.shell.system.Shell;
import io.vertx.ext.shell.term.SSHTermOptions;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.term.TelnetTermOptions;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandRegistry;
import io.vertx.ext.shell.term.TermServer;
import io.vertx.ext.web.Router;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class Examples {

  public void deployTelnetService(Vertx vertx) throws Exception {
    vertx.deployVerticle("maven:{maven-groupId}:{maven-artifactId}:{maven-version}",
        new DeploymentOptions().setConfig(
            new JsonObject().put("telnetOptions",
                new JsonObject().
                    put("host", "localhost").
                    put("port", 4000))
        )
    );
  }

  public void deploySSHServiceWithShiro(Vertx vertx) throws Exception {
    vertx.deployVerticle("maven:{maven-groupId}:{maven-artifactId}:{maven-version}",
        new DeploymentOptions().setConfig(new JsonObject().
                put("sshOptions", new JsonObject().
                    put("host", "localhost").
                    put("port", 5000).
                    put("keyPairOptions", new JsonObject().
                        put("path", "src/test/resources/ssh.jks").
                        put("password", "wibble")).
                    put("authOptions", new JsonObject().
                        put("provider", "shiro").
                        put("config", new JsonObject().
                            put("properties_path", "file:/path/to/my/auth.properties"))))
        )
    );
  }

  public void deploySSHServiceWithJDBC(Vertx vertx) throws Exception {
    vertx.deployVerticle("maven:{maven-groupId}:{maven-artifactId}:{maven-version}",
        new DeploymentOptions().setConfig(new JsonObject().
                put("sshOptions", new JsonObject().
                    put("host", "localhost").
                    put("port", 5000).
                    put("keyPairOptions", new JsonObject().
                        put("path", "src/test/resources/ssh.jks").
                        put("password", "wibble")).
                    put("authOptions", new JsonObject().
                        put("provider", "jdbc").
                        put("config", new JsonObject()
                            .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
                            .put("driver_class", "org.hsqldb.jdbcDriver"))))
        )
    );
  }

  public void deploySSHServiceWithMongo(Vertx vertx) throws Exception {
    vertx.deployVerticle("maven:{maven-groupId}:{maven-artifactId}:{maven-version}",
        new DeploymentOptions().setConfig(new JsonObject().
                put("sshOptions", new JsonObject().
                    put("host", "localhost").
                    put("port", 5000).
                    put("keyPairOptions", new JsonObject().
                        put("path", "src/test/resources/ssh.jks").
                        put("password", "wibble")).
                    put("authOptions", new JsonObject().
                        put("provider", "mongo").
                        put("config", new JsonObject().
                            put("connection_string", "mongodb://localhost:27018"))))
        )
    );
  }

  public void deployHttpServiceWithShiro(Vertx vertx) throws Exception {
    vertx.deployVerticle("maven:{maven-groupId}:{maven-artifactId}:{maven-version}",
        new DeploymentOptions().setConfig(new JsonObject().
                put("httpOptions", new JsonObject().
                    put("host", "localhost").
                    put("port", 8080).
                    put("ssl", true).
                    put("keyPairOptions", new JsonObject().
                        put("path", "src/test/resources/server-keystore.jks").
                        put("password", "wibble")).
                    put("authOptions", new JsonObject().
                        put("provider", "shiro").
                        put("config", new JsonObject().
                            put("properties_path", "file:/path/to/my/auth.properties"))))
        )
    );
  }

  public void deployHttpServiceWithJDBC(Vertx vertx) throws Exception {
    vertx.deployVerticle("maven:{maven-groupId}:{maven-artifactId}:{maven-version}",
        new DeploymentOptions().setConfig(new JsonObject().
                put("httpOptions", new JsonObject().
                    put("host", "localhost").
                    put("port", 8080).
                    put("ssl", true).
                    put("keyPairOptions", new JsonObject().
                        put("path", "src/test/resources/server-keystore.jks").
                        put("password", "wibble")).
                    put("authOptions", new JsonObject().
                        put("provider", "jdbc").
                        put("config", new JsonObject()
                            .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
                            .put("driver_class", "org.hsqldb.jdbcDriver"))))
        )
    );
  }

  public void deployHttpServiceWithMongo(Vertx vertx) throws Exception {
    vertx.deployVerticle("maven:{maven-groupId}:{maven-artifactId}:{maven-version}",
        new DeploymentOptions().setConfig(new JsonObject().
                put("httpOptions", new JsonObject().
                    put("host", "localhost").
                    put("port", 8080).
                    put("ssl", true).
                    put("keyPairOptions", new JsonObject().
                        put("path", "src/test/resources/server-keystore.jks").
                        put("password", "wibble")).
                    put("authOptions", new JsonObject().
                        put("provider", "mongo").
                        put("config", new JsonObject().
                            put("connection_string", "mongodb://localhost:27018"))))
        )
    );
  }

  public void runTelnetService(Vertx vertx) throws Exception {
    ShellService service = ShellService.create(vertx,
        new ShellServiceOptions().setTelnetOptions(
            new TelnetTermOptions().
                setHost("localhost").
                setPort(4000)
        )
    );
    service.start();
  }

  public void runSSHServiceWithShiro(Vertx vertx) throws Exception {
    ShellService service = ShellService.create(vertx,
        new ShellServiceOptions().setSSHOptions(
            new SSHTermOptions().
                setHost("localhost").
                setPort(5000).
                setKeyPairOptions(new JksOptions().
                        setPath("server-keystore.jks").
                        setPassword("wibble")
                ).
                setAuthOptions(new ShiroAuthOptions().
                        setType(ShiroAuthRealmType.PROPERTIES).
                        setConfig(new JsonObject().
                            put("properties_path", "file:/path/to/my/auth.properties"))
                )
        )
    );
    service.start();
  }

  public void runSSHServiceWithMongo(Vertx vertx) throws Exception {
    ShellService service = ShellService.create(vertx,
        new ShellServiceOptions().setSSHOptions(
            new SSHTermOptions().
                setHost("localhost").
                setPort(5000).
                setKeyPairOptions(new JksOptions().
                        setPath("server-keystore.jks").
                        setPassword("wibble")
                ).
                setAuthOptions(new MongoAuthOptions().setConfig(new JsonObject().
                        put("connection_string", "mongodb://localhost:27018"))
                )
        )
    );
    service.start();
  }

  public void runSSHServiceWithJDBC(Vertx vertx) throws Exception {
    ShellService service = ShellService.create(vertx,
        new ShellServiceOptions().setSSHOptions(
            new SSHTermOptions().
                setHost("localhost").
                setPort(5000).
                setKeyPairOptions(new JksOptions().
                        setPath("server-keystore.jks").
                        setPassword("wibble")
                ).
                setAuthOptions(new JDBCAuthOptions().setConfig(new JsonObject()
                        .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
                        .put("driver_class", "org.hsqldb.jdbcDriver"))
                )
        )
    );
    service.start();
  }

  public void runHttpService(Vertx vertx) throws Exception {
    ShellService service = ShellService.create(vertx,
        new ShellServiceOptions().setHttpOptions(
            new HttpTermOptions().
                setHost("localhost").
                setPort(8080)
        )
    );
    service.start();
  }

  public void runHTTPServiceWithMongo(Vertx vertx) throws Exception {
    ShellService service = ShellService.create(vertx,
        new ShellServiceOptions().setHttpOptions(
            new HttpTermOptions().
                setHost("localhost").
                setPort(8080).
                setAuthOptions(new MongoAuthOptions().setConfig(new JsonObject().
                        put("connection_string", "mongodb://localhost:27018"))
                )
        )
    );
    service.start();
  }

  public void runHTTPServiceWithJDBC(Vertx vertx) throws Exception {
    ShellService service = ShellService.create(vertx,
        new ShellServiceOptions().setHttpOptions(
            new HttpTermOptions().
                setHost("localhost").
                setPort(8080).
                setAuthOptions(new JDBCAuthOptions().setConfig(new JsonObject()
                        .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
                        .put("driver_class", "org.hsqldb.jdbcDriver"))
                )
        )
    );
    service.start();
  }

  public void shellServer(Vertx vertx, Router router) {

    ShellServer server = ShellServer.create(vertx); // <1>

    Router shellRouter = Router.router(vertx); // <2>
    router.mountSubRouter("/shell", shellRouter);
    TermServer httpTermServer = TermServer.createHttpTermServer(vertx, router);

    TermServer sshTermServer = TermServer.createSSHTermServer(vertx); // <3>

    server.registerTermServer(httpTermServer); // <4>
    server.registerTermServer(sshTermServer);

    server.registerCommandResolver(CommandResolver.baseCommands(vertx)); // <5>

    server.listen(); // <6>
  }

  public void helloWorld(Vertx vertx) {

    CommandBuilder builder = CommandBuilder.command("my-command");
    builder.processHandler(process -> {

      // Write a message to the console
      process.write("Hello World");

      // End the process
      process.end();
    });

    // Register the command
    CommandRegistry registry = CommandRegistry.getShared(vertx);
    registry.registerCommand(builder.build(vertx));
  }

  public void cliCommand() {
    CLI cli = CLI.create("my-command").
        addArgument(new Argument().setArgName("my-arg")).
        addOption(new Option().setShortName("m").setLongName("my-option"));
    CommandBuilder command = CommandBuilder.command(cli);
    command.processHandler(process -> {

      CommandLine commandLine = process.commandLine();

      String argValue = commandLine.getArgumentValue(0);
      String optValue = commandLine.getOptionValue("my-option");
      process.write("The argument is " + argValue + " and the option is " + optValue);

      process.end();
    });
  }

  public void cliCommandWithHelp() {
    CLI cli = CLI.create("my-command").
        addArgument(new Argument().setArgName("my-arg")).
        addOption(new Option().setArgName("help").setShortName("h").setLongName("help"));
    CommandBuilder command = CommandBuilder.command(cli);
    command.processHandler(process -> {
      // ...
    });
  }

  public void commandArgs(CommandBuilder command) {
    command.processHandler(process -> {

      for (String arg : process.args()) {
        // Print each argument on the console
        process.write("Argument " + arg);
      }

      process.end();
    });
  }

  public void session(CommandBuilder command) {
    command.processHandler(process -> {

      Session session = process.session();

      if (session.get("my_key") == null) {
        session.put("my key", "my value");
      }

      process.end();
    });
  }


  public void readStdin(Tty tty) {
    tty.setStdin(data -> {
      System.out.println("Received " + data);
    });
  }

  public void writeStdout(Tty tty) {
    tty.stdout().write("Hello World");
  }

  public void terminalSize(Tty tty) {
    tty.stdout().write("Current terminal size: (" + tty.width() + ", " + tty.height() + ")");
  }

  public void resizeHandlerTerminal(Tty tty) {
    tty.resizehandler(v -> {
      System.out.println("terminal resized : " + tty.width() + " " + tty.height());
    });
  }

  public void terminalType(Tty tty) {
    System.out.println("terminal type : " + tty.type());
  }

  public void asyncCommand(CommandBuilder command) {
    command.processHandler(process -> {
      Vertx vertx = process.vertx();

      // Set a timer
      vertx.setTimer(1000, id -> {

        // End the command when the timer is fired
        process.end();
      });
    });
  }

  public void interruptHandler(CommandBuilder command) {
    command.processHandler(process -> {
      Vertx vertx = process.vertx();

      // Every second print a message on the console
      long periodicId = vertx.setPeriodic(1000, id -> {
        process.write("tick\n");
      });

      // When user press Ctrl+C: cancel the timer and end the process
      process.interruptHandler(v -> {
        vertx.cancelTimer(periodicId);
        process.end();
      });
    });
  }

  public void suspendResumeHandler(CommandBuilder command) {
    command.processHandler(process -> {

      // Command is suspended
      process.suspendHandler(v -> {
        System.out.println("Suspended");
      });

      // Command is resumed
      process.resumeHandler(v -> {
        System.out.println("Resumed");
      });
    });
  }

  public void endHandler(CommandBuilder command) {
    command.processHandler(process -> {

      // Command terminates
      process.endHandler(v -> {
        System.out.println("Terminated");
      });
    });
  }

  public void telnetEchoTerminal(Vertx vertx) {
    TermServer server = TermServer.createTelnetTermServer(vertx, new TelnetTermOptions().setPort(5000).setHost("localhost"));
    server.termHandler(term -> {
      term.setStdin(line -> {
        term.stdout().write(line);
      });
    });
    server.listen();
  }

  public void sshEchoTerminal(Vertx vertx) {
    TermServer server = TermServer.createSSHTermServer(vertx, new SSHTermOptions().setPort(5000).setHost("localhost"));
    server.termHandler(term -> {
      term.setStdin(line -> {
        term.stdout().write(line);
      });
    });
    server.listen();
  }

  public void httpEchoTerminal(Vertx vertx) {
    TermServer server = TermServer.createHttpTermServer(vertx, new HttpTermOptions().setPort(5000).setHost("localhost"));
    server.termHandler(term -> {
      term.setStdin(line -> {
        term.stdout().write(line);
      });
    });
    server.listen();
  }

  public void httpEchoTerminalUsingRouter(Vertx vertx, Router router) {
    TermServer server = TermServer.createHttpTermServer(vertx, router, new HttpTermOptions().setPort(5000).setHost("localhost"));
    server.termHandler(term -> {
      term.setStdin(line -> {
        term.stdout().write(line);
      });
    });
    server.listen();
  }

  public void creatingShell(ShellServer shellServer) {

    // Create a shell ession
    Shell shell = shellServer.createShell();

  }

  public void runningShellCommand(ShellServer shellServer) {

    // Create a shell
    Shell shell = shellServer.createShell();

    // Create a job fo the command
    Job job = shell.createJob("my-command 1234");

    // Create a pseudo terminal
    Pty pty = Pty.create();
    pty.setStdout(data -> {
      System.out.println("Command wrote " + data);
    });

    // Run the command
    job.setTty(pty.slave());
    job.statusUpdateHandler(status -> {
      System.out.println("Command terminated with status " + status);
    });
  }
}
