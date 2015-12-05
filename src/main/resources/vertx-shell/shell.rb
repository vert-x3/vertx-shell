require 'vertx-shell/job'
require 'vertx-shell/job_controller'
require 'vertx-shell/cli_token'
require 'vertx-shell/session'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.Shell
module VertxShell
  #  An interactive session between a consumer and a shell.
  class Shell
    # @private
    # @param j_del [::VertxShell::Shell] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Shell] the underlying java delegate
    def j_del
      @j_del
    end
    #  See {::VertxShell::Shell#create_job}
    # @overload createJob(line)
    #   @param [Array<::VertxShell::CliToken>] line the command line creating this job
    # @overload createJob(line)
    #   @param [String] line 
    # @return [::VertxShell::Job]
    def create_job(param_1=nil)
      if param_1.class == Array && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:createJob, [Java::JavaUtil::List.java_class]).call(param_1.map { |element| element.j_del }),::VertxShell::Job)
      elsif param_1.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:createJob, [Java::java.lang.String.java_class]).call(param_1),::VertxShell::Job)
      end
      raise ArgumentError, "Invalid arguments when calling create_job(param_1)"
    end
    #  @return the shell's job controller
    # @return [::VertxShell::JobController]
    def job_controller
      if !block_given?
        if @cached_job_controller != nil
          return @cached_job_controller
        end
        return @cached_job_controller = ::Vertx::Util::Utils.safe_create(@j_del.java_method(:jobController, []).call(),::VertxShell::JobController)
      end
      raise ArgumentError, "Invalid arguments when calling job_controller()"
    end
    #  @return the current shell session
    # @return [::VertxShell::Session]
    def session
      if !block_given?
        if @cached_session != nil
          return @cached_session
        end
        return @cached_session = ::Vertx::Util::Utils.safe_create(@j_del.java_method(:session, []).call(),::VertxShell::Session)
      end
      raise ArgumentError, "Invalid arguments when calling session()"
    end
    #  Close the shell.
    # @return [void]
    def close
      if !block_given?
        return @j_del.java_method(:close, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
  end
end
