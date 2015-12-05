require 'vertx-shell/job'
require 'vertx-shell/process'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.system.JobController
module VertxShell
  #  The job controller.<p/>
  class JobController
    # @private
    # @param j_del [::VertxShell::JobController] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::JobController] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [::VertxShell::JobController]
    def self.create
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellSystem::JobController.java_method(:create, []).call(),::VertxShell::JobController)
      end
      raise ArgumentError, "Invalid arguments when calling create()"
    end
    #  @return the current foreground job
    # @return [::VertxShell::Job]
    def foreground_job
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:foregroundJob, []).call(),::VertxShell::Job)
      end
      raise ArgumentError, "Invalid arguments when calling foreground_job()"
    end
    #  @return the active jobs
    # @return [Set<::VertxShell::Job>]
    def jobs
      if !block_given?
        return ::Vertx::Util::Utils.to_set(@j_del.java_method(:jobs, []).call()).map! { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxShell::Job) }
      end
      raise ArgumentError, "Invalid arguments when calling jobs()"
    end
    #  Returns an active job in this session by its .
    # @param [Fixnum] id the job id
    # @return [::VertxShell::Job] the job of  when not found
    def get_job(id=nil)
      if id.class == Fixnum && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getJob, [Java::int.java_class]).call(id),::VertxShell::Job)
      end
      raise ArgumentError, "Invalid arguments when calling get_job(id)"
    end
    #  Create a job wrapping a process.
    # @param [::VertxShell::Process] process the process
    # @param [String] line the line
    # @return [::VertxShell::Job] the created job
    def create_job(process=nil,line=nil)
      if process.class.method_defined?(:j_del) && line.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:createJob, [Java::IoVertxExtShellSystem::Process.java_class,Java::java.lang.String.java_class]).call(process.j_del,line),::VertxShell::Job)
      end
      raise ArgumentError, "Invalid arguments when calling create_job(process,line)"
    end
    #  Close the controller and terminate all the underlying jobs, a closed controller does not accept anymore jobs.
    # @yield 
    # @return [void]
    def close
      if !block_given?
        return @j_del.java_method(:close, []).call()
      elsif block_given?
        return @j_del.java_method(:close, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
  end
end
