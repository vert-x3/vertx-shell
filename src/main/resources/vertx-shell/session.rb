require 'vertx/util/utils.rb'
# Generated from io.vertx.ext.shell.session.Session
module VertxShell
  #  A shell session.
  class Session
    # @private
    # @param j_del [::VertxShell::Session] the java delegate
    def initialize(j_del)
      @j_del = j_del
    end
    # @private
    # @return [::VertxShell::Session] the underlying java delegate
    def j_del
      @j_del
    end
    #  Create a new empty session.
    # @return [::VertxShell::Session] the created session
    def self.create
      if !block_given?
        return ::Vertx::Util::Utils.safe_create(Java::IoVertxExtShellSession::Session.java_method(:create, []).call(),::VertxShell::Session)
      end
      raise ArgumentError, "Invalid arguments when calling create()"
    end
    #  Put some data in a session
    # @param [String] key the key for the data
    # @param [Object] obj the data
    # @return [self]
    def put(key=nil,obj=nil)
      if key.class == String && (obj.class == String  || obj.class == Hash || obj.class == Array || obj.class == NilClass || obj.class == TrueClass || obj.class == FalseClass || obj.class == Fixnum || obj.class == Float) && !block_given?
        @j_del.java_method(:put, [Java::java.lang.String.java_class,Java::java.lang.Object.java_class]).call(key,::Vertx::Util::Utils.to_object(obj))
        return self
      end
      raise ArgumentError, "Invalid arguments when calling put(key,obj)"
    end
    #  Get some data from the session
    # @param [String] key the key of the data
    # @return [Object] the data
    def get(key=nil)
      if key.class == String && !block_given?
        return ::Vertx::Util::Utils.from_object(@j_del.java_method(:get, [Java::java.lang.String.java_class]).call(key))
      end
      raise ArgumentError, "Invalid arguments when calling get(key)"
    end
    #  Remove some data from the session
    # @param [String] key the key of the data
    # @return [Object] the data that was there or null if none there
    def remove(key=nil)
      if key.class == String && !block_given?
        return ::Vertx::Util::Utils.from_object(@j_del.java_method(:remove, [Java::java.lang.String.java_class]).call(key))
      end
      raise ArgumentError, "Invalid arguments when calling remove(key)"
    end
  end
end
