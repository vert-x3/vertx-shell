require 'vertx-shell/tty'
require 'vertx-shell/session'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.system.Process
module VertxShell
  #  A process managed by the shell.
  class Process
    # @private
    # @param j_del [::VertxShell::Process] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Process] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::VertxShell::Tty] tty 
    # @return [void]
    def set_tty(tty=nil)
      if tty.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:setTty, [Java::IoVertxExtShellIo::Tty.java_class]).call(tty.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling set_tty(tty)"
    end
    # @return [::VertxShell::Tty]
    def get_tty
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getTty, []).call(),::VertxShell::Tty)
      end
      raise ArgumentError, "Invalid arguments when calling get_tty()"
    end
    # @param [::VertxShell::Session] session 
    # @return [void]
    def set_session(session=nil)
      if session.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:setSession, [Java::IoVertxExtShellSession::Session.java_class]).call(session.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling set_session(session)"
    end
    # @return [::VertxShell::Session]
    def get_session
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getSession, []).call(),::VertxShell::Session)
      end
      raise ArgumentError, "Invalid arguments when calling get_session()"
    end
    #  Execute the process.
    # @yield the end handler
    # @return [void]
    def execute
      if block_given?
        return @j_del.java_method(:execute, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(event) }))
      end
      raise ArgumentError, "Invalid arguments when calling execute()"
    end
    #  Attempt to interrupt the process.
    # @return [true,false] true if the process caught the signal
    def interrupt?
      if !block_given?
        return @j_del.java_method(:interrupt, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling interrupt?()"
    end
    #  Suspend the process.
    # @return [void]
    def resume
      if !block_given?
        return @j_del.java_method(:resume, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling resume()"
    end
    #  Resume the process.
    # @return [void]
    def suspend
      if !block_given?
        return @j_del.java_method(:suspend, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling suspend()"
    end
  end
end
