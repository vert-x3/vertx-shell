require 'vertx-shell/command'
require 'vertx/vertx'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.registry.CommandResolver
module VertxShell
  #  A resolver for commands, so the shell can discover commands automatically.
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
    #  @return the base commands of Vert.x Shell.
    # @return [::VertxShell::CommandResolver]
    def self.base_commands
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellRegistry::CommandResolver.java_method(:baseCommands, []).call(),::VertxShell::CommandResolver)
      end
      raise ArgumentError, "Invalid arguments when calling base_commands()"
    end
    #  Resolve commands.
    # @param [::Vertx::Vertx] vertx the vertx instance
    # @yield the handler that will receive the resolution callback
    # @return [void]
    def resolve_commands(vertx=nil)
      if vertx.class.method_defined?(:j_del) && block_given?
        return @j_del.java_method(:resolveCommands, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxCore::Handler.java_class]).call(vertx.j_del,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxShell::Command) } : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling resolve_commands(vertx)"
    end
  end
end
