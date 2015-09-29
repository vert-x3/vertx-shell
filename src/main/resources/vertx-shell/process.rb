require 'vertx-shell/process_context'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.process.Process
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class Process
    # @private
    # @param j_del [::VertxShell::Process] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Process] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::VertxShell::ProcessContext] context 
    # @return [void]
    def execute(context=nil)
      if context.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:execute, [Java::IoVertxExtShellProcess::ProcessContext.java_class]).call(context.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling execute(context)"
    end
  end
end
