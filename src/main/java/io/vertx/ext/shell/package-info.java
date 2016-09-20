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
 * === Shell service
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
 * vertx run -conf '{"sshOptions":{"port":4000,"keyPairOptions":{"path":"ssh.jks","password":"secret"},"authOptions":{"provider":"shiro","config":{"properties_path":"file:auth.properties"}}}}' maven:${maven.groupId}:${maven.artifactId}:${maven.version}
 * ----
 *
 * or
 *
 * .Starting a shell service available via HTTP
 * [source,subs="+attributes"]
 * ----
 * # create a certificate for the HTTP server
 * keytool -genkey -keyalg RSA -keystore keystore.jks -keysize 2048 -validity 1095 -dname CN=localhost -keypass secret -storepass secret
 * # create the auth config
 * echo user.admin=password > auth.properties
 * vertx run -conf '{"httpOptions":{"port":8080,"ssl":true,"keyStoreOptions":{"path":"keystore.jks","password":"secret"},"authOptions":{"provider":""shiro,"config":{"properties_path":"file:auth.properties"}}}}' maven:${maven.groupId}:${maven.artifactId}:${maven.version}
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
 * {@link examples.Examples#deploySSHServiceWithShiro(io.vertx.core.Vertx)}
 * ----
 *
 * or
 *
 * [source,$lang,subs="+attributes"]
 * ----
 * {@link examples.Examples#deployHttpServiceWithShiro(io.vertx.core.Vertx)}
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
 * {@link examples.Examples#runSSHServiceWithShiro(io.vertx.core.Vertx)}
 * ----
 *
 * Starting a shell service available via Telnet:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#runTelnetService}
 * ----
 *
 * The {@link io.vertx.ext.shell.term.TelnetTermOptions} extends the Vert.x Core `NetServerOptions` as the Telnet server
 * implementation is based on a `NetServer`.
 *
 * CAUTION: Telnet does not provide any authentication nor encryption at all.
 *
 * Starting a shell service available via HTTP:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#runHttpService}
 * ----
 *
 * == Authentication
 *
 * The SSH and HTTP connectors provide both authentication built on top of _vertx-auth_ with the following supported
 * providers:
 *
 * - _shiro_ : provides `.properties` and _LDAP_ backend as seen in the ShellService presentation
 * - _jdbc_ : JDBC backend
 * - _mongo_ : MongoDB backend
 *
 * These options can be created directly using directly {@link io.vertx.ext.auth.AuthOptions}:
 *
 * - {@link io.vertx.ext.auth.shiro.ShiroAuthOptions} for Shiro
 * - {@link io.vertx.ext.auth.jdbc.JDBCAuthOptions} for JDBC
 * - {@link io.vertx.ext.auth.mongo.MongoAuthOptions} for Mongo
 *
 * As for external service configuration in Json, the `authOptions` uses the `provider` property to distinguish:
 *
 * ----
 * {
 *   ...
 *   "authOptions": {
 *     "provider":"shiro",
 *     "config": {
 *       "properties_path":"file:auth.properties"
 *     }
 *   }
 *   ...
 * }
 * ----
 *
 * == Telnet term configuration
 *
 * Telnet terms are configured by {@link io.vertx.ext.shell.ShellServiceOptions#setTelnetOptions},
 * the {@link io.vertx.ext.shell.term.TelnetTermOptions} extends the {@link io.vertx.core.net.NetServerOptions} so they
 * have the exact same configuration.
 *
 * == SSH term configuration
 *
 * SSH terms are configured by {@link io.vertx.ext.shell.ShellServiceOptions#setSSHOptions}:
 *
 * - {@link io.vertx.ext.shell.term.SSHTermOptions#setPort}: port
 * - {@link io.vertx.ext.shell.term.SSHTermOptions#setHost}: host
 *
 * Only username/password authentication is supported at the moment, it can be configured with property file
 * or LDAP, see Vert.x Auth for more info:
 *
 * - {@link io.vertx.ext.shell.term.SSHTermOptions#setAuthOptions}: configures user authentication
 *
 * The server key configuration reuses the key pair store configuration scheme provided by _Vert.x Core_:
 *
 * - {@link io.vertx.ext.shell.term.SSHTermOptions#setKeyPairOptions}: set `.jks` key pair store
 * - {@link io.vertx.ext.shell.term.SSHTermOptions#setPfxKeyPairOptions}: set `.pfx` key pair store
 * - {@link io.vertx.ext.shell.term.SSHTermOptions#setPemKeyPairOptions}: set `.pem` key pair store
 *
 *
 * .Deploying the Shell Service on SSH with Mongo authentication
 * [source,$lang,subs="+attributes"]
 * ----
 * {@link examples.Examples#deploySSHServiceWithMongo(io.vertx.core.Vertx)}
 * ----
 *
 * .Running the Shell Service on SSH with Mongo authentication
 * [source,$lang,subs="+attributes"]
 * ----
 * {@link examples.Examples#runSSHServiceWithMongo(io.vertx.core.Vertx)}
 * ----
 *
 * .Deploying the Shell Service on SSH with JDBC authentication
 * [source,$lang,subs="+attributes"]
 * ----
 * {@link examples.Examples#deploySSHServiceWithJDBC(io.vertx.core.Vertx)}
 * ----
 *
 * .Running the Shell Service on SSH with JDBC authentication
 * [source,$lang,subs="+attributes"]
 * ----
 * {@link examples.Examples#runSSHServiceWithJDBC(io.vertx.core.Vertx)}
 * ----
 *
 * == HTTP term configuration
 *
 * HTTP terms are configured by {@link io.vertx.ext.shell.ShellServiceOptions#setHttpOptions}, the http options
 * extends the {@link io.vertx.core.http.HttpServerOptions} so they expose the exact same configuration.
 *
 * In addition there are extra options for configuring an HTTP term:
 *
 * - {@link io.vertx.ext.shell.term.HttpTermOptions#setAuthOptions}: configures user authentication
 * - {@link io.vertx.ext.shell.term.HttpTermOptions#setSockJSHandlerOptions}: configures SockJS
 * - {@link io.vertx.ext.shell.term.HttpTermOptions#setSockJSPath}: the SockJS path in the router
 *
 * .Deploying the Shell Service on HTTP with Mongo authentication
 * [source,$lang,subs="+attributes"]
 * ----
 * {@link examples.Examples#deployHttpServiceWithMongo(io.vertx.core.Vertx)}
 * ----
 *
 * .Running the Shell Service on HTTP with Mongo authentication
 * [source,$lang,subs="+attributes"]
 * ----
 * {@link examples.Examples#runHTTPServiceWithMongo(io.vertx.core.Vertx)}
 * ----
 *
 * .Deploying the Shell Service on HTTP with JDBC authentication
 * [source,$lang,subs="+attributes"]
 * ----
 * {@link examples.Examples#deployHttpServiceWithJDBC(io.vertx.core.Vertx)}
 * ----
 *
 * .Running the Shell Service on HTTP with JDBC authentication
 * [source,$lang,subs="+attributes"]
 * ----
 * {@link examples.Examples#runHTTPServiceWithJDBC(io.vertx.core.Vertx)}
 * ----
 *
 * == Keymap configuration
 *
 * The shell uses a default keymap configuration that can be overriden using the `inputrc` property of the various
 * term configuration object:
 *
 * - {@link io.vertx.ext.shell.term.TelnetTermOptions#setIntputrc}
 * - {@link io.vertx.ext.shell.term.SSHTermOptions#setIntputrc}
 * - {@link io.vertx.ext.shell.term.HttpTermOptions#setIntputrc}
 *
 * The `inputrc` must point to a file available via the classloader or the filesystem.
 *
 * The `inputrc` only function bindings and the available functions are:
 *
 * - _backward-char_
 * - _forward-char_
 * - _next-history_
 * - _previous-history_
 * - _backward-delete-char_
 * - _backward-delete-char_
 * - _backward-word_
 * - _end-of-line_
 * - _beginning-of-line_
 * - _delete-char_
 * - _delete-char_
 * - _complete_
 * - _accept-line_
 * - _accept-line_
 * - _kill-line_
 * - _backward-word_
 * - _forward-word_
 * - _backward-kill-word_
 *
 * NOTE: Extra functions can be added, however this is done by implementing functions of the `Term.d` project on which
 * Vert.x Shell is based, for instance the https://github.com/termd/termd/blob/c1629623c8a3add4bde7778640bf8cc233a7c98f/src/examples/java/examples/readlinefunction/ReverseFunction.java[reverse function]
 * can be implemented and then declared in a `META-INF/services/io.termd.core.readline.Function` to be loaded by the shell.
 *
 * == Base commands
 *
 * To find out the available commands you can use the _help_ builtin command:
 *
 * . Verticle commands
 * .. verticle-ls: list all deployed verticles
 * .. verticle-undeploy: undeploy a verticle
 * .. verticle-deploy: deploys a verticle with deployment options as JSON string
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
 * NOTE: this command list should evolve in next releases of Vert.x Shell. Other Vert.x project may provide commands to extend
 * Vert.x Shell, for instance Dropwizard Metrics.
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
 * After a command is created, it needs to be registed to a {@link io.vertx.ext.shell.command.CommandRegistry}. The
 * command registry holds all the commands for a Vert.x instance.
 *
 * A command is registered until it is unregistered with the {@link io.vertx.ext.shell.command.CommandRegistry#unregisterCommand(java.lang.String)}.
 * When a command is registered from a Verticle, this command is unregistered when this verticle is undeployed.
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
 * with the shell. A {@link io.vertx.ext.shell.command.CommandProcess} extends {@link io.vertx.ext.shell.term.Tty}
 * which is used for interacting with the terminal.
 *
 * === Terminal usage
 *
 * ==== terminal I/O
 *
 * The {@link io.vertx.ext.shell.term.Tty#stdinHandler} handler is used to be notified when the terminal
 * receives data, e.g the user uses his keyboard:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#readStdin}
 * ----
 *
 * A command can use the {@link io.vertx.ext.shell.term.Tty#write} to write to the standard output.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#writeStdout}
 * ----
 *
 * ==== Terminal size
 *
 * The current terminal size can be obtained using {@link io.vertx.ext.shell.term.Tty#width()} and
 * {@link io.vertx.ext.shell.term.Tty#height()}.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#terminalSize}
 * ----
 *
 * ==== Resize event
 *
 * When the size of the terminal changes the {@link io.vertx.ext.shell.term.Tty#resizehandler(io.vertx.core.Handler)}
 * is called, the new terminal size can be obtained with {@link io.vertx.ext.shell.term.Tty#width()} and
 * {@link io.vertx.ext.shell.term.Tty#height()}.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#resizeHandlerTerminal}
 * ----
 *
 * ==== Terminal type
 *
 * The terminal type is useful for sending escape codes to the remote terminal: {@link io.vertx.ext.shell.term.Tty#type()}
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
 * used in commands to scope data. A command can get the session with {@link io.vertx.ext.shell.command.CommandProcess#session()}:
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
 * ==== Interrupt event
 *
 * The {@link io.vertx.ext.shell.command.CommandProcess#interruptHandler(io.vertx.core.Handler)} is called when the process
 * is interrupted, this event is fired when the user press _Ctrl+C_ during the execution of a command. This handler can
 * be used for interrupting commands _blocking_ the CLI and gracefully ending the command process:
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
 * - the command can receive interrupts event or end events
 *
 * The {@link io.vertx.ext.shell.command.CommandProcess#resumeHandler(io.vertx.core.Handler)} is called when the process
 * is resumed, usually when the user types _fg_:
 *
 * - the command can receive the resume event when it has registered an handler for this event
 * - the command will receive again data from the standard input when it has registered an stdin handler
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#suspendResumeHandler}
 * ----
 *
 * ==== End events
 *
 * The {@link io.vertx.ext.shell.command.CommandProcess#endHandler(io.vertx.core.Handler)} (io.vertx.core.Handler)} is
 * called when the process is running or suspended and the command terminates, for instance the shell session is closed,
 * the command is _terminated_.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#endHandler}
 * ----
 *
 * The end handler is called even when the command invokes {@link io.vertx.ext.shell.command.CommandProcess#end()}.
 *
 * This handler is useful for cleaning up resources upon command termination, for instance closing a client or a timer.
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
 * == Shell server
 *
 * The Shell service is a convenient facade for starting a preconfigured shell either programmatically or as a Vert.x service.
 * When more flexibility is needed, a {@link io.vertx.ext.shell.ShellServer} can be used instead of the service.
 *
 * For instance the shell http term can be configured to use an existing router instead of starting its own http server.
 *
 * Using a shell server requires explicit configuration but provides full flexiblity, a shell server is setup in a few
 * steps:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#shellServer}
 * ----
 * <1> create a the shell server
 * <2> create an HTTP term server mounted on an existing router
 * <3> create an SSH term server
 * <4> register term servers
 * <5> register all base commands
 * <6> finally start the shell server
 *
 * Besides, the shell server can also be used for creating in process shell session: it provides a programmatic interactive shell.
 *
 * In process shell session can be created with {@link io.vertx.ext.shell.ShellServer#createShell}:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#creatingShell}
 * ----
 *
 * The main use case is running or testing a command:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#runningShellCommand}
 * ----
 *
 * The {@link io.vertx.ext.shell.term.Pty} pseudo terminal is the main interface for interacting with the command
 * when it's running:
 *
 * - uses standard input/output for writing or reading strings
 * - resize the terminal
 *
 * The {@link io.vertx.ext.shell.system.JobController#close} closes the shell, it will terminate all jobs in the current shell
 * session.
 *
 * == Terminal servers
 *
 * Vert.x Shell also provides bare terminal servers for those who need to write pure terminal applications.
 *
 * A {@link io.vertx.ext.shell.term.Term} handler must be set on a term server before starting it. This handler will
 * handle each term when the user connects.
 *
 * An {@link io.vertx.ext.auth.AuthOptions} can be set on {@link io.vertx.ext.shell.term.SSHTermOptions} and {@link io.vertx.ext.shell.term.HttpTermOptions}.
 * Alternatively, an {@link io.vertx.ext.auth.AuthProvider} can be {@link io.vertx.ext.shell.term.TermServer#authProvider(io.vertx.ext.auth.AuthProvider) set}
 * directly on the term server before starting it.
 *
 * === SSH term
 *
 * The terminal server {@link io.vertx.ext.shell.term.Term} handler accepts incoming terminal connections.
 * When a remote terminal connects, the {@link io.vertx.ext.shell.term.Term} can be used to interact with connected
 * terminal.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#sshEchoTerminal}
 * ----
 *
 * The {@link io.vertx.ext.shell.term.Term} is also a {@link io.vertx.ext.shell.term.Tty}, this section explains
 * how to use the tty.
 *
 * === Telnet term
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#telnetEchoTerminal}
 * ----
 *
 * === HTTP term
 *
 * The {@link io.vertx.ext.shell.term.TermServer#createHttpTermServer} method creates an HTTP term server, built
 * on top of Vert.x Web using the SockJS protocol.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#httpEchoTerminal}
 * ----
 *
 * An HTTP term can start its own HTTP server, or it can reuse an existing Vert.x Web {@link io.vertx.ext.web.Router}.
 *
 * The shell can be found at `/shell.html`.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#httpEchoTerminalUsingRouter}
 * ----
 *
 * The later option is convenient when the HTTP shell is integrated in an existing HTTP server.
 *
 * The HTTP term server by default is configured for serving:
 *
 * - the `shell.html` page
 * - the `https://github.com/chjj/term.js/[term.js]` client library
 * - the `vertxshell.js` client library
 *
 * The `vertxshell.js` integrates `term.js` is the client side part of the HTTP term.
 *
 * It integrates `term.js` with SockJS and needs the URL of the HTTP term server endpoint:
 *
 * [source,javascript]
 * ----
 * window.addEventListener('load', function () {
 *   var url = 'http://localhost/shell';
 *   new VertxTerm(url, {
 *     cols: 80,
 *     rows: 24
 *    });
 *  });
 * ----
 *
 * Straight websockets can also be used, if so, the remote term URL should be suffixed with `/websocket`:
 *
 * [source,javascript]
 * ----
 * window.addEventListener('load', function () {
 *   var url = 'ws://localhost/shell/websocket';
 *   new VertxTerm(url, {
 *     cols: 80,
 *     rows: 24
 *    });
 *  });
 * ----
 *
 * For customization purpose these resources can be copied and customized, they are available in the Vert.x Shell
 * jar under the `io.vertx.ext.shell` packages.
 *
 * == Command discovery
 *
 * The command discovery can be used when new commands need to be added to Vert.x without an explicit registration.
 *
 * For example, the _Dropwizard_ metrics service, adds specific metrics command to the shell service on the fly.
 *
 * It can be achieved via the {@code java.util.ServiceLoader} of a {@link io.vertx.ext.shell.spi.CommandResolverFactory}.
 *
 * [source,java]
 * ----
 * public class CustomCommands implements CommandResolverFactory {
 *
 *   public void resolver(Vertx vertx, Handler<AsyncResult<CommandResolver>> resolverHandler) {
 *     resolverHandler.handler(() -> Arrays.asList(myCommand1, myCommand2));
 *   }
 * }
 * ----
 *
 * The {@code resolver} method is async, because the resolver may need to wait some condition before commands
 * are resolved.
 *
 * The shell service discovery using the service loader mechanism:
 *
 * .The service provider file `META-INF/services/io.vertx.ext.shell.spi.CommandResolverFactory`
 * [source]
 * ----
 * my.CustomCommands
 * ----
 *
 * This is only valid for the {@link io.vertx.ext.shell.ShellService}. {@link io.vertx.ext.shell.ShellServer}
 * don't use this mechanism.
 *
 * == Command pack
 *
 * A command pack is a jar that provides new Vert.x Shell commands.
 *
 * Such jar just need to be present on the classpath and it is discovered by Vertx. Shell.
 *
 * [source,java]
 * ----
 * {@link examples.pack.CommandPackExample}
 * ----
 *
 * The command pack uses command discovery mechanism, so it needs the descriptor:
 *
 * .`META-INF/services/io.vertx.ext.shell.spi.CommandResolverFactory` descriptor
 * [source]
 * ----
 * examples.pack.CommandPackExample
 * ----
 */
@ModuleGen(name = "vertx-shell", groupPackage = "io.vertx")
@Document(fileName = "index.adoc")
package io.vertx.ext.shell;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
