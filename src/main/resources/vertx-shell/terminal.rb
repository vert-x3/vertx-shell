require 'vertx-shell/stream'
require 'vertx-shell/tty'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.net.Terminal
module VertxShell
  #  The remote terminal.
  class Terminal < ::VertxShell::Tty
    # @private
    # @param j_del [::VertxShell::Terminal] the java delegate
    def initialize(j_del)
      super(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Terminal] the underlying java delegate
    def j_del
      @j_del
    end
    # @yield 
    # @return [self]
    def resizehandler
      if block_given?
        @j_del.java_method(:resizehandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling resizehandler()"
    end
    # @param [::VertxShell::Stream] stdin 
    # @return [self]
    def set_stdin(stdin=nil)
      if stdin.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:setStdin, [Java::IoVertxExtShellIo::Stream.java_class]).call(stdin.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling set_stdin(stdin)"
    end
    # @yield 
    # @return [self]
    def close_handler
      if block_given?
        @j_del.java_method(:closeHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling close_handler()"
    end
    # @return [void]
    def close
      if !block_given?
        return @j_del.java_method(:close, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
  end
end
