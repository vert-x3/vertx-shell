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
import io.vertx.ext.auth.shiro.ShiroAuthOptions;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.shell.term.Pty;
import io.vertx.ext.shell.term.Tty;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.shell.system.Shell;
import io.vertx.ext.shell.term.SSHOptions;
import io.vertx.ext.shell.session.Session;
import io.vertx.ext.shell.ShellService;
import io.vertx.ext.shell.ShellServiceOptions;
import io.vertx.ext.shell.term.TelnetOptions;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.registry.CommandRegistry;
import io.vertx.ext.shell.term.TermServer;

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

  public void deploySSHService(Vertx vertx) throws Exception {
    vertx.deployVerticle("maven:{maven-groupId}:{maven-artifactId}:{maven-version}",
        new DeploymentOptions().setConfig(
            new JsonObject().put("sshOptions",
                new JsonObject().
                    put("host", "localhost").
                    put("port", 5000).
                    put("keyPairOptions",
                        new JsonObject().
                            put("path", "server-keystore.jks").
                            put("password", "wibble")).
                    put("config", new JsonObject().
                        put("properties_path", "file:/path/to/my/auth.properties")))
        )
    );
  }

  public void runTelnetService(Vertx vertx) throws Exception {
    ShellService service = ShellService.create(vertx,
        new ShellServiceOptions().setTelnetOptions(
            new TelnetOptions().
                setHost("localhost").
                setPort(4000)
        )
    );
    service.start();
  }

  public void runSSHService(Vertx vertx) throws Exception {
    ShellService service = ShellService.create(vertx,
        new ShellServiceOptions().setSSHOptions(
            new SSHOptions().
                setHost("localhost").
                setPort(5000).
                setKeyPairOptions(new JksOptions().
                        setPath("server-keystore.jks").
                        setPassword("wibble")
                ).
                setShiroAuthOptions(new ShiroAuthOptions().
                        setType(ShiroAuthRealmType.PROPERTIES).
                        setConfig(new JsonObject().
                            put("properties_path", "file:/path/to/my/auth.properties"))
                )
        )
    );
    service.start();
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
    CommandRegistry registry = CommandRegistry.get(vertx);
    registry.registerCommand(builder.build());
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

  public void telnetEchoTerminal(Vertx vertx) {
    TermServer server = TermServer.createTelnetServer(vertx, new TelnetOptions().setPort(5000).setHost("localhost"));
    server.termHandler(term -> {
      term.setStdin(line -> {
        term.stdout().write(line);
      });
    });
    server.listen();
  }

  public void sshEchoTerminal(Vertx vertx) {
    TermServer server = TermServer.createSSHServer(vertx, new SSHOptions().setPort(5000).setHost("localhost"));
    server.termHandler(term -> {
      term.setStdin(line -> {
        term.stdout().write(line);
      });
    });
    server.listen();
  }

  public void creatingShell(ShellService shellService) {

    // Create a shell ession
    Shell shell = shellService.createShell();

  }

  public void runningShellCommand(ShellService shellService) {

    // Create a shell
    Shell shell = shellService.createShell();

    // Create a job fo the command
    Job job = shell.createJob("my-command 1234");

    // Create a pseudo terminal
    Pty pty = Pty.create();
    pty.setStdout(data -> {
      System.out.println("Command wrote " + data);
    });

    // Run the command
    job.setTty(pty.slave());
    job.run(status -> {
      System.out.println("Command terminated with status " + status);
    });
  }
}
