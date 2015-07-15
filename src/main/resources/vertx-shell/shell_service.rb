require 'vertx-shell/command_manager'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.ShellService
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class ShellService
    # @private
    # @param j_del [::VertxShell::ShellService] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::ShellService] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::Vertx::Vertx] vertx 
    # @param [::VertxShell::CommandManager] mgr 
    # @param [Fixnum] port 
    # @return [::VertxShell::ShellService]
    def self.create(vertx=nil,mgr=nil,port=nil)
      if vertx.class.method_defined?(:j_del) && mgr.class.method_defined?(:j_del) && port.class == Fixnum && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShell::ShellService.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtShellCommand::CommandManager.java_class,Java::int.java_class]).call(vertx.j_del,mgr.j_del,port),::VertxShell::ShellService)
      end
      raise ArgumentError, "Invalid arguments when calling create(vertx,mgr,port)"
    end
    # @return [void]
    def listen
      if !block_given?
        return @j_del.java_method(:listen, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling listen()"
    end
  end
end
