require 'vertx-shell/stream'
require 'vertx-shell/tty'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.io.Pty
module VertxShell
  #  A pseudo terminal used for controlling a {::VertxShell::Tty}. This interface acts as a pseudo
  #  terminal master, {::VertxShell::Pty#slave} returns the assocated slave pseudo terminal.
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
    #  Create a new pseudo terminal.
    # @param [String] terminalType the terminal type, for instance 
    # @return [::VertxShell::Pty] the created pseudo terminal
    def self.create(terminalType=nil)
      if !block_given? && terminalType == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellIo::Pty.java_method(:create, []).call(),::VertxShell::Pty)
      elsif terminalType.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellIo::Pty.java_method(:create, [Java::java.lang.String.java_class]).call(terminalType),::VertxShell::Pty)
      end
      raise ArgumentError, "Invalid arguments when calling create(terminalType)"
    end
    #  @return the standard input of the terminal
    # @return [::VertxShell::Stream]
    def stdin
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:stdin, []).call(),::VertxShell::Stream)
      end
      raise ArgumentError, "Invalid arguments when calling stdin()"
    end
    #  Set the standard out of the pseudo terminal.
    # @param [::VertxShell::Stream] stdout the standard output
    # @return [self]
    def set_stdout(stdout=nil)
      if stdout.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:setStdout, [Java::IoVertxExtShellIo::Stream.java_class]).call(stdout.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling set_stdout(stdout)"
    end
    #  Resize the terminal.
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
    #  @return the pseudo terminal slave
    # @return [::VertxShell::Tty]
    def slave
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:slave, []).call(),::VertxShell::Tty)
      end
      raise ArgumentError, "Invalid arguments when calling slave()"
    end
  end
end
