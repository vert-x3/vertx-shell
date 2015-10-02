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
var CommandBuilder = require('vertx-shell-js/command_builder');
var CommandProcess = require('vertx-shell-js/command_process');

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
   @return the command name

   @public

   @return {string}
   */
  this.name = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_command["name()"]();
    } else utils.invalidArgs();
  };

  /**
   @return the command line interface, can be null

   @public

   @return {CLI}
   */
  this.cli = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_command["cli()"](), CLI);
    } else utils.invalidArgs();
  };

  /**
   Process the command, when the command is done processing it should call the {@link CommandProcess#end} method.

   @public
   @param process {CommandProcess} the command process 
   */
  this.process = function(process) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_command["process(io.vertx.ext.shell.command.CommandProcess)"](process._jdel);
    } else utils.invalidArgs();
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
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_command;
};

/**
 Create a new commmand with its {@link CLI} descriptor. This command can then retrieve the parsed
 {@link CommandProcess#commandLine} when it executes to know get the command arguments and options.

 @memberof module:vertx-shell-js/command
 @param cli {CLI} the cli to use 
 @return {CommandBuilder} the command
 */
Command.builder = function() {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'string') {
    return utils.convReturnVertxGen(JCommand["builder(java.lang.String)"](__args[0]), CommandBuilder);
  }else if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(JCommand["builder(io.vertx.core.cli.CLI)"](__args[0]._jdel), CommandBuilder);
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = Command;