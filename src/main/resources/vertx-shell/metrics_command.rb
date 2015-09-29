require 'vertx-shell/command'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.metrics.MetricsCommand
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class MetricsCommand
    # @private
    # @param j_del [::VertxShell::MetricsCommand] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::MetricsCommand] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [::VertxShell::Command]
    def self.ls
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandMetrics::MetricsCommand.java_method(:ls, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling ls()"
    end
    # @return [::VertxShell::Command]
    def self.info
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommandMetrics::MetricsCommand.java_method(:info, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling info()"
    end
  end
end
