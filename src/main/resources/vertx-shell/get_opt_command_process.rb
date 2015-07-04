require 'vertx-shell/stream'
require 'vertx-shell/command_process'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.getopt.GetOptCommandProcess
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class GetOptCommandProcess < ::VertxShell::CommandProcess
    # @private
    # @param j_del [::VertxShell::GetOptCommandProcess] the java delegate
    def initialize(j_del)
      super(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::GetOptCommandProcess] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [Array<String>]
    def arguments
      if !block_given?
        return @j_del.java_method(:arguments, []).call().to_a.map { |elt| elt }
      end
      raise ArgumentError, "Invalid arguments when calling arguments()"
    end
    # @param [String] name 
    # @return [Array<String>]
    def get_option(name=nil)
      if name.class == String && !block_given?
        return @j_del.java_method(:getOption, [Java::java.lang.String.java_class]).call(name).to_a.map { |elt| elt }
      end
      raise ArgumentError, "Invalid arguments when calling get_option(name)"
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
    # @param [String] event 
    # @yield 
    # @return [self]
    def event_handler(event=nil)
      if event.class == String && block_given?
        @j_del.java_method(:eventHandler, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(event,Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling event_handler(event)"
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
  end
end
