require 'vertx-shell/completion'
require 'vertx-shell/command_process'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.Command
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class Command
    # @private
    # @param j_del [::VertxShell::Command] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Command] the underlying java delegate
    def j_del
      @j_del
    end
    #  Create a new commmand.
    # @param [String] name the command name
    # @return [::VertxShell::Command] the command object
    def self.command(name=nil)
      if name.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommand::Command.java_method(:command, [Java::java.lang.String.java_class]).call(name),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling command(name)"
    end
    # @return [String]
    def name
      if !block_given?
        return @j_del.java_method(:name, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling name()"
    end
    # @yield 
    # @return [self]
    def process_handler
      if block_given?
        @j_del.java_method(:processHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxShell::CommandProcess)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling process_handler()"
    end
    # @yield 
    # @return [self]
    def complete_handler
      if block_given?
        @j_del.java_method(:completeHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxShell::Completion)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling complete_handler()"
    end
  end
end
