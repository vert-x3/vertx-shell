require 'vertx-shell/command'
require 'vertx-shell/completion'
require 'vertx-shell/cli_token'
require 'vertx-shell/process'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.registry.CommandRegistration
module VertxShell
  #  A registration of a command in the {::VertxShell::CommandRegistry}
  class CommandRegistration
    # @private
    # @param j_del [::VertxShell::CommandRegistration] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::CommandRegistration] the underlying java delegate
    def j_del
      @j_del
    end
    #  @return the registered command.
    # @return [::VertxShell::Command]
    def command
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:command, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling command()"
    end
    #  Complete the command for the given completion.
    # @param [::VertxShell::Completion] completion the completion
    # @return [void]
    def complete(completion=nil)
      if completion.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:complete, [Java::IoVertxExtShellCli::Completion.java_class]).call(completion.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling complete(completion)"
    end
    #  Create a new process with the passed arguments.
    # @param [Array<::VertxShell::CliToken>] args the process arguments
    # @return [::VertxShell::Process] the process
    def create_process(args=nil)
      if args.class == Array && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:createProcess, [Java::JavaUtil::List.java_class]).call(args.map { |element| element.j_del }),::VertxShell::Process)
      end
      raise ArgumentError, "Invalid arguments when calling create_process(args)"
    end
    #  Unregister the current command
    # @yield 
    # @return [void]
    def unregister
      if !block_given?
        return @j_del.java_method(:unregister, []).call()
      elsif block_given?
        return @j_del.java_method(:unregister, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling unregister()"
    end
  end
end
