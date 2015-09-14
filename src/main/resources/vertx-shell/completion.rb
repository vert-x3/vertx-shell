require 'vertx-shell/session'
require 'vertx-shell/cli_token'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.cli.Completion
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
    #  @return the current Vert.x instance
    # @return [::Vertx::Vertx]
    def vertx
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:vertx, []).call(),::Vertx::Vertx)
      end
      raise ArgumentError, "Invalid arguments when calling vertx()"
    end
    # @return [::VertxShell::Session]
    def session
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:session, []).call(),::VertxShell::Session)
      end
      raise ArgumentError, "Invalid arguments when calling session()"
    end
    #  @return the current line being completed in raw format, i.e without any char escape performed
    # @return [String]
    def raw_line
      if !block_given?
        return @j_del.java_method(:rawLine, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling raw_line()"
    end
    # @return [Array<::VertxShell::CliToken>]
    def line_tokens
      if !block_given?
        return @j_del.java_method(:lineTokens, []).call().to_a.map { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxShell::CliToken) }
      end
      raise ArgumentError, "Invalid arguments when calling line_tokens()"
    end
    # @overload complete(candidates)
    #   @param [Array<String>] candidates 
    # @overload complete(value,terminal)
    #   @param [String] value 
    #   @param [true,false] terminal 
    # @return [void]
    def complete(param_1=nil,param_2=nil)
      if param_1.class == Array && !block_given? && param_2 == nil
        return @j_del.java_method(:complete, [Java::JavaUtil::List.java_class]).call(param_1.map { |element| element })
      elsif param_1.class == String && (param_2.class == TrueClass || param_2.class == FalseClass) && !block_given?
        return @j_del.java_method(:complete, [Java::java.lang.String.java_class,Java::boolean.java_class]).call(param_1,param_2)
      end
      raise ArgumentError, "Invalid arguments when calling complete(param_1,param_2)"
    end
  end
end
