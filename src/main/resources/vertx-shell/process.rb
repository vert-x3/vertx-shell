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
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == Process
    end
    def @@j_api_type.wrap(obj)
      Process.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxExtShellSystem::Process.java_class
    end
    # @return [:READY,:RUNNING,:STOPPED,:TERMINATED] the current process status
    def status
      if !block_given?
        return @j_del.java_method(:status, []).call().name.intern
      end
      raise ArgumentError, "Invalid arguments when calling status()"
    end
    # @return [Fixnum] the process exit code when the status is  otherwise <code>null</code>
    def exit_code
      if !block_given?
        return @j_del.java_method(:exitCode, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling exit_code()"
    end
    #  Set the process tty.
    # @param [::VertxShell::Tty] tty the process tty
    # @return [self]
    def set_tty(tty=nil)
      if tty.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:setTty, [Java::IoVertxExtShellTerm::Tty.java_class]).call(tty.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling set_tty(#{tty})"
    end
    # @return [::VertxShell::Tty] the process tty
    def get_tty
      if !block_given?
        if @cached_get_tty != nil
          return @cached_get_tty
        end
        return @cached_get_tty = ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getTty, []).call(),::VertxShell::Tty)
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
      raise ArgumentError, "Invalid arguments when calling set_session(#{session})"
    end
    # @return [::VertxShell::Session] the process session
    def get_session
      if !block_given?
        if @cached_get_session != nil
          return @cached_get_session
        end
        return @cached_get_session = ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getSession, []).call(),::VertxShell::Session)
      end
      raise ArgumentError, "Invalid arguments when calling get_session()"
    end
    #  Set an handler for being notified when the process terminates.
    # @yield the handler called when the process terminates.
    # @return [self]
    def terminated_handler
      if block_given?
        @j_del.java_method(:terminatedHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(event) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling terminated_handler()"
    end
    #  Run the process.
    # @param [true,false] foregraound 
    # @yield handler called after process callback
    # @return [void]
    def run(foregraound=nil)
      if !block_given? && foregraound == nil
        return @j_del.java_method(:run, []).call()
      elsif (foregraound.class == TrueClass || foregraound.class == FalseClass) && !block_given?
        return @j_del.java_method(:run, [Java::boolean.java_class]).call(foregraound)
      elsif block_given? && foregraound == nil
        return @j_del.java_method(:run, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
      elsif (foregraound.class == TrueClass || foregraound.class == FalseClass) && block_given?
        return @j_del.java_method(:run, [Java::boolean.java_class,Java::IoVertxCore::Handler.java_class]).call(foregraound,Proc.new { yield })
      end
      raise ArgumentError, "Invalid arguments when calling run(#{foregraound})"
    end
    #  Attempt to interrupt the process.
    # @yield handler called after interrupt callback
    # @return [true,false] true if the process caught the signal
    def interrupt?
      if !block_given?
        return @j_del.java_method(:interrupt, []).call()
      elsif block_given?
        return @j_del.java_method(:interrupt, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
      end
      raise ArgumentError, "Invalid arguments when calling interrupt?()"
    end
    #  Suspend the process.
    # @param [true,false] foreground 
    # @yield handler called after resume callback
    # @return [void]
    def resume(foreground=nil)
      if !block_given? && foreground == nil
        return @j_del.java_method(:resume, []).call()
      elsif (foreground.class == TrueClass || foreground.class == FalseClass) && !block_given?
        return @j_del.java_method(:resume, [Java::boolean.java_class]).call(foreground)
      elsif block_given? && foreground == nil
        return @j_del.java_method(:resume, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
      elsif (foreground.class == TrueClass || foreground.class == FalseClass) && block_given?
        return @j_del.java_method(:resume, [Java::boolean.java_class,Java::IoVertxCore::Handler.java_class]).call(foreground,Proc.new { yield })
      end
      raise ArgumentError, "Invalid arguments when calling resume(#{foreground})"
    end
    #  Resume the process.
    # @yield handler called after suspend callback
    # @return [void]
    def suspend
      if !block_given?
        return @j_del.java_method(:suspend, []).call()
      elsif block_given?
        return @j_del.java_method(:suspend, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
      end
      raise ArgumentError, "Invalid arguments when calling suspend()"
    end
    #  Terminate the process.
    # @yield handler called after end callback
    # @return [void]
    def terminate
      if !block_given?
        return @j_del.java_method(:terminate, []).call()
      elsif block_given?
        return @j_del.java_method(:terminate, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
      end
      raise ArgumentError, "Invalid arguments when calling terminate()"
    end
    #  Set the process in background.
    # @yield handler called after background callback
    # @return [void]
    def to_background
      if !block_given?
        return @j_del.java_method(:toBackground, []).call()
      elsif block_given?
        return @j_del.java_method(:toBackground, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
      end
      raise ArgumentError, "Invalid arguments when calling to_background()"
    end
    #  Set the process in foreground.
    # @yield handler called after foreground callback
    # @return [void]
    def to_foreground
      if !block_given?
        return @j_del.java_method(:toForeground, []).call()
      elsif block_given?
        return @j_del.java_method(:toForeground, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
      end
      raise ArgumentError, "Invalid arguments when calling to_foreground()"
    end
  end
end
