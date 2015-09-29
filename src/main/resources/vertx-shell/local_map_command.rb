require 'vertx-shell/command'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.base.LocalMapCommand
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class LocalMapCommand
    # @private
    # @param j_del [::VertxShell::LocalMapCommand] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::LocalMapCommand] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [::VertxShell::Command]
    def self.get
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandBase::LocalMapCommand.java_method(:get, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling get()"
    end
    # @return [::VertxShell::Command]
    def self.put
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandBase::LocalMapCommand.java_method(:put, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling put()"
    end
    # @return [::VertxShell::Command]
    def self.rm
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandBase::LocalMapCommand.java_method(:rm, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling rm()"
    end
  end
end
