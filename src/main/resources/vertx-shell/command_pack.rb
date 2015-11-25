require 'vertx-shell/command'
require 'vertx/vertx'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.CommandPack
module VertxShell
  #  A command pack is a set of commands, for instance the base command pack, the metrics command pack, etc...
  class CommandPack
    # @private
    # @param j_del [::VertxShell::CommandPack] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::CommandPack] the underlying java delegate
    def j_del
      @j_del
    end
    #  Lookup commands.
    # @param [::Vertx::Vertx] vertx the vertx instance
    # @yield the handler that will receive the lookup callback
    # @return [void]
    def lookup_commands(vertx=nil)
      if vertx.class.method_defined?(:j_del) && block_given?
        return @j_del.java_method(:lookupCommands, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxCore::Handler.java_class]).call(vertx.j_del,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ar.result.to_a.map { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxShell::Command) } : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling lookup_commands(vertx)"
    end
  end
end
