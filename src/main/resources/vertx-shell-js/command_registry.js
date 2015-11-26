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

/** @module vertx-shell-js/command_registry */
var utils = require('vertx-js/util/utils');
var Command = require('vertx-shell-js/command');
var Vertx = require('vertx-js/vertx');
var CommandResolver = require('vertx-shell-js/command_resolver');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JCommandRegistry = io.vertx.ext.shell.command.CommandRegistry;

/**
 A registry that contains the commands known by a shell.<p/>

 It is a mutable command resolver.


 @class
*/
var CommandRegistry = function(j_val) {

  var j_commandRegistry = j_val;
  var that = this;
  CommandResolver.call(this, j_val);

  /**

   @public
   @param command {Command} 
   @param completionHandler {function} 
   @return {CommandRegistry}
   */
  this.registerCommand = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_commandRegistry["registerCommand(io.vertx.ext.shell.command.Command)"](__args[0]._jdel);
      return that;
    }  else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'function') {
      j_commandRegistry["registerCommand(io.vertx.ext.shell.command.Command,io.vertx.core.Handler)"](__args[0]._jdel, function(ar) {
      if (ar.succeeded()) {
        __args[1](utils.convReturnVertxGen(ar.result(), Command), null);
      } else {
        __args[1](null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param commands {Array.<Command>} 
   @param completionHandler {function} 
   @return {CommandRegistry}
   */
  this.registerCommands = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0] instanceof Array) {
      j_commandRegistry["registerCommands(java.util.List)"](utils.convParamListVertxGen(__args[0]));
      return that;
    }  else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0] instanceof Array && typeof __args[1] === 'function') {
      j_commandRegistry["registerCommands(java.util.List,io.vertx.core.Handler)"](utils.convParamListVertxGen(__args[0]), function(ar) {
      if (ar.succeeded()) {
        __args[1](utils.convReturnListSetVertxGen(ar.result(), Command), null);
      } else {
        __args[1](null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param commandName {string} 
   @param completionHandler {function} 
   @return {CommandRegistry}
   */
  this.unregisterCommand = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_commandRegistry["unregisterCommand(java.lang.String)"](__args[0]);
      return that;
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_commandRegistry["unregisterCommand(java.lang.String,io.vertx.core.Handler)"](__args[0], function(ar) {
      if (ar.succeeded()) {
        __args[1](null, null);
      } else {
        __args[1](null, ar.cause());
      }
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_commandRegistry;
};

/**
 Get the shared registry for the Vert.x instance.

 @memberof module:vertx-shell-js/command_registry
 @param vertx {Vertx} the vertx instance 
 @return {CommandRegistry} the shared registry
 */
CommandRegistry.getShared = function(vertx) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(JCommandRegistry["getShared(io.vertx.core.Vertx)"](vertx._jdel), CommandRegistry);
  } else throw new TypeError('function invoked with invalid arguments');
};

/**
 Create a new registry.

 @memberof module:vertx-shell-js/command_registry
 @param vertx {Vertx} the vertx instance 
 @return {CommandRegistry} the created registry
 */
CommandRegistry.create = function(vertx) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(JCommandRegistry["create(io.vertx.core.Vertx)"](vertx._jdel), CommandRegistry);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = CommandRegistry;