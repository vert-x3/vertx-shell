require 'vertx-shell/execution'
require 'vertx-shell/option'
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
    # @param [String] name 
    # @return [::VertxShell::Command]
    def self.create(name=nil)
      if name.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCommand::Command.java_method(:create, [Java::java.lang.String.java_class]).call(name),::VertxShell::Command)
      end
      raise ArgumentError, "Invalid arguments when calling create(name)"
    end
    # @param [::VertxShell::Option] option 
    # @return [self]
    def add_option(option=nil)
      if option.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:addOption, [Java::IoVertxExtShellCommand::Option.java_class]).call(option.j_del)
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
    # @return [String]
    def name
      if !block_given?
        return @j_del.java_method(:name, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling name()"
    end
    # @yield 
    # @return [void]
    def set_execute_handler
      if block_given?
        return @j_del.java_method(:setExecuteHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxShell::Execution)) }))
      end
      raise ArgumentError, "Invalid arguments when calling set_execute_handler()"
    end
    # @return [void]
    def unregister
      if !block_given?
        return @j_del.java_method(:unregister, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling unregister()"
    end
  end
end
