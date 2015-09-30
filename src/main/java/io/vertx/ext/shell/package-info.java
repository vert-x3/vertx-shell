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
 * todo.
 *
 * == Shell Service
 *
 * The {@link io.vertx.ext.shell.ShellService} takes care of starting an instance of Vert.x Shell. It can be started
 * programmatically or as a service from the command line.
 *
 * Starting a shell service available via SSH:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#runSSHService(io.vertx.core.Vertx)}
 * ----
 *
 * The server key configuration reuses the key store configuration scheme provided by _Vert.x Core_.
 *
 * User authenticates via login/password (no key authentication for now) and is based on _Vert.x Auth_ component supporting:
 *
 * - Shiro Authentication : _properties_ configuration or _ldap_ configuration
 * - JDBC Authentication : todo
 * - Mongo Authentication : todo
 *
 * Starting a shell service available via Telnet:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#runTelnetService}
 * ----
 *
 * The {@link io.vertx.ext.shell.net.TelnetOptions} extends the Vert.x Core `NetServerOptions` as the Telnet server
 * implementation is based on a `NetServer`.
 *
 * CAUTION: Telnet does not provide any authentication nor encryption at all.
 *
 * Or via the service facility:
 *
 * [source,subs="+attributes"]
 * ----
 * > vertx run maven:{maven-groupId}:{maven-artifactId}:{maven-version}
 * ----
 *
 * == Base commands
 *
 * To find out the available commands you can use the _help_ builtin command.
 *
 * todo.
 *
 * == Extending Vert.x Shell
 *
 * Vert.x Shell can be extended with custom commands in any of the languages supporting code generation.
 *
 * A command is created by the {@link io.vertx.ext.shell.command.Command#command} method: the command process handler is called
 * by the shell when the command is executed, this handler can be set with the {@link io.vertx.ext.shell.command.Command#processHandler}
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
 * === Terminal size
 *
 * The current terminal size can be obtained using {@link io.vertx.ext.shell.command.CommandProcess#width()} and
 * {@link io.vertx.ext.shell.command.CommandProcess#height()}.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#terminalSize}
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
 * === Process I/O
 *
 * A command can set a {@link io.vertx.ext.shell.command.CommandProcess#setStdin} handler
 * to be notified when the shell receives data, e.g the user uses his keyboard:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#readStdin}
 * ----
 *
 * A command can use the {@link io.vertx.ext.shell.command.CommandProcess#stdout()} to write to the standard output.
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#writeStdout}
 * ----
 *
 * Or it can use the {@link io.vertx.ext.shell.command.CommandProcess#write(java.lang.String)} method:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#write}
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
 * A command can subscribe to a few process events, named after the posix signals.
 *
 * ==== `SIGINT` event
 *
 * The {@link io.vertx.ext.shell.io.EventType#SIGINT} event is fired when the process is interrupted, this event is fired when the user press
 * _Ctrl+C_ during the execution of a command. This handler can be used for interrupting commands _blocking_ the CLI and
 * gracefully ending the command process:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#SIGINT}
 * ----
 *
 * When no `SIGINT` handler is registered, pressing _Ctrl+C_ will have no effect on the current process and the event
 * will be delayed and will likely be handled by the shell, like printing a new line on the console.
 *
 * ==== `SIGTSTP`/`SIGCONT` events
 *
 * The {@link io.vertx.ext.shell.io.EventType#SIGTSTP} event is fired when the process is running and the user press _Ctrl+Z_: the command
 * is _suspended_:
 *
 * - the command can receive the `SIGTSTP` event when it has registered an handler for this event
 * - the command will not receive anymore data from the standard input
 * - the shell prompt the user for input
 *
 * The {@link io.vertx.ext.shell.io.EventType#SIGCONT} event is fired when the process is resumed, usually when the user types _fg_:
 *
 * - the command can receive the `SIGCONT` event when it has registered an handler for this event
 * - the command will receive anymore data from the standard input when it has registered an stdin handler
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#SIGTSTP_SIGCONT}
 * ----
 *
 * ==== `SIGWINCH` event
 *
 * The {@link io.vertx.ext.shell.io.EventType#SIGWINCH} event is fired when the size of the terminal changes, the new terminal size can be obtained
 * with {@link io.vertx.ext.shell.command.CommandProcess#width()} and {@link io.vertx.ext.shell.command.CommandProcess#height()}.
 *
 * === Command completion
 *
 * A command can provide a completion handler when it wants to provide contextual command line interface completion.
 *
 * Like the process handler, the {@link io.vertx.ext.shell.command.Command#completionHandler(io.vertx.core.Handler) completion
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
 */
@ModuleGen(name = "vertx-shell", groupPackage = "io.vertx")
@Document(fileName = "index.adoc")
package io.vertx.ext.shell;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;