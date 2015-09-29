require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.io.Stream
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class Stream
    # @private
    # @param j_del [::VertxShell::Stream] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Stream] the underlying java delegate
    def j_del
      @j_del
    end
    # @yield 
    # @return [::VertxShell::Stream]
    def self.of_string
      if block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellIo::Stream.java_method(:ofString, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(event) })),::VertxShell::Stream)
      end
      raise ArgumentError, "Invalid arguments when calling of_string()"
    end
    # @yield 
    # @return [::VertxShell::Stream]
    def self.of_json
      if block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellIo::Stream.java_method(:ofJson, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(event != nil ? JSON.parse(event.encode) : nil) })),::VertxShell::Stream)
      end
      raise ArgumentError, "Invalid arguments when calling of_json()"
    end
    # @overload write(data)
    #   @param [String] data 
    # @overload write(data)
    #   @param [Hash{String => Object}] data 
    # @return [self]
    def write(param_1=nil)
      if param_1.class == String && !block_given?
        @j_del.java_method(:write, [Java::java.lang.String.java_class]).call(param_1)
        return self
      elsif param_1.class == Hash && !block_given?
        @j_del.java_method(:write, [Java::IoVertxCoreJson::JsonObject.java_class]).call(::Vertx::Util::Utils.to_json_object(param_1))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling write(param_1)"
    end
  end
end
