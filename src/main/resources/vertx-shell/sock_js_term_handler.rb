require 'vertx-web/sock_js_socket'
require 'vertx/buffer'
require 'vertx/vertx'
require 'vertx-shell/term'
require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.term.SockJSTermHandler
module VertxShell
  #  @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
  class SockJSTermHandler
    # @private
    # @param j_del [::VertxShell::SockJSTermHandler] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::SockJSTermHandler] the underlying java delegate
    def j_del
      @j_del
    end
    # @param [::VertxWeb::SockJSSocket] arg0 
    # @return [void]
    def handle(arg0=nil)
      if arg0.class.method_defined?(:j_del) && !block_given?
        return @j_del.java_method(:handle, [Java::IoVertxExtWebHandlerSockjs::SockJSSocket.java_class]).call(arg0.j_del)
      end
      raise ArgumentError, "Invalid arguments when calling handle(arg0)"
    end
    # @return [::Vertx::Buffer]
    def self.default_vertx_term_script_resource
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellTerm::SockJSTermHandler.java_method(:defaultVertxTermScriptResource, []).call(),::Vertx::Buffer)
      end
      raise ArgumentError, "Invalid arguments when calling default_vertx_term_script_resource()"
    end
    # @return [::Vertx::Buffer]
    def self.default_term_script_resource
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellTerm::SockJSTermHandler.java_method(:defaultTermScriptResource, []).call(),::Vertx::Buffer)
      end
      raise ArgumentError, "Invalid arguments when calling default_term_script_resource()"
    end
    # @return [::Vertx::Buffer]
    def self.default_term_markup_resource
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellTerm::SockJSTermHandler.java_method(:defaultTermMarkupResource, []).call(),::Vertx::Buffer)
      end
      raise ArgumentError, "Invalid arguments when calling default_term_markup_resource()"
    end
    # @param [::Vertx::Vertx] vertx 
    # @return [::VertxShell::SockJSTermHandler]
    def self.create(vertx=nil)
      if vertx.class.method_defined?(:j_del) && !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellTerm::SockJSTermHandler.java_method(:create, [Java::IoVertxCore::Vertx.java_class]).call(vertx.j_del),::VertxShell::SockJSTermHandler)
      end
      raise ArgumentError, "Invalid arguments when calling create(vertx)"
    end
    # @yield 
    # @return [self]
    def term_handler
      if block_given?
        @j_del.java_method(:termHandler, [Java::IoVertxCore::Handler.java_class]).call((Proc.new { |event| yield(::Vertx::Util::Utils.safe_create(event,::VertxShell::Term)) }))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling term_handler()"
    end
  end
end
