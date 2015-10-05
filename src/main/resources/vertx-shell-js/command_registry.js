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
var CommandRegistration = require('vertx-shell-js/command_registration');
var Command = require('vertx-shell-js/command');
var Completion = require('vertx-shell-js/completion');
var Vertx = require('vertx-js/vertx');
var CliToken = require('vertx-shell-js/cli_token');
var Process = require('vertx-shell-js/process');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JCommandRegistry = io.vertx.ext.shell.registry.CommandRegistry;

/**
 A registry that contains the commands known by a shell.

 @class
*/
var CommandRegistry = function(j_val) {

  var j_commandRegistry = j_val;
  var that = this;

  /**
   @return the current command registrations

   @public

   @return {Array.<CommandRegistration>}
   */
  this.registrations = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnListSetVertxGen(j_commandRegistry["registrations()"](), CommandRegistration);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Try to create a process from the command line tokens.

   @public
   @param line {Array.<CliToken>} the command line tokens 
   @param handler {function} the handler to be notified about process creation 
   */
  this.createProcess = function() {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_commandRegistry["createProcess(java.lang.String,io.vertx.core.Handler)"](__args[0], function(ar) {
      if (ar.succeeded()) {
        __args[1](utils.convReturnVertxGen(ar.result(), Process), null);
      } else {
        __args[1](null, ar.cause());
      }
    });
    }  else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0] instanceof Array && typeof __args[1] === 'function') {
      j_commandRegistry["createProcess(java.util.List,io.vertx.core.Handler)"](utils.convParamListVertxGen(__args[0]), function(ar) {
      if (ar.succeeded()) {
        __args[1](utils.convReturnVertxGen(ar.result(), Process), null);
      } else {
        __args[1](null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Perform completion, the completion argument will be notified of the completion progress.

   @public
   @param completion {Completion} the completion object 
   */
  this.complete = function(completion) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_commandRegistry["complete(io.vertx.ext.shell.cli.Completion)"](completion._jdel);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param command {Command} 
   @param doneHandler {function} 
   */
  this.registerCommand = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_commandRegistry["registerCommand(io.vertx.ext.shell.command.Command)"](__args[0]._jdel);
    }  else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'function') {
      j_commandRegistry["registerCommand(io.vertx.ext.shell.command.Command,io.vertx.core.Handler)"](__args[0]._jdel, function(ar) {
      if (ar.succeeded()) {
        __args[1](utils.convReturnVertxGen(ar.result(), CommandRegistration), null);
      } else {
        __args[1](null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param commandName {string} 
   @param doneHandler {function} 
   */
  this.unregisterCommand = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_commandRegistry["unregisterCommand(java.lang.String)"](__args[0]);
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_commandRegistry["unregisterCommand(java.lang.String,io.vertx.core.Handler)"](__args[0], function(ar) {
      if (ar.succeeded()) {
        __args[1](null, null);
      } else {
        __args[1](null, ar.cause());
      }
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_commandRegistry;
};

/**
 Get the registry for the Vert.x instance

 @memberof module:vertx-shell-js/command_registry
 @param vertx {Vertx} the vertx instance 
 @return {CommandRegistry} the registry
 */
CommandRegistry.get = function(vertx) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(JCommandRegistry["get(io.vertx.core.Vertx)"](vertx._jdel), CommandRegistry);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = CommandRegistry;