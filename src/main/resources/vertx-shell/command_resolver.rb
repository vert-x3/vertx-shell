require 'vertx-shell/command'
require 'vertx/vertx'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.CommandResolver
module VertxShell
  #  A resolver for commands, so the shell can discover commands.
  class CommandResolver
    # @private
    # @param j_del [::VertxShell::CommandResolver] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::CommandResolver] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::Vertx::Vertx] vertx 
    # @return [::VertxShell::CommandResolver] the base commands of Vert.x Shell.
    def self.base_commands(vertx=nil)
      if vertx.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommand::CommandResolver.java_method(:baseCommands, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::VertxShell::CommandResolver)
      end
      raise ArgumentError, "Invalid arguments when calling base_commands(vertx)"
    end
    # @return [Array<::VertxShell::Command>] the current commands
    def commands
      if !block_given?
        return @j_del.java_method(:commands, []).call().to_a.map { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxShell::Command) }
      end
      raise ArgumentError, "Invalid arguments when calling commands()"
    end
    #  Returns a single command by its name.
    # @param [String] name the command name
    # @return [::VertxShell::Command] the commad or null
    def get_command(name=nil)
      if name.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getCommand, [Java::java.lang.String.java_class]).call(name),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling get_command(name)"
    end
  end
end
