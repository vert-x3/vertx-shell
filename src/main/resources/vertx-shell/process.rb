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
    #  Set the process tty.
    # @param [::VertxShell::Tty] tty the process tty
    # @return [self]
    def set_tty(tty=nil)
      if tty.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:setTty, [Java::IoVertxExtShellTerm::Tty.java_class]).call(tty.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling set_tty(tty)"
    end
    #  @return the process tty
    # @return [::VertxShell::Tty]
    def get_tty
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getTty, []).call(),::VertxShell::Tty)
      end
      raise ArgumentError, "Invalid arguments when calling get_tty()"
    end
    #  Set the process session
    # @param [::VertxShell::Session] session the process session
    # @return [self]
    def set_session(session=nil)
      if session.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:setSession, [Java::IoVertxExtShellSession::Session.java_class]).call(session.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling set_session(session)"
    end
    #  @return the process session
    # @return [::VertxShell::Session]
    def get_session
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getSession, []).call(),::VertxShell::Session)
      end
      raise ArgumentError, "Invalid arguments when calling get_session()"
    end
    #  Set an handler called when the process terminates.
    # @yield the terminate handler
    # @return [self]
    def terminate_handler
      if block_given?
        @j_del.java_method(:terminateHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(event) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling terminate_handler()"
    end
    #  Run the process.
    # @return [void]
    def run
      if !block_given?
        return @j_del.java_method(:run, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling run()"
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
    #  Terminate the process.
    # @return [void]
    def terminate
      if !block_given?
        return @j_del.java_method(:terminate, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling terminate()"
    end
  end
end
