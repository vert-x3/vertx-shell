require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.completion.Entry
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class Entry
    # @private
    # @param j_del [::VertxShell::Entry] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Entry] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [true,false] terminal 
    # @param [String] value 
    # @return [::VertxShell::Entry]
    def self.entry(terminal=nil,value=nil)
      if (terminal.class == TrueClass || terminal.class == FalseClass) && value.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCompletion::Entry.java_method(:entry, [Java::boolean.java_class,Java::java.lang.String.java_class]).call(terminal,value),::VertxShell::Entry)
      end
      raise ArgumentError, "Invalid arguments when calling entry(terminal,value)"
    end
    # @param [String] value 
    # @return [::VertxShell::Entry]
    def self.terminal_entry(value=nil)
      if value.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCompletion::Entry.java_method(:terminalEntry, [Java::java.lang.String.java_class]).call(value),::VertxShell::Entry)
      end
      raise ArgumentError, "Invalid arguments when calling terminal_entry(value)"
    end
    # @param [String] value 
    # @return [::VertxShell::Entry]
    def self.non_terminal_entry(value=nil)
      if value.class == String && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellCompletion::Entry.java_method(:nonTerminalEntry, [Java::java.lang.String.java_class]).call(value),::VertxShell::Entry)
      end
      raise ArgumentError, "Invalid arguments when calling non_terminal_entry(value)"
    end
    # @return [String]
    def value
      if !block_given?
        return @j_del.java_method(:value, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling value()"
    end
    # @return [true,false]
    def terminal?
      if !block_given?
        return @j_del.java_method(:isTerminal, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling terminal?()"
    end
  end
end
