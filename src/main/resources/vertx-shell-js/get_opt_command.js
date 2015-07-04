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

/** @module vertx-shell-js/get_opt_command */
var utils = require('vertx-js/util/utils');
var Command = require('vertx-shell-js/command');
var GetOptCommandProcess = require('vertx-shell-js/get_opt_command_process');
var Option = require('vertx-shell-js/option');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JGetOptCommand = io.vertx.ext.shell.getopt.GetOptCommand;

/**

 @class
*/
var GetOptCommand = function(j_val) {

  var j_getOptCommand = j_val;
  var that = this;

  /**

   @public
   @param handler {function} 
   */
  this.processHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_getOptCommand["processHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(jVal, GetOptCommandProcess));
    });
    } else utils.invalidArgs();
  };

  /**

   @public
   @param option {Option} 
   @return {GetOptCommand}
   */
  this.addOption = function(option) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_getOptCommand["addOption(io.vertx.ext.shell.getopt.Option)"](option._jdel);
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param name {string} 
   @return {Option}
   */
  this.getOption = function(name) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_getOptCommand["getOption(java.lang.String)"](name), Option);
    } else utils.invalidArgs();
  };

  /**

   @public

   @return {Command}
   */
  this.build = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_getOptCommand["build()"](), Command);
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_getOptCommand;
};

/**

 @memberof module:vertx-shell-js/get_opt_command
 @param name {string} 
 @return {GetOptCommand}
 */
GetOptCommand.create = function(name) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'string') {
    return utils.convReturnVertxGen(JGetOptCommand["create(java.lang.String)"](name), GetOptCommand);
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = GetOptCommand;