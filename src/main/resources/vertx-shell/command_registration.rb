require 'vertx-shell/command'
require 'vertx-shell/completion'
require 'vertx-shell/cli_token'
require 'vertx-shell/process'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.registry.CommandRegistration
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
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
    # @return [::VertxShell::Command]
    def command
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:command, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling command()"
    end
    # @param [::VertxShell::Completion] completion 
    # @return [void]
    def complete(completion=nil)
      if completion.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:complete, [Java::IoVertxExtShellCli::Completion.java_class]).call(completion.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling complete(completion)"
    end
    # @param [Array<::VertxShell::CliToken>] args 
    # @return [::VertxShell::Process]
    def create_process(args=nil)
      if args.class == Array && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:createProcess, [Java::JavaUtil::List.java_class]).call(args.map { |element| element.j_del }),::VertxShell::Process)
      end
      raise ArgumentError, "Invalid arguments when calling create_process(args)"
    end
  end
end
