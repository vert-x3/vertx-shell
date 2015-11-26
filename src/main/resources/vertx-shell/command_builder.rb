require 'vertx-shell/command'
require 'vertx-shell/completion'
require 'vertx/cli'
require 'vertx/vertx'
require 'vertx-shell/command_process'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.CommandBuilder
module VertxShell
  #  A build for Vert.x Shell command.
  class CommandBuilder
    # @private
    # @param j_del [::VertxShell::CommandBuilder] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::CommandBuilder] the underlying java delegate
    def j_del
      @j_del
    end
    #  Create a new commmand with its {::Vertx::CLI} descriptor. This command can then retrieve the parsed
    #  {::VertxShell::CommandProcess#command_line} when it executes to know get the command arguments and options.
    # @overload command(name)
    #   @param [String] name the command name
    # @overload command(cli)
    #   @param [::Vertx::CLI] cli the cli to use
    # @return [::VertxShell::CommandBuilder] the command
    def self.command(param_1=nil)
      if param_1.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommand::CommandBuilder.java_method(:command, [Java::java.lang.String.java_class]).call(param_1),::VertxShell::CommandBuilder)
      elsif param_1.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommand::CommandBuilder.java_method(:command, [Java::IoVertxCoreCli::CLI.java_class]).call(param_1.j_del),::VertxShell::CommandBuilder)
      end
      raise ArgumentError, "Invalid arguments when calling command(param_1)"
    end
    #  Set the command process handler, the process handler is called when the command is executed.
    # @yield the process handler
    # @return [self]
    def process_handler
      if block_given?
        @j_del.java_method(:processHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxShell::CommandProcess)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling process_handler()"
    end
    #  Set the command completion handler, the completion handler when the user asks for contextual command line
    #  completion, usually hitting the <i>tab</i> key.
    # @yield the completion handler
    # @return [self]
    def completion_handler
      if block_given?
        @j_del.java_method(:completionHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxShell::Completion)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling completion_handler()"
    end
    #  Build the command
    # @param [::Vertx::Vertx] vertx the vertx instance
    # @return [::VertxShell::Command] the built command
    def build(vertx=nil)
      if vertx.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:build, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling build(vertx)"
    end
  end
end
