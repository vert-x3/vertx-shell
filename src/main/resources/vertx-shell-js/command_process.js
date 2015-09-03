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

/** @module vertx-shell-js/command_process */
var utils = require('vertx-js/util/utils');
var CliToken = require('vertx-shell-js/cli_token');
var Stream = require('vertx-shell-js/stream');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JCommandProcess = io.vertx.ext.shell.command.CommandProcess;

/**

 @class
*/
var CommandProcess = function(j_val) {

  var j_commandProcess = j_val;
  var that = this;

  /**

   @public

   @return {Array.<CliToken>}
   */
  this.args = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnListSetVertxGen(j_commandProcess["args()"](), CliToken);
    } else utils.invalidArgs();
  };

  /**

   @public

   @return {number}
   */
  this.width = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_commandProcess["width()"]();
    } else utils.invalidArgs();
  };

  /**

   @public

   @return {number}
   */
  this.height = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_commandProcess["height()"]();
    } else utils.invalidArgs();
  };

  /**

   @public
   @param stdin {Stream} 
   @return {CommandProcess}
   */
  this.setStdin = function(stdin) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_commandProcess["setStdin(io.vertx.ext.shell.Stream)"](stdin._jdel);
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param event {string} 
   @param handler {function} 
   @return {CommandProcess}
   */
  this.eventHandler = function(event, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_commandProcess["eventHandler(java.lang.String,io.vertx.core.Handler)"](event, handler);
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public

   @return {Stream}
   */
  this.stdout = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_commandProcess["stdout()"](), Stream);
    } else utils.invalidArgs();
  };

  /**

   @public
   @param text {string} 
   @return {CommandProcess}
   */
  this.write = function(text) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_commandProcess["write(java.lang.String)"](text);
      return that;
    } else utils.invalidArgs();
  };

  /**
   End the process.

   @public
   @param status {number} the exit status. 
   */
  this.end = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_commandProcess["end()"]();
    }  else if (__args.length === 1 && typeof __args[0] ==='number') {
      j_commandProcess["end(int)"](__args[0]);
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_commandProcess;
};

// We export the Constructor function
module.exports = CommandProcess;