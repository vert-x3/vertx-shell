require 'vertx-shell/command'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.base.VerticleCommand
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class VerticleCommand
    # @private
    # @param j_del [::VertxShell::VerticleCommand] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::VerticleCommand] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [::VertxShell::Command]
    def self.ls
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandBase::VerticleCommand.java_method(:ls, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling ls()"
    end
    # @return [::VertxShell::Command]
    def self.deploy
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandBase::VerticleCommand.java_method(:deploy, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling deploy()"
    end
    # @return [::VertxShell::Command]
    def self.undeploy
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandBase::VerticleCommand.java_method(:undeploy, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling undeploy()"
    end
    # @return [::VertxShell::Command]
    def self.factories
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandBase::VerticleCommand.java_method(:factories, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling factories()"
    end
  end
end
