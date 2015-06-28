require 'vertx-shell/command_manager'
require 'vertx-shell/job'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.Shell
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class Shell
    # @private
    # @param j_del [::VertxShell::Shell] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Shell] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::Vertx::Vertx] vertx 
    # @param [::VertxShell::CommandManager] manager 
    # @return [::VertxShell::Shell]
    def self.create(vertx=nil,manager=nil)
      if vertx.class.method_defined?(:j_del) && manager.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShell::Shell.java_method(:create, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtShellCommand::CommandManager.java_class]).call(vertx.j_del,manager.j_del),::VertxShell::Shell)
      end
      raise ArgumentError, "Invalid arguments when calling create(vertx,manager)"
    end
    # @param [String] name 
    # @yield 
    # @return [void]
    def create_process(name=nil)
      if name.class == String && block_given?
        return @j_del.java_method(:createProcess, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(name,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxShell::Job) : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling create_process(name)"
    end
  end
end
