require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.io.InputStream
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class InputStream
    # @private
    # @param j_del [::VertxShell::InputStream] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::InputStream] the underlying java delegate
    def j_del
      @j_del
    end
    # @yield 
    # @return [self]
    def handler
      if block_given?
        @j_del.java_method(:handler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(event) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling handler()"
    end
  end
end
