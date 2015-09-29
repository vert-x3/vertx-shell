require 'vertx-shell/command'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.base.ServerCommand
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class ServerCommand
    # @private
    # @param j_del [::VertxShell::ServerCommand] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::ServerCommand] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [::VertxShell::Command]
    def self.ls
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandBase::ServerCommand.java_method(:ls, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling ls()"
    end
  end
end
