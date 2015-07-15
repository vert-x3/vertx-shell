require 'vertx-shell/dimension'
require 'vertx-shell/stream'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.Tty
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class Tty
    # @private
    # @param j_del [::VertxShell::Tty] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Tty] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [::VertxShell::Dimension]
    def window_size
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:windowSize, []).call(),::VertxShell::Dimension)
      end
      raise ArgumentError, "Invalid arguments when calling window_size()"
    end
    # @param [::VertxShell::Stream] stdin 
    # @return [void]
    def set_stdin(stdin=nil)
      if stdin.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:setStdin, [Java::IoVertxExtShell::Stream.java_class]).call(stdin.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling set_stdin(stdin)"
    end
    # @return [::VertxShell::Stream]
    def stdout
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:stdout, []).call(),::VertxShell::Stream)
      end
      raise ArgumentError, "Invalid arguments when calling stdout()"
    end
    # @param [String] event 
    # @yield 
    # @return [void]
    def event_handler(event=nil)
      if event.class == String && block_given?
        return @j_del.java_method(:eventHandler, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(event,Proc.new { yield })
      end
      raise ArgumentError, "Invalid arguments when calling event_handler(event)"
    end
  end
end
