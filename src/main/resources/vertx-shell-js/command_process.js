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
var Session = require('vertx-shell-js/session');
var Tty = require('vertx-shell-js/tty');
var CliToken = require('vertx-shell-js/cli_token');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JCommandProcess = io.vertx.ext.shell.command.CommandProcess;

/**

 @class
*/
var CommandProcess = function(j_val) {

  var j_commandProcess = j_val;
  var that = this;
  Tty.call(this, j_val);

  /**
   @return the current Vert.x instance

   @public

   @return {Vertx}
   */
  this.vertx = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_commandProcess["vertx()"](), Vertx);
    } else utils.invalidArgs();
  };

  /**
   @return the unparsed arguments tokens

   @public

   @return {Array.<CliToken>}
   */
  this.argsTokens = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnListSetVertxGen(j_commandProcess["argsTokens()"](), CliToken);
    } else utils.invalidArgs();
  };

  /**
   @return the actual string arguments of the command

   @public

   @return {Array.<string>}
   */
  this.args = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_commandProcess["args()"]();
    } else utils.invalidArgs();
  };

  /**
   @return the shell session

   @public

   @return {Session}
   */
  this.session = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_commandProcess["session()"](), Session);
    } else utils.invalidArgs();
  };

  /**

   @public
   @param stdin {function} 
   @return {CommandProcess}
   */
  this.setStdin = function(stdin) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_commandProcess["setStdin(io.vertx.core.Handler)"](function(jVal) {
      stdin(jVal);
    });
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