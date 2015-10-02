require 'vertx-shell/completion'
require 'vertx/cli'
require 'vertx-shell/command_builder'
require 'vertx-shell/command_process'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.Command
module VertxShell
  #  A Vert.x Shell command, it can be created from any language using the {::VertxShell::Command#builder} or from a
  #  Java class using {::VertxShell::Command#create}.
  class Command
    # @private
    # @param j_del [::VertxShell::Command] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Command] the underlying java delegate
    def j_del
      @j_del
    end
    #  Create a new commmand with its {::Vertx::CLI} descriptor. This command can then retrieve the parsed
    #  {::VertxShell::CommandProcess#command_line} when it executes to know get the command arguments and options.
    # @overload builder(name)
    #   @param [String] name the command name
    # @overload builder(cli)
    #   @param [::Vertx::CLI] cli the cli to use
    # @return [::VertxShell::CommandBuilder] the command
    def self.builder(param_1=nil)
      if param_1.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommand::Command.java_method(:builder, [Java::java.lang.String.java_class]).call(param_1),::VertxShell::CommandBuilder)
      elsif param_1.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommand::Command.java_method(:builder, [Java::IoVertxCoreCli::CLI.java_class]).call(param_1.j_del),::VertxShell::CommandBuilder)
      end
      raise ArgumentError, "Invalid arguments when calling builder(param_1)"
    end
    #  @return the command name
    # @return [String]
    def name
      if !block_given?
        return @j_del.java_method(:name, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling name()"
    end
    #  @return the command line interface, can be null
    # @return [::Vertx::CLI]
    def cli
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:cli, []).call(),::Vertx::CLI)
      end
      raise ArgumentError, "Invalid arguments when calling cli()"
    end
    #  Process the command, when the command is done processing it should call the {::VertxShell::CommandProcess#end} method.
    # @param [::VertxShell::CommandProcess] process the command process
    # @return [void]
    def process(process=nil)
      if process.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:process, [Java::IoVertxExtShellCommand::CommandProcess.java_class]).call(process.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling process(process)"
    end
    #  Perform command completion, when the command is done completing it should call 
    #  or  )} method to signal completion is done.
    # @param [::VertxShell::Completion] completion the completion object
    # @return [void]
    def complete(completion=nil)
      if completion.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:complete, [Java::IoVertxExtShellCli::Completion.java_class]).call(completion.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling complete(completion)"
    end
  end
end
