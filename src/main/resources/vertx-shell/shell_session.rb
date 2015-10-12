require 'vertx-shell/job'
require 'vertx-shell/cli_token'
require 'vertx-shell/session'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.system.ShellSession
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class ShellSession
    # @private
    # @param j_del [::VertxShell::ShellSession] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::ShellSession] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [::VertxShell::Session]
    def session
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:session, []).call(),::VertxShell::Session)
      end
      raise ArgumentError, "Invalid arguments when calling session()"
    end
    # @return [Set<::VertxShell::Job>]
    def jobs
      if !block_given?
        return ::Vertx::Util::Utils.to_set(@j_del.java_method(:jobs, []).call()).map! { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxShell::Job) }
      end
      raise ArgumentError, "Invalid arguments when calling jobs()"
    end
    # @param [Fixnum] id 
    # @return [::VertxShell::Job]
    def get_job(id=nil)
      if id.class == Fixnum && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getJob, [Java::int.java_class]).call(id),::VertxShell::Job)
      end
      raise ArgumentError, "Invalid arguments when calling get_job(id)"
    end
    # @param [Array<::VertxShell::CliToken>] args 
    # @yield 
    # @return [void]
    def create_job(args=nil)
      if args.class == Array && block_given?
        return @j_del.java_method(:createJob, [Java::JavaUtil::List.java_class,Java::IoVertxCore::Handler.java_class]).call(args.map { |element| element.j_del },(Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxShell::Job) : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling create_job(args)"
    end
  end
end
