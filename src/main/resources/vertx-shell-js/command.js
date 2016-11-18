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

/** @module vertx-shell-js/command */
var utils = require('vertx-js/util/utils');
var Completion = require('vertx-shell-js/completion');
var CLI = require('vertx-js/cli');
var CliToken = require('vertx-shell-js/cli_token');
var Process = require('vertx-shell-js/process');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JCommand = io.vertx.ext.shell.command.Command;

/**

 @class
*/
var Command = function(j_val) {

  var j_command = j_val;
  var that = this;

  /**

   @public

   @return {string} the command name
   */
  this.name = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_command["name()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {CLI} the command line interface, can be null
   */
  this.cli = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(CLI, j_command["cli()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Create a new process with the passed arguments.

   @public
   @param args {Array.<CliToken>} the process arguments 
   @return {Process} the process
   */
  this.createProcess = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(Process, j_command["createProcess()"]());
    }  else if (__args.length === 1 && typeof __args[0] === 'object' && __args[0] instanceof Array) {
      return utils.convReturnVertxGen(Process, j_command["createProcess(java.util.List)"](utils.convParamListVertxGen(__args[0])));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Perform command completion, when the command is done completing it should call 
   or  )} method to signal completion is done.

   @public
   @param completion {Completion} the completion object 
   */
  this.complete = function(completion) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_command["complete(io.vertx.ext.shell.cli.Completion)"](completion._jdel);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_command;
};

Command._jclass = utils.getJavaClass("io.vertx.ext.shell.command.Command");
Command._jtype = {
  accept: function(obj) {
    return Command._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(Command.prototype, {});
    Command.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
Command._create = function(jdel) {
  var obj = Object.create(Command.prototype, {});
  Command.apply(obj, arguments);
  return obj;
}
module.exports = Command;