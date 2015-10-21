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

/** @module vertx-shell-js/command_registration */
var utils = require('vertx-js/util/utils');
var Command = require('vertx-shell-js/command');
var Completion = require('vertx-shell-js/completion');
var CliToken = require('vertx-shell-js/cli_token');
var Process = require('vertx-shell-js/process');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JCommandRegistration = io.vertx.ext.shell.registry.CommandRegistration;

/**

 @class
*/
var CommandRegistration = function(j_val) {

  var j_commandRegistration = j_val;
  var that = this;

  /**
   @return the registered command.

   @public

   @return {Command}
   */
  this.command = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_commandRegistration["command()"](), Command);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Complete the command for the given completion.

   @public
   @param completion {Completion} the completion 
   */
  this.complete = function(completion) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_commandRegistration["complete(io.vertx.ext.shell.cli.Completion)"](completion._jdel);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Create a new process with the passed arguments.

   @public
   @param args {Array.<CliToken>} the process arguments 
   @return {Process} the process
   */
  this.createProcess = function(args) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0] instanceof Array) {
      return utils.convReturnVertxGen(j_commandRegistration["createProcess(java.util.List)"](utils.convParamListVertxGen(args)), Process);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Unregister the current command

   @public
   @param handler {function} 
   */
  this.unregister = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_commandRegistration["unregister()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'function') {
      j_commandRegistration["unregister(io.vertx.core.Handler)"](function(ar) {
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
  this._jdel = j_commandRegistration;
};

// We export the Constructor function
module.exports = CommandRegistration;