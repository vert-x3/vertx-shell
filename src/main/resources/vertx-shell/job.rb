require 'vertx-shell/tty'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.system.Job
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
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
    # @return [Fixnum]
    def id
      if !block_given?
        return @j_del.java_method(:id, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling id()"
    end
    # @return [:STARTING,:RUNNING,:STOPPED,:TERMINATED]
    def status
      if !block_given?
        return @j_del.java_method(:status, []).call().name.intern
      end
      raise ArgumentError, "Invalid arguments when calling status()"
    end
    # @return [Fixnum]
    def last_stopped
      if !block_given?
        return @j_del.java_method(:lastStopped, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling last_stopped()"
    end
    # @return [String]
    def line
      if !block_given?
        return @j_del.java_method(:line, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling line()"
    end
    # @return [::VertxShell::Tty]
    def get_tty
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getTty, []).call(),::VertxShell::Tty)
      end
      raise ArgumentError, "Invalid arguments when calling get_tty()"
    end
    # @param [::VertxShell::Tty] tty 
    # @return [self]
    def set_tty(tty=nil)
      if tty.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:setTty, [Java::IoVertxExtShellIo::Tty.java_class]).call(tty.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling set_tty(tty)"
    end
    # @return [self]
    def resize
      if !block_given?
        @j_del.java_method(:resize, []).call()
        return self
      end
      raise ArgumentError, "Invalid arguments when calling resize()"
    end
    # @return [true,false]
    def interrupt?
      if !block_given?
        return @j_del.java_method(:interrupt, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling interrupt?()"
    end
    # @yield 
    # @return [void]
    def run
      if block_given?
        return @j_del.java_method(:run, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(event) }))
      end
      raise ArgumentError, "Invalid arguments when calling run()"
    end
    # @return [self]
    def resume
      if !block_given?
        @j_del.java_method(:resume, []).call()
        return self
      end
      raise ArgumentError, "Invalid arguments when calling resume()"
    end
    # @return [self]
    def suspend
      if !block_given?
        @j_del.java_method(:suspend, []).call()
        return self
      end
      raise ArgumentError, "Invalid arguments when calling suspend()"
    end
  end
end
