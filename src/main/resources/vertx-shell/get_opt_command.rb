require 'vertx-shell/command'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.GetOptCommand
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class GetOptCommand < ::VertxShell::Command
    # @private
    # @param j_del [::VertxShell::GetOptCommand] the java delegate
    def initialize(j_del)
      super(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::GetOptCommand] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [void]
    def foo
      if !block_given?
        return @j_del.java_method(:foo, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling foo()"
    end
  end
end
