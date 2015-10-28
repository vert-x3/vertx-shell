require 'vertx-shell/tty'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.system.Job
module VertxShell
  #  A job executed in a {::VertxShell::Shell}, grouping one or several process.<p/>
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
    #  @return the job status
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
    #  @return the current tty this job uses
    # @return [::VertxShell::Tty]
    def get_tty
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getTty, []).call(),::VertxShell::Tty)
      end
      raise ArgumentError, "Invalid arguments when calling get_tty()"
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
    #  Run the job, before running the job a  must be set.
    # @yield to be notified when the job terminates
    # @return [void]
    def run
      if block_given?
        return @j_del.java_method(:run, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(event) }))
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
    #  Suspend the job.
    # @return [void]
    def resume
      if !block_given?
        return @j_del.java_method(:resume, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling resume()"
    end
    #  Resume the job.
    # @return [void]
    def suspend
      if !block_given?
        return @j_del.java_method(:suspend, []).call()
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
  end
end
