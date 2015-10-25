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

/**
 * = Vert.x Shell
 *
 * Vert.x Shell is a command line interface for the Vert.x runtime available from regular
 * terminals using different protocols.
 *
 * Vert.x Shell provides a variety of commands for interacting live with Vert.x services.
 *
 * Vert.x Shell can be extended with custom commands in any language supported by Vert.x
 *
 * == Using Vert.x Shell
 *
 * Vert.x Shell is a Vert.x Service and can be started programmatically via the {@link io.vertx.ext.shell.ShellService}
 * or deployed as a service.
 *
 * === Deployed service
 *
 * The shell can be started as a service directly either from the command line or as a the Vert.x deployment:
 *
 * .Starting a shell service available via Telnet
 * [source,subs="+attributes"]
 * ----
 * vertx run -conf '{"telnetOptions":{"port":5000}}' maven:${maven.groupId}:${maven.artifactId}:${maven.version}
 * ----
 *
 * or
 *
 * .Starting a shell service available via SSH
 * [source,subs="+attributes"]
 * ----
 * # create a key pair for the SSH server
 * keytool -genkey -keyalg RSA -keystore ssh.jks -keysize 2048 -validity 1095 -dname CN=localhost -keypass secret -storepass secret
 * # create the auth config
 * echo user.admin=password > auth.properties
 * # start the shell
 * vertx run -conf '{"sshOptions":{"port":4000,"keyPairOptions":{"path":"ssh.jks","password":"secret"},"shiroAuthOptions":{"config":{"properties_path":"file:auth.properties"}}}}' maven:${maven.groupId}:${maven.artifactId}:${maven.version}
 * ----
 *
 * You can also deploy this service inside your own verticle:
 *
 * [source,$lang,subs="+attributes"]
 * ----
 * {@link examples.Examples#deployTelnetService(io.vertx.core.Vertx)}
 * ----
 *
 * or
 *
 * [source,$lang,subs="+attributes"]
 * ----
 * {@link examples.Examples#deploySSHService(io.vertx.core.Vertx)}
 * ----
 *
 * NOTE: when Vert.x Shell is already on your classpath you can use `service:io.vertx.ext.shell` instead
 * or `maven:${maven.groupId}:${maven.artifactId}:${maven.version}`
 *
 * === Programmatic service
 *
 * The {@link io.vertx.ext.shell.ShellService} takes care of starting an instance of Vert.x Shell.
 *
 * Starting a shell service available via SSH:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#runSSHService(io.vertx.core.Vertx)}
 * ----
 *
 * Starting a shell service available via Telnet:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#runTelnetService}
 * ----
 *
 * The {@link io.vertx.ext.shell.term.TelnetOptions} extends the Vert.x Core `NetServerOptions` as the Telnet server
 * implementation is based on a `NetServer`.
 *
 * CAUTION: Telnet does not provide any authentication nor encryption at all.
 *
 * == Telnet configuration
 *
 * The telnet connector is configured by {@link io.vertx.ext.shell.ShellServiceOptions#setTelnetOptions},
 * the {@link io.vertx.ext.shell.term.TelnetOptions} extends the {@link io.vertx.core.net.NetServerOptions} so they
 * have the exact same configuration.
 *
 * == SSH configuration
 *
 * The SSH connector is configured by {@link io.vertx.ext.shell.ShellServiceOptions#setSSHOptions}:
 *
 * - {@link io.vertx.ext.shell.term.SSHOptions#setPort}: port
 * - {@link io.vertx.ext.shell.term.SSHOptions#setHost}: host
 *
 * Only username/password authentication is supported at the moment, it can be configured with property file
 * or LDAP, see Vert.x Auth for more info:
 *
 * - {@link io.vertx.ext.shell.term.SSHOptions#setShiroAuthOptions}: configures user authentication
 *
 * The server key configuration reuses the key pair store configuration scheme provided by _Vert.x Core_:
 *
 * - {@link io.vertx.ext.shell.term.SSHOptions#setKeyPairOptions}: set `.jks` key pair store
 * - {@link io.vertx.ext.shell.term.SSHOptions#setPfxKeyPairOptions}: set `.pfx` key pair store
 * - {@link io.vertx.ext.shell.term.SSHOptions#setPemKeyPairOptions}: set `.pem` key pair store
 *
 * == Base commands
 *
 * To find out the available commands you can use the _help_ builtin command:
 *
 * . Verticle commands
 * .. verticle-ls: list all deployed verticles
 * .. verticle-undeploy: undeploy a verticle
 * .. verticle-deploy: deployes a verticle
 * .. verticle-factories: list all known verticle factories
 * . File system commands
 * .. ls
 * .. cd
 * .. pwd
 * . Bus commands
 * .. bus-tail: display all incoming messages on an event bus address
 * .. bus-send: send a message on the event bus
 * . Net commands
 * .. net-ls: list all available net servers, including HTTP servers
 * . Shared data commands
 * .. local-map-put
 * .. local-map-get
 * .. local-map-rm
 * . Metrics commands (requires Dropwizard metrics setup)
 * .. metrics-ls: show all available metrics
 * .. metrics-info: show particular metrics
 * . Various commands
 * .. echo
 * .. sleep
 * .. help
 * .. exit
 * .. logout
 * . Job control
 * .. fg
 * .. bg
 * .. jobs
 *
 * NOTE: this command list should evolve in next releases of Vert.x Shell
 *
 * == Extending Vert.x Shell
 *
 * Vert.x Shell can be extended with custom commands in any of the languages supporting code generation.
 *
 * A command is created by the {@link io.vertx.ext.shell.command.CommandBuilder#command} method: the command process handler is called
 * by the shell when the command is executed, this handler can be set with the {@link io.vertx.ext.shell.command.CommandBuilder#processHandler}
 * method:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#helloWorld}
 * ----
 *
 * After a command is created, it needs to be registed to a {@link io.vertx.ext.shell.registry.CommandRegistry}. The
 * command registry holds all the commands for a Vert.x instance.
 *
 * A command is registered until it is unregistered with the {@link io.vertx.ext.shell.registry.CommandRegistration#unregister()}
 * method or the {@link io.vertx.ext.shell.registry.CommandRegistry#unregisterCommand(java.lang.String)}. When a command is
 * registered from a Verticle, this command is unregistered when this verticle is undeployed.
 *
 * NOTE: Command callbacks are invoked in the {@literal io.vertx.core.Context} when the command is registered in the
 * registry. Keep this in mind if you maintain state in a command.
 *
 * The {@link io.vertx.ext.shell.command.CommandProcess} object can be used for interacting with the shell.
 *
 * === Command arguments
 *
 * The {@link io.vertx.ext.shell.command.CommandProcess#args()} returns the command arguments:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#commandArgs}
 * ----
 *
 * Besides it is also possible to create commands using {@link io.vertx.core.cli.CLI Vert.x CLI}: it makes easier to
 * write command line argument parsing:
 *
 * - _option_ and _argument_ parsing
 * - argument _validation_
 * - generation of the command _usage_
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#cliCommand()}
 * ----
 *
 * When an option named _help_ is added to the CLI object, the shell will take care of generating the command usage
 * when the option is activated:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#cliCommandWithHelp()}
 * ----
 *
 * When the command executes the {@link io.vertx.ext.shell.command.CommandProcess process} is provided for interacting
 * with the shell. A {@link io.vertx.ext.shell.command.CommandProcess} extends {@link io.vertx.ext.shell.io.Tty}
 * which is used for interacting with the terminal.
 *
 * === Terminal usage
 *
 * ==== terminal I/O
 *
 * The {@link io.vertx.ext.shell.io.Tty#setStdin} handler is used to be notified when the terminal
 * receives data, e.g the user uses his keyboard:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#readStdin}
 * ----
 *
 * A command can use the {@link io.vertx.ext.shell.io.Tty#stdout()} to write to the standard output.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#writeStdout}
 * ----
 *
 * ==== Terminal size
 *
 * The current terminal size can be obtained using {@link io.vertx.ext.shell.io.Tty#width()} and
 * {@link io.vertx.ext.shell.io.Tty#height()}.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#terminalSize}
 * ----
 *
 * ==== Resize event
 *
 * When the size of the terminal changes the {@link io.vertx.ext.shell.io.Tty#resizehandler(io.vertx.core.Handler)}
 * is called, the new terminal size can be obtained with {@link io.vertx.ext.shell.io.Tty#width()} and
 * {@link io.vertx.ext.shell.io.Tty#height()}.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#resizeHandlerTerminal}
 * ----
 *
 * ==== Terminal type
 *
 * The terminal type is useful for sending escape codes to the remote terminal: {@link io.vertx.ext.shell.io.Tty#type()}
 * returns the current terminal type, it can be null if the terminal has not advertised the value.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#terminalType}
 * ----
 *
 * === Shell session
 *
 * The shell is a connected service that naturally maintains a session with the client, this session can be
 * used in commands to scope data. A command can get the session with {@link io.vertx.ext.shell.process.ProcessContext#session()}:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#session}
 * ----
 *
 * === Process termination
 *
 * Calling {@link io.vertx.ext.shell.command.CommandProcess#end()} ends the current process. It can be called directly
 * in the invocation of the command handler or any time later:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#asyncCommand}
 * ----
 *
 * === Process events
 *
 * A command can subscribe to a few process events.
 *
 * ==== Interrupted event
 *
 * The {@link io.vertx.ext.shell.command.CommandProcess#interruptHandler(io.vertx.core.Handler)} is called when the process is interrupted, this event is fired when the user press
 * _Ctrl+C_ during the execution of a command. This handler can be used for interrupting commands _blocking_ the CLI and
 * gracefully ending the command process:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#interruptHandler}
 * ----
 *
 * When no interrupt handler is registered, pressing _Ctrl+C_ will have no effect on the current process and the event
 * will be delayed and will likely be handled by the shell, like printing a new line on the console.
 *
 * ==== Suspend/resume events
 *
 * The {@link io.vertx.ext.shell.command.CommandProcess#suspendHandler(io.vertx.core.Handler)} is called when the process
 * is running and the user press _Ctrl+Z_, the command is _suspended_:
 *
 * - the command can receive the suspend event when it has registered an handler for this event
 * - the command will not receive anymore data from the standard input
 * - the shell prompt the user for input
 *
 * The {@link io.vertx.ext.shell.command.CommandProcess#resumeHandler(io.vertx.core.Handler)} is called when the process
 * is resumed, usually when the user types _fg_:
 *
 * - the command can receive the resume event when it has registered an handler for this event
 * - the command will receive anymore data from the standard input when it has registered an stdin handler
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#suspendResumeHandler}
 * ----
 *
 * === Command completion
 *
 * A command can provide a completion handler when it wants to provide contextual command line interface completion.
 *
 * Like the process handler, the {@link io.vertx.ext.shell.command.CommandBuilder#completionHandler(io.vertx.core.Handler) completion
 * handler} is non blocking because the implementation may use Vert.x services, e.g the file system.
 *
 * The {@link io.vertx.ext.shell.cli.Completion#lineTokens()} returns a list of {@link io.vertx.ext.shell.cli.CliToken tokens}
 * from the beginning of the line to the cursor position. The list can be empty if the cursor when the cursor is at the
 * beginning of the line.
 *
 * The {@link io.vertx.ext.shell.cli.Completion#rawLine()} returns the current completed from the beginning
 * of the line to the cursor position, in raw format, i.e without any char escape performed.
 *
 * Completion ends with a call to {@link io.vertx.ext.shell.cli.Completion#complete(java.util.List)}.
 *
 * == Terminal servers
 *
 * Vert.x Shell also provides bare terminal servers for those who need to write pure terminal applications.
 *
 * The terminal server {@link io.vertx.ext.shell.term.Term} handler accepts incoming terminal connections.
 * When a remote terminal connects, the {@link io.vertx.ext.shell.term.Term} can be used to interact with connected
 * terminal.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#sshEchoTerminal(io.vertx.core.Vertx)}
 * ----
 *
 * The telnet protocol is also supported:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#telnetEchoTerminal(io.vertx.core.Vertx)}
 * ----
 *
 * The {@link io.vertx.ext.shell.term.Term} is also a {@link io.vertx.ext.shell.io.Tty}, this section explains
 * how to use the tty.
 */
@ModuleGen(name = "vertx-shell", groupPackage = "io.vertx")
@Document(fileName = "index.adoc")
package io.vertx.ext.shell;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
