require 'vertx/vertx'
require 'vertx-web/router'
require 'vertx-shell/term'
require 'vertx-auth-common/auth_provider'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.term.TermServer
module VertxShell
  #  A server for terminal based applications.
  class TermServer
    # @private
    # @param j_del [::VertxShell::TermServer] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::TermServer] the underlying java delegate
    def j_del
      @j_del
    end
    @@j_api_type = Object.new
    def @@j_api_type.accept?(obj)
      obj.class == TermServer
    end
    def @@j_api_type.wrap(obj)
      TermServer.new(obj)
    end
    def @@j_api_type.unwrap(obj)
      obj.j_del
    end
    def self.j_api_type
      @@j_api_type
    end
    def self.j_class
      Java::IoVertxExtShellTerm::TermServer.java_class
    end
    #  Create a term server for the SSH protocol.
    # @param [::Vertx::Vertx] vertx the vertx instance
    # @param [Hash] options the ssh options
    # @return [::VertxShell::TermServer] the term server
    def self.create_ssh_term_server(vertx=nil,options=nil)
      if vertx.class.method_defined?(:j_del) && !block_given? && options == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellTerm::TermServer.java_method(:createSSHTermServer, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::VertxShell::TermServer)
      elsif vertx.class.method_defined?(:j_del) && options.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellTerm::TermServer.java_method(:createSSHTermServer, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtShellTerm::SSHTermOptions.java_class]).call(vertx.j_del,Java::IoVertxExtShellTerm::SSHTermOptions.new(::Vertx::Util::Utils.to_json_object(options))),::VertxShell::TermServer)
      end
      raise ArgumentError, "Invalid arguments when calling create_ssh_term_server(#{vertx},#{options})"
    end
    #  Create a term server for the Telnet protocol.
    # @param [::Vertx::Vertx] vertx the vertx instance
    # @param [Hash] options the term options
    # @return [::VertxShell::TermServer] the term server
    def self.create_telnet_term_server(vertx=nil,options=nil)
      if vertx.class.method_defined?(:j_del) && !block_given? && options == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellTerm::TermServer.java_method(:createTelnetTermServer, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::VertxShell::TermServer)
      elsif vertx.class.method_defined?(:j_del) && options.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellTerm::TermServer.java_method(:createTelnetTermServer, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtShellTerm::TelnetTermOptions.java_class]).call(vertx.j_del,Java::IoVertxExtShellTerm::TelnetTermOptions.new(::Vertx::Util::Utils.to_json_object(options))),::VertxShell::TermServer)
      end
      raise ArgumentError, "Invalid arguments when calling create_telnet_term_server(#{vertx},#{options})"
    end
    #  Create a term server for the HTTP protocol, using an existing router.
    # @overload createHttpTermServer(vertx)
    #   @param [::Vertx::Vertx] vertx the vertx instance
    # @overload createHttpTermServer(vertx,options)
    #   @param [::Vertx::Vertx] vertx the vertx instance
    #   @param [Hash] options the term options
    # @overload createHttpTermServer(vertx,router)
    #   @param [::Vertx::Vertx] vertx the vertx instance
    #   @param [::VertxWeb::Router] router the router
    # @overload createHttpTermServer(vertx,router,options)
    #   @param [::Vertx::Vertx] vertx the vertx instance
    #   @param [::VertxWeb::Router] router the router
    #   @param [Hash] options the term options
    # @return [::VertxShell::TermServer] the term server
    def self.create_http_term_server(param_1=nil,param_2=nil,param_3=nil)
      if param_1.class.method_defined?(:j_del) && !block_given? && param_2 == nil && param_3 == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellTerm::TermServer.java_method(:createHttpTermServer, [Java::IoVertxCore::Vertx.java_class]).call(param_1.j_del),::VertxShell::TermServer)
      elsif param_1.class.method_defined?(:j_del) && param_2.class == Hash && !block_given? && param_3 == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellTerm::TermServer.java_method(:createHttpTermServer, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtShellTerm::HttpTermOptions.java_class]).call(param_1.j_del,Java::IoVertxExtShellTerm::HttpTermOptions.new(::Vertx::Util::Utils.to_json_object(param_2))),::VertxShell::TermServer)
      elsif param_1.class.method_defined?(:j_del) && param_2.class.method_defined?(:j_del) && !block_given? && param_3 == nil
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellTerm::TermServer.java_method(:createHttpTermServer, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtWeb::Router.java_class]).call(param_1.j_del,param_2.j_del),::VertxShell::TermServer)
      elsif param_1.class.method_defined?(:j_del) && param_2.class.method_defined?(:j_del) && param_3.class == Hash && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellTerm::TermServer.java_method(:createHttpTermServer, [Java::IoVertxCore::Vertx.java_class,Java::IoVertxExtWeb::Router.java_class,Java::IoVertxExtShellTerm::HttpTermOptions.java_class]).call(param_1.j_del,param_2.j_del,Java::IoVertxExtShellTerm::HttpTermOptions.new(::Vertx::Util::Utils.to_json_object(param_3))),::VertxShell::TermServer)
      end
      raise ArgumentError, "Invalid arguments when calling create_http_term_server(#{param_1},#{param_2},#{param_3})"
    end
    #  Set the term handler that will receive incoming client connections. When a remote terminal connects
    #  the <code>handler</code> will be called with the {::VertxShell::Term} which can be used to interact with the remote
    #  terminal.
    # @yield the term handler
    # @return [self]
    def term_handler
      if block_given?
        @j_del.java_method(:termHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxShell::Term)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling term_handler()"
    end
    #  Set an auth provider to use, any provider configured in options will override this provider. This should be used
    #  when a custom auth provider should be used.
    # @param [::VertxAuthCommon::AuthProvider] provider the auth to use
    # @return [self]
    def auth_provider(provider=nil)
      if provider.class.method_defined?(:j_del) && !block_given?
        @j_del.java_method(:authProvider, [Java::IoVertxExtAuth::AuthProvider.java_class]).call(provider.j_del)
        return self
      end
      raise ArgumentError, "Invalid arguments when calling auth_provider(#{provider})"
    end
    #  Bind the term server, the {::VertxShell::TermServer#term_handler} must be set before.
    # @yield the listen handler
    # @return [self]
    def listen
      if !block_given?
        @j_del.java_method(:listen, []).call()
        return self
      elsif block_given?
        @j_del.java_method(:listen, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |ar| yield(ar.failed ? ar.cause : nil, ar.succeeded ? ::Vertx::Util::Utils.safe_create(ar.result,::VertxShell::TermServer) : nil) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling listen()"
    end
    #  The actual port the server is listening on. This is useful if you bound the server specifying 0 as port number
    #  signifying an ephemeral port
    # @return [Fixnum] the actual port the server is listening on.
    def actual_port
      if !block_given?
        return @j_del.java_method(:actualPort, []).call()
      end
      raise ArgumentError, "Invalid arguments when calling actual_port()"
    end
    #  Like {::VertxShell::TermServer#close} but supplying a handler that will be notified when close is complete.
    # @yield the handler to be notified when the term server is closed
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
