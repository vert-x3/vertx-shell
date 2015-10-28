require 'vertx/command_line'
require 'vertx-shell/stream'
require 'vertx/vertx'
require 'vertx-shell/cli_token'
require 'vertx-shell/tty'
require 'vertx-shell/session'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.CommandProcess
module VertxShell
  #  The command process provides interaction with the process of the command provided by Vert.x Shell.
  class CommandProcess < ::VertxShell::Tty
    # @private
    # @param j_del [::VertxShell::CommandProcess] the java delegate
    def initialize(j_del)
      super(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::CommandProcess] the underlying java delegate
    def j_del
      @j_del
    end
    #  @return the current Vert.x instance
    # @return [::Vertx::Vertx]
    def vertx
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:vertx, []).call(),::Vertx::Vertx)
      end
      raise ArgumentError, "Invalid arguments when calling vertx()"
    end
    #  @return the unparsed arguments tokens
    # @return [Array<::VertxShell::CliToken>]
    def args_tokens
      if !block_given?
        return @j_del.java_method(:argsTokens, []).call().to_a.map { |elt| ::Vertx::Util::Utils.safe_create(elt,::VertxShell::CliToken) }
      end
      raise ArgumentError, "Invalid arguments when calling args_tokens()"
    end
    #  @return the actual string arguments of the command
    # @return [Array<String>]
    def args
      if !block_given?
        return @j_del.java_method(:args, []).call().to_a.map { |elt| elt }
      end
      raise ArgumentError, "Invalid arguments when calling args()"
    end
    #  @return the command line object or null
    # @return [::Vertx::CommandLine]
    def command_line
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:commandLine, []).call(),::Vertx::CommandLine)
      end
      raise ArgumentError, "Invalid arguments when calling command_line()"
    end
    #  @return the shell session
    # @return [::VertxShell::Session]
    def session
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:session, []).call(),::VertxShell::Session)
      end
      raise ArgumentError, "Invalid arguments when calling session()"
    end
    # @param [::VertxShell::Stream] stdin 
    # @return [self]
    def set_stdin(stdin=nil)
      if stdin.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:setStdin, [Java::IoVertxExtShellIo::Stream.java_class]).call(stdin.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling set_stdin(stdin)"
    end
    #  Set an interrupt handler, this handler is called when the command is interrupted, for instance user
    #  press <code>Ctrl-C</code>.
    # @yield the interrupt handler
    # @return [self]
    def interrupt_handler
      if block_given?
        @j_del.java_method(:interruptHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling interrupt_handler()"
    end
    #  Set a suspend handler, this handler is called when the command is suspended, for instance user
    #  press <code>Ctrl-Z</code>.
    # @yield the interrupt handler
    # @return [self]
    def suspend_handler
      if block_given?
        @j_del.java_method(:suspendHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling suspend_handler()"
    end
    #  Set a resume handler, this handler is called when the command is resumed, for instance user
    #  types <code>bg</code> or <code>fg</code> to resume the command.
    # @yield the interrupt handler
    # @return [self]
    def resume_handler
      if block_given?
        @j_del.java_method(:resumeHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling resume_handler()"
    end
    #  Set an end handler, this handler is called when the command is ended, for instance the command is running
    #  and the shell closes.
    # @yield the end handler
    # @return [self]
    def end_handler
      if block_given?
        @j_del.java_method(:endHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling end_handler()"
    end
    # @param [String] text 
    # @return [self]
    def write(text=nil)
      if text.class == String && !block_given?
        @j_del.java_method(:write, [Java::java.lang.String.java_class]).call(text)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling write(text)"
    end
    # @yield 
    # @return [self]
    def resizehandler
      if block_given?
        @j_del.java_method(:resizehandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling resizehandler()"
    end
    #  End the process.
    # @param [Fixnum] status the exit status.
    # @return [void]
    def end(status=nil)
      if !block_given? && status == nil
        return @j_del.java_method(:end, []).call()
      elsif status.class == Fixnum && !block_given?
        return @j_del.java_method(:end, [Java::int.java_class]).call(status)
      end
      raise ArgumentError, "Invalid arguments when calling end(status)"
    end
  end
end
