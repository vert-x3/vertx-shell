require 'vertx-shell/command_registration'
require 'vertx-shell/command'
require 'vertx-shell/completion'
require 'vertx/vertx'
require 'vertx-shell/cli_token'
require 'vertx-shell/command_pack'
require 'vertx-shell/process'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.registry.CommandRegistry
module VertxShell
  #  A registry that contains the commands known by a shell.
  class CommandRegistry
    # @private
    # @param j_del [::VertxShell::CommandRegistry] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::CommandRegistry] the underlying java delegate
    def j_del
      @j_del
    end
    #  Get the registry for the Vert.x instance
    # @param [::Vertx::Vertx] vertx the vertx instance
    # @return [::VertxShell::CommandRegistry] the registry
    def self.get(vertx=nil)
      if vertx.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellRegistry::CommandRegistry.java_method(:get, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::VertxShell::CommandRegistry)
      end
      raise ArgumentError, "Invalid arguments when calling get(vertx)"
    end
    #  @return the current command registrations
    # @return [Array<::VertxShell::CommandRegistration>]
    def registrations
      if !block_given?
        return @j_del.java_method(:registrations, []).call().to_a.map { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxShell::CommandRegistration) }
      end
      raise ArgumentError, "Invalid arguments when calling registrations()"
    end
    #  Try to create a process from the command line tokens.
    # @overload createProcess(line)
    #   @param [String] line the command line to parse
    # @overload createProcess(line)
    #   @param [Array<::VertxShell::CliToken>] line the command line tokens
    # @return [::VertxShell::Process] the created process
    def create_process(param_1=nil)
      if param_1.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:createProcess, [Java::java.lang.String.java_class]).call(param_1),::VertxShell::Process)
      elsif param_1.class == Array && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:createProcess, [Java::JavaUtil::List.java_class]).call(param_1.map { |element| element.j_del }),::VertxShell::Process)
      end
      raise ArgumentError, "Invalid arguments when calling create_process(param_1)"
    end
    #  Perform completion, the completion argument will be notified of the completion progress.
    # @param [::VertxShell::Completion] completion the completion object
    # @return [void]
    def complete(completion=nil)
      if completion.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:complete, [Java::IoVertxExtShellCli::Completion.java_class]).call(completion.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling complete(completion)"
    end
    # @param [::VertxShell::Command] command 
    # @yield 
    # @return [self]
    def register_command(command=nil)
      if command.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:registerCommand, [Java::IoVertxExtShellCommand::Command.java_class]).call(command.j_del)
        return self
      elsif command.class.method_defined?(:j_del) && block_given?
        @j_del.java_method(:registerCommand, [Java::IoVertxExtShellCommand::Command.java_class,Java::IoVertxCore::Handler.java_class]).call(command.j_del,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxShell::CommandRegistration) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling register_command(command)"
    end
    # @overload registerCommands(commandPack)
    #   @param [::VertxShell::CommandPack] commandPack the commands to register
    # @overload registerCommands(commands)
    #   @param [Array<::VertxShell::Command>] commands the commands to register
    # @overload registerCommands(commandPack,doneHandler)
    #   @param [::VertxShell::CommandPack] commandPack the commands to register
    #   @yield 
    # @overload registerCommands(commands,doneHandler)
    #   @param [Array<::VertxShell::Command>] commands 
    #   @yield 
    # @return [self]
    def register_commands(param_1=nil)
      if param_1.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:registerCommands, [Java::IoVertxExtShellCommand::CommandPack.java_class]).call(param_1.j_del)
        return self
      elsif param_1.class == Array && !block_given?
        @j_del.java_method(:registerCommands, [Java::JavaUtil::List.java_class]).call(param_1.map { |element| element.j_del })
        return self
      elsif param_1.class.method_defined?(:j_del) && block_given?
        @j_del.java_method(:registerCommands, [Java::IoVertxExtShellCommand::CommandPack.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.j_del,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxShell::CommandRegistration) } : nil) }))
        return self
      elsif param_1.class == Array && block_given?
        @j_del.java_method(:registerCommands, [Java::JavaUtil::List.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.map { |element| element.j_del },(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxShell::CommandRegistration) } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling register_commands(param_1)"
    end
    # @param [String] commandName 
    # @yield 
    # @return [self]
    def unregister_command(commandName=nil)
      if commandName.class == String && !block_given?
        @j_del.java_method(:unregisterCommand, [Java::java.lang.String.java_class]).call(commandName)
        return self
      elsif commandName.class == String && block_given?
        @j_del.java_method(:unregisterCommand, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(commandName,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling unregister_command(commandName)"
    end
  end
end
