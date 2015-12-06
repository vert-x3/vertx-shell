require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.io.OutputStream
module VertxShell
  #  A stream of text.
  class OutputStream
    # @private
    # @param j_del [::VertxShell::OutputStream] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::OutputStream] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [String] data 
    # @return [self]
    def write(data=nil)
      if data.class == String && !block_given?
        @j_del.java_method(:write, [Java::java.lang.String.java_class]).call(data)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling write(data)"
    end
  end
end
