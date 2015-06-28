require 'vertx-shell/stream'
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
    # @return [::VertxShell::Stream]
    def stdin
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:stdin, []).call(),::VertxShell::Stream)
      end
      raise ArgumentError, "Invalid arguments when calling stdin()"
    end
    # @param [::VertxShell::Stream] stdout 
    # @return [void]
    def set_stdout(stdout=nil)
      if stdout.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:setStdout, [Java::IoVertxExtShell::Stream.java_class]).call(stdout.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling set_stdout(stdout)"
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
    # @param [:KILL] signal 
    # @return [void]
    def send_signal(signal=nil)
      if signal.class == Symbol && !block_given?
        return @j_del.java_method(:sendSignal, [Java::IoVertxExtShell::Signal.java_class]).call(Java::IoVertxExtShell::Signal.valueOf(signal))
      end
      raise ArgumentError, "Invalid arguments when calling send_signal(signal)"
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
