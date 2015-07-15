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

/** @module vertx-shell-js/command_manager */
var utils = require('vertx-js/util/utils');
var Command = require('vertx-shell-js/command');
var Completion = require('vertx-shell-js/completion');
var CliToken = require('vertx-shell-js/cli_token');
var Process = require('vertx-shell-js/process');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JCommandManager = io.vertx.ext.shell.command.CommandManager;

/**

 @class
*/
var CommandManager = function(j_val) {

  var j_commandManager = j_val;
  var that = this;

  /**

   @public
   @param line {Array.<CliToken>} 
   @param handler {function} 
   */
  this.createProcess = function() {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_commandManager["createProcess(java.lang.String,io.vertx.core.Handler)"](__args[0], function(ar) {
      if (ar.succeeded()) {
        __args[1](utils.convReturnVertxGen(ar.result(), Process), null);
      } else {
        __args[1](null, ar.cause());
      }
    });
    }  else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0] instanceof Array && typeof __args[1] === 'function') {
      j_commandManager["createProcess(java.util.List,io.vertx.core.Handler)"](utils.convParamListVertxGen(__args[0]), function(ar) {
      if (ar.succeeded()) {
        __args[1](utils.convReturnVertxGen(ar.result(), Process), null);
      } else {
        __args[1](null, ar.cause());
      }
    });
    } else utils.invalidArgs();
  };

  /**

   @public
   @param completion {Completion} 
   */
  this.complete = function(completion) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_commandManager["complete(io.vertx.ext.shell.cli.Completion)"](completion._jdel);
    } else utils.invalidArgs();
  };

  /**

   @public
   @param command {Command} 
   @param handler {function} 
   */
  this.registerCommand = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_commandManager["registerCommand(io.vertx.ext.shell.command.Command)"](__args[0]._jdel);
    }  else if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'function') {
      j_commandManager["registerCommand(io.vertx.ext.shell.command.Command,io.vertx.core.Handler)"](__args[0]._jdel, function(ar) {
      if (ar.succeeded()) {
        __args[1](null, null);
      } else {
        __args[1](null, ar.cause());
      }
    });
    } else utils.invalidArgs();
  };

  /**

   @public

   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_commandManager["close()"]();
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_commandManager;
};

/**

 @memberof module:vertx-shell-js/command_manager
 @param vertx {Vertx} 
 @return {CommandManager}
 */
CommandManager.create = function(vertx) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(JCommandManager["create(io.vertx.core.Vertx)"](vertx._jdel), CommandManager);
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = CommandManager;