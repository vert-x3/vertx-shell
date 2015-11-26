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

/** @module vertx-shell-js/shell_server */
var utils = require('vertx-js/util/utils');
var Shell = require('vertx-shell-js/shell');
var TermServer = require('vertx-shell-js/term_server');
var Vertx = require('vertx-js/vertx');
var CommandResolver = require('vertx-shell-js/command_resolver');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JShellServer = io.vertx.ext.shell.ShellServer;
var ShellServerOptions = io.vertx.ext.shell.ShellServerOptions;

/**
 The shell server.<p/>

 @class
*/
var ShellServer = function(j_val) {

  var j_shellServer = j_val;
  var that = this;

  /**
   Set the command resolver for this server.

   @public
   @param resolver {CommandResolver} the resolver 
   @return {ShellServer} a reference to this, so the API can be used fluently
   */
  this.commandResolver = function(resolver) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_shellServer["commandResolver(io.vertx.ext.shell.command.CommandResolver)"](resolver._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Register a term server to this shell server, the term server lifecycle methods are managed by this shell server.

   @public
   @param termServer {TermServer} the term server to add 
   @return {ShellServer} a reference to this, so the API can be used fluently
   */
  this.registerTermServer = function(termServer) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_shellServer["registerTermServer(io.vertx.ext.shell.term.TermServer)"](termServer._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Create a new shell, the returned shell should be closed explicitely.

   @public

   @return {Shell} the created shell
   */
  this.createShell = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_shellServer["createShell()"](), Shell);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Start the shell service, this is an asynchronous start.

   @public
   @param listenHandler {function} handler for getting notified when service is started 
   */
  this.listen = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_shellServer["listen()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'function') {
      j_shellServer["listen(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        __args[0](null, null);
      } else {
        __args[0](null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Close the shell server, this is an asynchronous close.

   @public
   @param completionHandler {function} handler for getting notified when service is stopped 
   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_shellServer["close()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'function') {
      j_shellServer["close(io.vertx.core.Handler)"](function(ar) {
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
  this._jdel = j_shellServer;
};

/**
 Create a new shell server with default options.

 @memberof module:vertx-shell-js/shell_server
 @param vertx {Vertx} the vertx 
 @param options {Object} the options 
 @return {ShellServer} the created shell server
 */
ShellServer.create = function() {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(JShellServer["create(io.vertx.core.Vertx)"](__args[0]._jdel), ShellServer);
  }else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && (typeof __args[1] === 'object' && __args[1] != null)) {
    return utils.convReturnVertxGen(JShellServer["create(io.vertx.core.Vertx,io.vertx.ext.shell.ShellServerOptions)"](__args[0]._jdel, __args[1] != null ? new ShellServerOptions(new JsonObject(JSON.stringify(__args[1]))) : null), ShellServer);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = ShellServer;