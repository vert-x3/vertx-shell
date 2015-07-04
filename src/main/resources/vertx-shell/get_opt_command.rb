require 'vertx-shell/command'
require 'vertx-shell/get_opt_command_process'
require 'vertx-shell/option'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.getopt.GetOptCommand
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class GetOptCommand
    # @private
    # @param j_del [::VertxShell::GetOptCommand] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::GetOptCommand] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [String] name 
    # @return [::VertxShell::GetOptCommand]
    def self.create(name=nil)
      if name.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellGetopt::GetOptCommand.java_method(:create, [Java::java.lang.String.java_class]).call(name),::VertxShell::GetOptCommand)
      end
      raise ArgumentError, "Invalid arguments when calling create(name)"
    end
    # @yield 
    # @return [void]
    def process_handler
      if block_given?
        return @j_del.java_method(:processHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxShell::GetOptCommandProcess)) }))
      end
      raise ArgumentError, "Invalid arguments when calling process_handler()"
    end
    # @param [::VertxShell::Option] option 
    # @return [self]
    def add_option(option=nil)
      if option.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:addOption, [Java::IoVertxExtShellGetopt::Option.java_class]).call(option.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling add_option(option)"
    end
    # @param [String] name 
    # @return [::VertxShell::Option]
    def get_option(name=nil)
      if name.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:getOption, [Java::java.lang.String.java_class]).call(name),::VertxShell::Option)
      end
      raise ArgumentError, "Invalid arguments when calling get_option(name)"
    end
    # @return [::VertxShell::Command]
    def build
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:build, []).call(),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling build()"
    end
  end
end
