require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.term.SignalHandler
module VertxShell
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
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == SignalHandler
    end
    def @@j_api_type.wrap(obj)
      SignalHandler.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxExtShellTerm::SignalHandler.java_class
    end
    # @param [Fixnum] key 
    # @return [true,false]
    def deliver?(key=nil)
      if key.class == Fixnum && !block_given?
        return @j_del.java_method(:deliver, [Java::int.java_class]).call(key)
      end
      raise ArgumentError, "Invalid arguments when calling deliver?(#{key})"
    end
  end
end
