require 'vertx-shell/command'
require 'vertx/vertx'
require 'vertx-shell/command_resolver'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.CommandRegistry
module VertxShell
  #  A registry that contains the commands known by a shell.<p/>
  # 
  #  It is a mutable command resolver.
  class CommandRegistry < ::VertxShell::CommandResolver
    # @private
    # @param j_del [::VertxShell::CommandRegistry] the java delegate
    def initialize(j_del)
      super(j_del)
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
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommand::CommandRegistry.java_method(:get, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::VertxShell::CommandRegistry)
      end
      raise ArgumentError, "Invalid arguments when calling get(vertx)"
    end
    # @param [::VertxShell::Command] command 
    # @yield 
    # @return [self]
    def register_command(command=nil)
      if command.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:registerCommand, [Java::IoVertxExtShellCommand::Command.java_class]).call(command.j_del)
        return self
      elsif command.class.method_defined?(:j_del) && block_given?
        @j_del.java_method(:registerCommand, [Java::IoVertxExtShellCommand::Command.java_class,Java::IoVertxCore::Handler.java_class]).call(command.j_del,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxShell::Command) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling register_command(command)"
    end
    # @param [Array<::VertxShell::Command>] commands 
    # @yield 
    # @return [self]
    def register_commands(commands=nil)
      if commands.class == Array && !block_given?
        @j_del.java_method(:registerCommands, [Java::JavaUtil::List.java_class]).call(commands.map { |element| element.j_del })
        return self
      elsif commands.class == Array && block_given?
        @j_del.java_method(:registerCommands, [Java::JavaUtil::List.java_class,Java::IoVertxCore::Handler.java_class]).call(commands.map { |element| element.j_del },(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxShell::Command) } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling register_commands(commands)"
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
    #  Register a command resolver.
    # @param [::VertxShell::CommandResolver] resolver the commands to resolve from
    # @yield 
    # @return [self]
    def register_resolver(resolver=nil)
      if resolver.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:registerResolver, [Java::IoVertxExtShellCommand::CommandResolver.java_class]).call(resolver.j_del)
        return self
      elsif resolver.class.method_defined?(:j_del) && block_given?
        @j_del.java_method(:registerResolver, [Java::IoVertxExtShellCommand::CommandResolver.java_class,Java::IoVertxCore::Handler.java_class]).call(resolver.j_del,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxShell::Command) } : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling register_resolver(resolver)"
    end
  end
end
