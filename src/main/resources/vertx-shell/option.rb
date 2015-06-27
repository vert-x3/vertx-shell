require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.Option
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class Option
    # @private
    # @param j_del [::VertxShell::Option] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Option] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [String] name 
    # @param [Fixnum] arity 
    # @return [::VertxShell::Option]
    def self.create(name=nil,arity=nil)
      if name.class == String && arity.class == Fixnum && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommand::Option.java_method(:create, [Java::java.lang.String.java_class,Java::int.java_class]).call(name,arity),::VertxShell::Option)
      end
      raise ArgumentError, "Invalid arguments when calling create(name,arity)"
    end
    # @return [String]
    def name
      if !block_given?
        return @j_del.java_method(:name, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling name()"
    end
    # @return [Fixnum]
    def arity
      if !block_given?
        return @j_del.java_method(:arity, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling arity()"
    end
  end
end
