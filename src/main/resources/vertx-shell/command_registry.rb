require 'vertx-shell/command_registration'
require 'vertx-shell/command'
require 'vertx-shell/completion'
require 'vertx/vertx'
require 'vertx-shell/cli_token'
require 'vertx-shell/process'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.registry.CommandRegistry
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
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
    # @param [::Vertx::Vertx] vertx 
    # @return [::VertxShell::CommandRegistry]
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
    # @overload createProcess(s,handler)
    #   @param [String] s 
    #   @yield 
    # @overload createProcess(line,handler)
    #   @param [Array<::VertxShell::CliToken>] line 
    #   @yield 
    # @return [void]
    def create_process(param_1=nil)
      if param_1.class == String && block_given?
        return @j_del.java_method(:createProcess, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxShell::Process) : nil) }))
      elsif param_1.class == Array && block_given?
        return @j_del.java_method(:createProcess, [Java::JavaUtil::List.java_class,Java::IoVertxCore::Handler.java_class]).call(param_1.map { |element| element.j_del },(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxShell::Process) : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling create_process(param_1)"
    end
    # @param [::VertxShell::Completion] completion 
    # @return [void]
    def complete(completion=nil)
      if completion.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:complete, [Java::IoVertxExtShellCli::Completion.java_class]).call(completion.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling complete(completion)"
    end
    # @param [::VertxShell::Command] command 
    # @yield 
    # @return [void]
    def register_command(command=nil)
      if command.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:registerCommand, [Java::IoVertxExtShellCommand::Command.java_class]).call(command.j_del)
      elsif command.class.method_defined?(:j_del) && block_given?
        return @j_del.java_method(:registerCommand, [Java::IoVertxExtShellCommand::Command.java_class,Java::IoVertxCore::Handler.java_class]).call(command.j_del,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxShell::CommandRegistration) : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling register_command(command)"
    end
    # @param [String] commandName 
    # @yield 
    # @return [void]
    def unregister_command(commandName=nil)
      if commandName.class == String && !block_given?
        return @j_del.java_method(:unregisterCommand, [Java::java.lang.String.java_class]).call(commandName)
      elsif commandName.class == String && block_given?
        return @j_del.java_method(:unregisterCommand, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(commandName,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling unregister_command(commandName)"
    end
    # @return [void]
    def release
      if !block_given?
        return @j_del.java_method(:release, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling release()"
    end
  end
end
