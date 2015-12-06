require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.term.Tty
module VertxShell
  #  Provide interactions with the Shell TTY.
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
    #  @return the declared tty type, for instance , ,  etc... it can be null
    #  when the tty does not have declared its type.
    # @return [String]
    def type
      if !block_given?
        return @j_del.java_method(:type, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling type()"
    end
    #  @return the current width, i.e the number of rows or  if unknown
    # @return [Fixnum]
    def width
      if !block_given?
        return @j_del.java_method(:width, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling width()"
    end
    #  @return the current height, i.e the number of columns or  if unknown
    # @return [Fixnum]
    def height
      if !block_given?
        return @j_del.java_method(:height, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling height()"
    end
    #  Set a stream handler on the standard input to read the data.
    # @yield the standard input
    # @return [self]
    def stdin_handler
      if block_given?
        @j_del.java_method(:stdinHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(event) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling stdin_handler()"
    end
    #  Write data to the standard output.
    # @param [String] data the data to write
    # @return [self]
    def write(data=nil)
      if data.class == String && !block_given?
        @j_del.java_method(:write, [Java::java.lang.String.java_class]).call(data)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling write(data)"
    end
    #  Set a resize handler, the handler is called when the tty size changes.
    # @yield the resize handler
    # @return [self]
    def resizehandler
      if block_given?
        @j_del.java_method(:resizehandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling resizehandler()"
    end
  end
end
