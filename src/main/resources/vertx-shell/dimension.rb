require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.Dimension
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class Dimension
    # @private
    # @param j_del [::VertxShell::Dimension] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Dimension] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [Fixnum] width 
    # @param [Fixnum] height 
    # @return [::VertxShell::Dimension]
    def self.create(width=nil,height=nil)
      if width.class == Fixnum && height.class == Fixnum && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShell::Dimension.java_method(:create, [Java::int.java_class,Java::int.java_class]).call(width,height),::VertxShell::Dimension)
      end
      raise ArgumentError, "Invalid arguments when calling create(width,height)"
    end
    # @return [Fixnum]
    def width
      if !block_given?
        return @j_del.java_method(:width, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling width()"
    end
    # @return [Fixnum]
    def height
      if !block_given?
        return @j_del.java_method(:height, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling height()"
    end
  end
end
