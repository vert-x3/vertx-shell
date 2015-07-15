require 'vertx-shell/tty'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.Job
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class Job
    # @private
    # @param j_del [::VertxShell::Job] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Job] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::VertxShell::Tty] tty 
    # @return [void]
    def set_tty(tty=nil)
      if tty.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:setTty, [Java::IoVertxExtShell::Tty.java_class]).call(tty.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling set_tty(tty)"
    end
    # @yield 
    # @return [void]
    def run
      if !block_given?
        return @j_del.java_method(:run, []).call()
      elsif block_given?
        return @j_del.java_method(:run, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
      end
      raise ArgumentError, "Invalid arguments when calling run()"
    end
    # @yield 
    # @return [void]
    def end_handler
      if block_given?
        return @j_del.java_method(:endHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(event) }))
      end
      raise ArgumentError, "Invalid arguments when calling end_handler()"
    end
  end
end
