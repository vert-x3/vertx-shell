require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.Completion
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class Completion
    # @private
    # @param j_del [::VertxShell::Completion] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Completion] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [String]
    def text
      if !block_given?
        return @j_del.java_method(:text, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling text()"
    end
    # @param [Array<String>] candidates 
    # @return [void]
    def complete(candidates=nil)
      if candidates.class == Array && !block_given?
        return @j_del.java_method(:complete, [Java::JavaUtil::List.java_class]).call(candidates.map { |element| element })
      end
      raise ArgumentError, "Invalid arguments when calling complete(candidates)"
    end
  end
end
