require 'vertx-shell/stream'
require 'vertx-shell/tty'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.io.Pty
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class Pty
    # @private
    # @param j_del [::VertxShell::Pty] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Pty] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [::VertxShell::Pty]
    def self.create
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellIo::Pty.java_method(:create, []).call(),::VertxShell::Pty)
      end
      raise ArgumentError, "Invalid arguments when calling create()"
    end
    # @return [::VertxShell::Stream]
    def stdin
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:stdin, []).call(),::VertxShell::Stream)
      end
      raise ArgumentError, "Invalid arguments when calling stdin()"
    end
    # @param [Fixnum] width 
    # @param [Fixnum] height 
    # @return [self]
    def set_size(width=nil,height=nil)
      if width.class == Fixnum && height.class == Fixnum && !block_given?
        @j_del.java_method(:setSize, [Java::int.java_class,Java::int.java_class]).call(width,height)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling set_size(width,height)"
    end
    # @return [::VertxShell::Tty]
    def slave
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:slave, []).call(),::VertxShell::Tty)
      end
      raise ArgumentError, "Invalid arguments when calling slave()"
    end
  end
end
