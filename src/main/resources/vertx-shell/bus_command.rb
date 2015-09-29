require 'vertx-shell/command'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.base.BusCommand
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class BusCommand
    # @private
    # @param j_del [::VertxShell::BusCommand] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::BusCommand] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [::VertxShell::Command]
    def self.send
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandBase::BusCommand.java_method(:send, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling send()"
    end
    # @return [::VertxShell::Command]
    def self.tail
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandBase::BusCommand.java_method(:tail, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling tail()"
    end
  end
end
