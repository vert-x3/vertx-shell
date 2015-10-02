require 'vertx-shell/command'
require 'vertx-shell/completion'
require 'vertx-shell/command_process'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.command.CommandBuilder
module VertxShell
  #  A build for Vert.x Shell command.
  class CommandBuilder
    # @private
    # @param j_del [::VertxShell::CommandBuilder] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::CommandBuilder] the underlying java delegate
    def j_del
      @j_del
    end
    #  Set the command process handler, the process handler is called when the command is executed.
    # @yield the process handler
    # @return [self]
    def process_handler
      if block_given?
        @j_del.java_method(:processHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxShell::CommandProcess)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling process_handler()"
    end
    #  Set the command completion handler, the completion handler when the user asks for contextual command line
    #  completion, usually hitting the <i>tab</i> key.
    # @yield the completion handler
    # @return [self]
    def completion_handler
      if block_given?
        @j_del.java_method(:completionHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxShell::Completion)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling completion_handler()"
    end
    #  @return the command
    # @return [::VertxShell::Command]
    def build
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:build, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling build()"
    end
  end
end
