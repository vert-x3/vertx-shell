require 'vertx-shell/command'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.base.FileSystemCommand
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class FileSystemCommand
    # @private
    # @param j_del [::VertxShell::FileSystemCommand] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::FileSystemCommand] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [::VertxShell::Command]
    def self.cd
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandBase::FileSystemCommand.java_method(:cd, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling cd()"
    end
    # @return [::VertxShell::Command]
    def self.pwd
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandBase::FileSystemCommand.java_method(:pwd, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling pwd()"
    end
    # @return [::VertxShell::Command]
    def self.ls
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandBase::FileSystemCommand.java_method(:ls, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling ls()"
    end
  end
end
