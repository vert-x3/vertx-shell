/*
 * Copyright 2014 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

/** @module vertx-shell-js/term_server */
var utils = require('vertx-js/util/utils');
var Vertx = require('vertx-js/vertx');
var Router = require('vertx-web-js/router');
var Term = require('vertx-shell-js/term');
var AuthProvider = require('vertx-auth-common-js/auth_provider');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JTermServer = Java.type('io.vertx.ext.shell.term.TermServer');
var TelnetTermOptions = Java.type('io.vertx.ext.shell.term.TelnetTermOptions');
var HttpTermOptions = Java.type('io.vertx.ext.shell.term.HttpTermOptions');
var SSHTermOptions = Java.type('io.vertx.ext.shell.term.SSHTermOptions');

/**
 A server for terminal based applications.

 @class
*/
var TermServer = function(j_val) {

  var j_termServer = j_val;
  var that = this;

  /**
   Set the term handler that will receive incoming client connections. When a remote terminal connects
   the <code>handler</code> will be called with the {@link Term} which can be used to interact with the remote
   terminal.

   @public
   @param handler {function} the term handler 
   @return {TermServer} this object
   */
  this.termHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_termServer["termHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(Term, jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set an auth provider to use, any provider configured in options will override this provider. This should be used
   when a custom auth provider should be used.

   @public
   @param provider {AuthProvider} the auth to use 
   @return {TermServer} this object
   */
  this.authProvider = function(provider) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_termServer["authProvider(io.vertx.ext.auth.AuthProvider)"](provider._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Bind the term server, the {@link TermServer#termHandler} must be set before.

   @public
   @param listenHandler {function} the listen handler 
   @return {TermServer} this object
   */
  this.listen = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_termServer["listen()"]();
      return that;
    }  else if (__args.length === 1 && typeof __args[0] === 'function') {
      j_termServer["listen(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        __args[0](utils.convReturnVertxGen(TermServer, ar.result()), null);
      } else {
        __args[0](null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   The actual port the server is listening on. This is useful if you bound the server specifying 0 as port number
   signifying an ephemeral port

   @public

   @return {number} the actual port the server is listening on.
   */
  this.actualPort = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_termServer["actualPort()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Like {@link TermServer#close} but supplying a handler that will be notified when close is complete.

   @public
   @param completionHandler {function} the handler to be notified when the term server is closed 
   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_termServer["close()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'function') {
      j_termServer["close(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        __args[0](null, null);
      } else {
        __args[0](null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_termServer;
};

TermServer._jclass = utils.getJavaClass("io.vertx.ext.shell.term.TermServer");
TermServer._jtype = {
  accept: function(obj) {
    return TermServer._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(TermServer.prototype, {});
    TermServer.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
TermServer._create = function(jdel) {
  var obj = Object.create(TermServer.prototype, {});
  TermServer.apply(obj, arguments);
  return obj;
}
/**
 Create a term server for the SSH protocol.

 @memberof module:vertx-shell-js/term_server
 @param vertx {Vertx} the vertx instance 
 @param options {Object} the ssh options 
 @return {TermServer} the term server
 */
TermServer.createSSHTermServer = function() {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(TermServer, JTermServer["createSSHTermServer(io.vertx.core.Vertx)"](__args[0]._jdel));
  }else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && (typeof __args[1] === 'object' && __args[1] != null)) {
    return utils.convReturnVertxGen(TermServer, JTermServer["createSSHTermServer(io.vertx.core.Vertx,io.vertx.ext.shell.term.SSHTermOptions)"](__args[0]._jdel, __args[1] != null ? new SSHTermOptions(new JsonObject(Java.asJSONCompatible(__args[1]))) : null));
  } else throw new TypeError('function invoked with invalid arguments');
};

/**
 Create a term server for the Telnet protocol.

 @memberof module:vertx-shell-js/term_server
 @param vertx {Vertx} the vertx instance 
 @param options {Object} the term options 
 @return {TermServer} the term server
 */
TermServer.createTelnetTermServer = function() {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(TermServer, JTermServer["createTelnetTermServer(io.vertx.core.Vertx)"](__args[0]._jdel));
  }else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && (typeof __args[1] === 'object' && __args[1] != null)) {
    return utils.convReturnVertxGen(TermServer, JTermServer["createTelnetTermServer(io.vertx.core.Vertx,io.vertx.ext.shell.term.TelnetTermOptions)"](__args[0]._jdel, __args[1] != null ? new TelnetTermOptions(new JsonObject(Java.asJSONCompatible(__args[1]))) : null));
  } else throw new TypeError('function invoked with invalid arguments');
};

/**
 Create a term server for the HTTP protocol, using an existing router.

 @memberof module:vertx-shell-js/term_server
 @param vertx {Vertx} the vertx instance 
 @param router {Router} the router 
 @param options {Object} the term options 
 @return {TermServer} the term server
 */
TermServer.createHttpTermServer = function() {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(TermServer, JTermServer["createHttpTermServer(io.vertx.core.Vertx)"](__args[0]._jdel));
  }else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && (typeof __args[1] === 'object' && __args[1] != null)) {
    return utils.convReturnVertxGen(TermServer, JTermServer["createHttpTermServer(io.vertx.core.Vertx,io.vertx.ext.shell.term.HttpTermOptions)"](__args[0]._jdel, __args[1] != null ? new HttpTermOptions(new JsonObject(Java.asJSONCompatible(__args[1]))) : null));
  }else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object' && __args[1]._jdel) {
    return utils.convReturnVertxGen(TermServer, JTermServer["createHttpTermServer(io.vertx.core.Vertx,io.vertx.ext.web.Router)"](__args[0]._jdel, __args[1]._jdel));
  }else if (__args.length === 3 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object' && __args[1]._jdel && (typeof __args[2] === 'object' && __args[2] != null)) {
    return utils.convReturnVertxGen(TermServer, JTermServer["createHttpTermServer(io.vertx.core.Vertx,io.vertx.ext.web.Router,io.vertx.ext.shell.term.HttpTermOptions)"](__args[0]._jdel, __args[1]._jdel, __args[2] != null ? new HttpTermOptions(new JsonObject(Java.asJSONCompatible(__args[2]))) : null));
  } else throw new TypeError('function invoked with invalid arguments');
};

module.exports = TermServer;