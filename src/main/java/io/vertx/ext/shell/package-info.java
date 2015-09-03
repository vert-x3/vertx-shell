/**
 * = Vert.x Shell
 *
 * Vert.x Shell is a command line interface for the Vert.x runtime available from regular
 * terminals using different protocols.
 *
 * Vert.x Shell provides a variety of commands for interacting live with Vert.x services.
 *
 * Vert.x Shell can be extended with custom commands in the language of your choice.
 *
 * == Using Vert.x Shell
 *
 * todo.
 *
 * == Shell Service
 *
 * todo.
 *
 * == Base commands
 *
 * todo.
 *
 * == Extending Vert.x Shell
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#helloWorld}
 * ----
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
 * The `SIGINT` event is fired when the process is interrupted, this event is fired when the user press
 * _Ctrl+C_ during the execution of a command. This handler can be used for interrupting commands _blocking_ the CLI and
 * gracefully ending the command process:
 *
 * [source,$lang]
 * ----
 * {@link examples.Examples#SIGINT}
 * ----
 *
 * When no `SIGINT` handler is registered, pressing _Ctrl+C_ will have no effect.
 *
 * ==== `SIGTSTP`/`SIGCONT` events
 *
 * todo.
 *
 * ==== `SIGWINCH` event
 *
 * todo.
 *
 */
@GenModule(name = "vertx-shell")
@Document(fileName = "index.adoc")
package io.vertx.ext.shell;

import io.vertx.codegen.annotations.GenModule;
import io.vertx.docgen.Document;