require 'vertx-shell/completion'
require 'vertx-shell/signal_handler'
require 'vertx-shell/tty'
require 'vertx-shell/session'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.term.Term
module VertxShell
  #  The terminal.
  class Term < ::VertxShell::Tty
    # @private
    # @param j_del [::VertxShell::Term] the java delegate
    def initialize(j_del)
      super(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Term] the underlying java delegate
    def j_del
      @j_del
    end
    # @return [String] the declared tty type, for instance , , etc... it can be null when the tty does not have declared its type.
    def type
      if !block_given?
        return @j_del.java_method(:type, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling type()"
    end
    # @return [Fixnum] the current width, i.e the number of rows or  if unknown
    def width
      if !block_given?
        return @j_del.java_method(:width, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling width()"
    end
    # @return [Fixnum] the current height, i.e the number of columns or  if unknown
    def height
      if !block_given?
        return @j_del.java_method(:height, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling height()"
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
    # @yield 
    # @return [self]
    def stdin_handler
      if block_given?
        @j_del.java_method(:stdinHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(event) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling stdin_handler()"
    end
    # @param [String] data 
    # @return [self]
    def write(data=nil)
      if data.class == String && !block_given?
        @j_del.java_method(:write, [Java::java.lang.String.java_class]).call(data)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling write(data)"
    end
    # @return [Fixnum] the last time this term received input
    def last_accessed_time
      if !block_given?
        return @j_del.java_method(:lastAccessedTime, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling last_accessed_time()"
    end
    #  Echo some text in the terminal, escaped if necessary.<p/>
    # @param [String] text the text to echo
    # @return [self]
    def echo(text=nil)
      if text.class == String && !block_given?
        @j_del.java_method(:echo, [Java::java.lang.String.java_class]).call(text)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling echo(text)"
    end
    #  Associate the term with a session.
    # @param [::VertxShell::Session] session the session to set
    # @return [::VertxShell::Term] a reference to this, so the API can be used fluently
    def set_session(session=nil)
      if session.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:setSession, [Java::IoVertxExtShellSession::Session.java_class]).call(session.j_del),::VertxShell::Term)
      end
      raise ArgumentError, "Invalid arguments when calling set_session(session)"
    end
    #  Set an interrupt signal handler on the term.
    # @param [::VertxShell::SignalHandler] handler the interrupt handler
    # @return [self]
    def interrupt_handler(handler=nil)
      if handler.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:interruptHandler, [Java::IoVertxExtShellTerm::SignalHandler.java_class]).call(handler.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling interrupt_handler(handler)"
    end
    #  Set a suspend signal handler on the term.
    # @param [::VertxShell::SignalHandler] handler the suspend handler
    # @return [self]
    def suspend_handler(handler=nil)
      if handler.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:suspendHandler, [Java::IoVertxExtShellTerm::SignalHandler.java_class]).call(handler.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling suspend_handler(handler)"
    end
    #  Prompt the user a line of text, providing a completion handler to handle user's completion.
    # @param [String] prompt the displayed prompt
    # @param [Proc] lineHandler the line handler called with the line
    # @yield the completion handler
    # @return [void]
    def readline(prompt=nil,lineHandler=nil)
      if prompt.class == String && block_given? && lineHandler == nil
        return @j_del.java_method(:readline, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class]).call(prompt,(Proc.new { |event| yield(event) }))
      elsif prompt.class == String && lineHandler.class == Proc && block_given?
        return @j_del.java_method(:readline, [Java::java.lang.String.java_class,Java::IoVertxCore::Handler.java_class,Java::IoVertxCore::Handler.java_class]).call(prompt,(Proc.new { |event| lineHandler.call(event) }),(Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxShell::Completion)) }))
      end
      raise ArgumentError, "Invalid arguments when calling readline(prompt,lineHandler)"
    end
    #  Set a handler that will be called when the terminal is closed.
    # @yield the handler
    # @return [self]
    def close_handler
      if block_given?
        @j_del.java_method(:closeHandler, [Java::IoVertxCore::Handler.java_class]).call(Proc.new { yield })
        return self
      end
      raise ArgumentError, "Invalid arguments when calling close_handler()"
    end
    #  Close the connection to terminal.
    # @return [void]
    def close
      if !block_given?
        return @j_del.java_method(:close, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
  end
end
