require 'vertx-shell/tty'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.term.Pty
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
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellTerm::Pty.java_method(:create, []).call(),::VertxShell::Pty)
      elsif terminalType.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellTerm::Pty.java_method(:create, [Java::java.lang.String.java_class]).call(terminalType),::VertxShell::Pty)
      end
      raise ArgumentError, "Invalid arguments when calling create(terminalType)"
    end
    #  Set the standard out handler of the pseudo terminal.
    # @yield the standard output
    # @return [self]
    def stdout_handler
      if block_given?
        @j_del.java_method(:stdoutHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(event) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling stdout_handler()"
    end
    #  Write data to the slave standard input of the pseudo terminal.
    # @param [String] data the data to write
    # @return [self]
    def write(data=nil)
      if data.class == String && !block_given?
        @j_del.java_method(:write, [Java::java.lang.String.java_class]).call(data)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling write(data)"
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
    # @return [::VertxShell::Tty] the pseudo terminal slave
    def slave
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:slave, []).call(),::VertxShell::Tty)
      end
      raise ArgumentError, "Invalid arguments when calling slave()"
    end
  end
end
