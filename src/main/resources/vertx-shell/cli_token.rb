require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.cli.CliToken
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class CliToken
    # @private
    # @param j_del [::VertxShell::CliToken] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::CliToken] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [:BLANK,:OPT,:LONG_OPT,:TEXT]
    def get_kind
      if !block_given?
        return @j_del.java_method(:getKind, []).call().name.intern
      end
      raise ArgumentError, "Invalid arguments when calling get_kind()"
    end
    # @return [String]
    def get_raw
      if !block_given?
        return @j_del.java_method(:getRaw, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling get_raw()"
    end
    # @return [String]
    def get_value
      if !block_given?
        return @j_del.java_method(:getValue, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling get_value()"
    end
    # @param [String] s 
    # @return [Array<::VertxShell::CliToken>]
    def self.tokenize(s=nil)
      if s.class == String && !block_given?
        return Java::IoVertxExtShellCli::CliToken.java_method(:tokenize, [Java::java.lang.String.java_class]).call(s).to_a.map { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxShell::CliToken) }
      end
      raise ArgumentError, "Invalid arguments when calling tokenize(s)"
    end
  end
end
