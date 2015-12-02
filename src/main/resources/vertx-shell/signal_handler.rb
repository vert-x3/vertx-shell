require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.term.SignalHandler
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class SignalHandler
    # @private
    # @param j_del [::VertxShell::SignalHandler] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::SignalHandler] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [Fixnum] key 
    # @return [true,false]
    def deliver?(key=nil)
      if key.class == Fixnum && !block_given?
        return @j_del.java_method(:deliver, [Java::int.java_class]).call(key)
      end
      raise ArgumentError, "Invalid arguments when calling deliver?(key)"
    end
  end
end
