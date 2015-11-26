require 'vertx-shell/shell'
require 'vertx-shell/term_server'
require 'vertx/vertx'
require 'vertx-shell/command_resolver'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.ShellServer
module VertxShell
  #  The shell server.<p/>
  # 
  #  A shell server is associated with a collection of : the {::VertxShell::ShellServer#register_term_server}
  #  method registers a term server. Term servers life cycle are managed by this server.<p/>
  # 
  #  When a  receives an incoming connection, a  instance is created and
  #  associated with this connection.<p/>
  # 
  #  The {::VertxShell::ShellServer#create_shell} method can be used to create  instance for testing purposes.
  class ShellServer
    # @private
    # @param j_del [::VertxShell::ShellServer] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::ShellServer] the underlying java delegate
    def j_del
      @j_del
    end
    #  Create a new shell server.
    # @param [::Vertx::Vertx] vertx the vertx
    # @return [::VertxShell::ShellServer] the created shell server
    def self.create(vertx=nil)
      if vertx.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShell::ShellServer.java_method(:create, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::VertxShell::ShellServer)
      end
      raise ArgumentError, "Invalid arguments when calling create(vertx)"
    end
    #  Set the command resolver for this server.
    # @param [::VertxShell::CommandResolver] resolver the resolver
    # @return [self]
    def command_resolver(resolver=nil)
      if resolver.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:commandResolver, [Java::IoVertxExtShellCommand::CommandResolver.java_class]).call(resolver.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling command_resolver(resolver)"
    end
    #  Set the shell welcome message.
    # @param [String] msg the welcome message
    # @return [self]
    def set_welcome_message(msg=nil)
      if msg.class == String && !block_given?
        @j_del.java_method(:setWelcomeMessage, [Java::java.lang.String.java_class]).call(msg)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling set_welcome_message(msg)"
    end
    #  Register a term server to this shell server, the term server lifecycle methods are managed by this shell server.
    # @param [::VertxShell::TermServer] termServer the term server to add
    # @return [self]
    def register_term_server(termServer=nil)
      if termServer.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:registerTermServer, [Java::IoVertxExtShellTerm::TermServer.java_class]).call(termServer.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling register_term_server(termServer)"
    end
    #  Create a new shell, the returned shell should be closed explicitely.
    # @return [::VertxShell::Shell] the created shell
    def create_shell
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(@j_del.java_method(:createShell, []).call(),::VertxShell::Shell)
      end
      raise ArgumentError, "Invalid arguments when calling create_shell()"
    end
    #  Start the shell service, this is an asynchronous start.
    # @yield handler for getting notified when service is started
    # @return [void]
    def listen
      if !block_given?
        return @j_del.java_method(:listen, []).call()
      elsif block_given?
        return @j_del.java_method(:listen, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling listen()"
    end
    #  Close the shell server, this is an asynchronous close.
    # @yield handler for getting notified when service is stopped
    # @return [void]
    def close
      if !block_given?
        return @j_del.java_method(:close, []).call()
      elsif block_given?
        return @j_del.java_method(:close, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil) }))
      end
      raise ArgumentError, "Invalid arguments when calling close()"
    end
  end
end
