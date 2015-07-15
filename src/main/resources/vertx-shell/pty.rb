require 'vertx-shell/dimension'
require 'vertx-shell/stream'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.term.Pty
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class Pty
    # @private
    # @param j_del [::VertxShell::Pty] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Pty] the underlying java delegate
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
    # @param [::VertxShell::Dimension] size 
    # @return [void]
    def set_window_size(size=nil)
      if size.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:setWindowSize, [Java::IoVertxExtShell::Dimension.java_class]).call(size.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling set_window_size(size)"
    end
    # @param [String] event 
    # @return [true,false]
    def send_event?(event=nil)
      if event.class == String && !block_given?
        return @j_del.java_method(:sendEvent, [Java::java.lang.String.java_class]).call(event)
      end
      raise ArgumentError, "Invalid arguments when calling send_event?(event)"
    end
  end
end
