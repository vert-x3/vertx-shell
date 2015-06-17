require 'vertx-shell/stream'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.Execution
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class Execution
    # @private
    # @param j_del [::VertxShell::Execution] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Execution] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::VertxShell::Stream] stdin 
    # @return [self]
    def set_stdin(stdin=nil)
      if stdin.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:setStdin, [Java::IoVertxExtShell::Stream.java_class]).call(stdin.j_del)
        return self
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
    # @param [String] text 
    # @return [self]
    def write(text=nil)
      if text.class == String && !block_given?
        @j_del.java_method(:write, [Java::java.lang.String.java_class]).call(text)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling write(text)"
    end
    # @param [Fixnum] code 
    # @return [void]
    def end(code=nil)
      if code.class == Fixnum && !block_given?
        return @j_del.java_method(:end, [Java::int.java_class]).call(code)
      end
      raise ArgumentError, "Invalid arguments when calling end(code)"
    end
  end
end
