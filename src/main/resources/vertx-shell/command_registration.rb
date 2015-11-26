require 'vertx-shell/command'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.registry.CommandRegistration
module VertxShell
  #  A registration of a command in the {::VertxShell::CommandRegistry}
  class CommandRegistration
    # @private
    # @param j_del [::VertxShell::CommandRegistration] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::CommandRegistration] the underlying java delegate
    def j_del
      @j_del
    end
    #  @return the registered command.
    # @return [::VertxShell::Command]
    def command
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getCommand, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling command()"
    end
    #  Unregister the current command
    # @yield 
    # @return [void]
    def unregister
      if !block_given?
        return @j_del.java_method(:unregister, []).call()
      elsif block_given?
        return @j_del.java_method(:unregister, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling unregister()"
    end
  end
end
