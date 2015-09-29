require 'vertx-shell/command'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.base.ShellCommands
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class ShellCommands
    # @private
    # @param j_del [::VertxShell::ShellCommands] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::ShellCommands] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [::VertxShell::Command]
    def self.sleep
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandBase::ShellCommands.java_method(:sleep, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling sleep()"
    end
    # @return [::VertxShell::Command]
    def self.echo
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandBase::ShellCommands.java_method(:echo, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling echo()"
    end
    # @return [::VertxShell::Command]
    def self.help
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandBase::ShellCommands.java_method(:help, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling help()"
    end
  end
end
