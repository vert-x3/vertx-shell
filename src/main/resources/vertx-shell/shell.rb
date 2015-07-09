require 'vertx-shell/command_manager'
require 'vertx-shell/job'
require 'vertx-shell/completion'
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
    # @param [String] s 
    # @yield 
    # @return [void]
    def create_job(s=nil)
      if s.class == String && block_given?
        return @j_del.java_method(:createJob, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(s,(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxShell::Job) : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling create_job(s)"
    end
    # @param [::VertxShell::Completion] completion 
    # @return [void]
    def complete(completion=nil)
      if completion.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:complete, [Java::IoVertxExtShellCompletion::Completion.java_class]).call(completion.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling complete(completion)"
    end
  end
end
