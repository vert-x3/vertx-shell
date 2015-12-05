require 'vertx-shell/process'
require 'vertx-shell/tty'
require 'vertx-shell/session'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.system.Job
module VertxShell
  #  A job executed in a {::VertxShell::JobController}, grouping one or several process.<p/>
  # 
  #  The job life cycle can be controlled with the {::VertxShell::Job#run}, {::VertxShell::Job#resume} and {::VertxShell::Job#suspend} and {::VertxShell::Job#interrupt}
  #  methods.
  class Job
    # @private
    # @param j_del [::VertxShell::Job] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Job] the underlying java delegate
    def j_del
      @j_del
    end
    #  @return the job id
    # @return [Fixnum]
    def id
      if !block_given?
        return @j_del.java_method(:id, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling id()"
    end
    #  @return the job exec status
    # @return [:READY,:RUNNING,:STOPPED,:TERMINATED]
    def status
      if !block_given?
        return @j_del.java_method(:status, []).call().name.intern
      end
      raise ArgumentError, "Invalid arguments when calling status()"
    end
    #  @return when the job was last stopped
    # @return [Fixnum]
    def last_stopped
      if !block_given?
        return @j_del.java_method(:lastStopped, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling last_stopped()"
    end
    #  @return the execution line of the job, i.e the shell command line that launched this job
    # @return [String]
    def line
      if !block_given?
        return @j_del.java_method(:line, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling line()"
    end
    #  Set a tty on the job.
    # @param [::VertxShell::Tty] tty the tty to use
    # @return [self]
    def set_tty(tty=nil)
      if tty.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:setTty, [Java::IoVertxExtShellTerm::Tty.java_class]).call(tty.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling set_tty(tty)"
    end
    #  Set a session on the job.
    # @param [::VertxShell::Session] session the session to use
    # @return [self]
    def set_session(session=nil)
      if session.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:setSession, [Java::IoVertxExtShellSession::Session.java_class]).call(session.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling set_session(session)"
    end
    #  Set an handler called when the job terminates.
    # @yield the terminate handler
    # @return [self]
    def status_update_handler
      if block_given?
        @j_del.java_method(:statusUpdateHandler, [Java::IoVertxCore::Handler.java_class]).call(nil)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling status_update_handler()"
    end
    #  Run the job, before running the job a  must be set.
    # @return [self]
    def run
      if !block_given?
        @j_del.java_method(:run, []).call()
        return self
      end
      raise ArgumentError, "Invalid arguments when calling run()"
    end
    #  Attempt to interrupt the job.
    # @return [true,false] true if the job is actually interrupted
    def interrupt?
      if !block_given?
        return @j_del.java_method(:interrupt, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling interrupt?()"
    end
    #  Resume the job.
    # @param [true,false] foreground true when the job is resumed in foreground
    # @return [self]
    def resume(foreground=nil)
      if !block_given? && foreground == nil
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:resume, []).call(),::VertxShell::Job)
      elsif (foreground.class == TrueClass || foreground.class == FalseClass) && !block_given?
        @j_del.java_method(:resume, [Java::boolean.java_class]).call(foreground)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling resume(foreground)"
    end
    #  Send the job to background.
    # @return [self]
    def to_background
      if !block_given?
        @j_del.java_method(:toBackground, []).call()
        return self
      end
      raise ArgumentError, "Invalid arguments when calling to_background()"
    end
    #  Send the job to foreground.
    # @return [self]
    def to_foreground
      if !block_given?
        @j_del.java_method(:toForeground, []).call()
        return self
      end
      raise ArgumentError, "Invalid arguments when calling to_foreground()"
    end
    #  Resume the job.
    # @return [self]
    def suspend
      if !block_given?
        @j_del.java_method(:suspend, []).call()
        return self
      end
      raise ArgumentError, "Invalid arguments when calling suspend()"
    end
    #  Terminate the job.
    # @return [void]
    def terminate
      if !block_given?
        return @j_del.java_method(:terminate, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling terminate()"
    end
    #  @return the first process in the job
    # @return [::VertxShell::Process]
    def process
      if !block_given?
        if @cached_process != nil
          return @cached_process
        end
        return @cached_process = ::Vertx::Util::Utils.safe_create(@j_del.java_method(:process, []).call(),::VertxShell::Process)
      end
      raise ArgumentError, "Invalid arguments when calling process()"
    end
  end
end
