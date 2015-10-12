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
var CommandLine = require('vertx-js/command_line');
var Stream = require('vertx-shell-js/stream');
var Tty = require('vertx-shell-js/tty');
var Vertx = require('vertx-js/vertx');
var CliToken = require('vertx-shell-js/cli_token');
var Session = require('vertx-shell-js/session');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JCommandProcess = io.vertx.ext.shell.command.CommandProcess;

/**
 The command process provides interaction with the process of the command provided by Vert.x Shell.

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
    } else throw new TypeError('function invoked with invalid arguments');
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
    } else throw new TypeError('function invoked with invalid arguments');
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
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the command line object or null

   @public

   @return {CommandLine}
   */
  this.commandLine = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_commandProcess["commandLine()"](), CommandLine);
    } else throw new TypeError('function invoked with invalid arguments');
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
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param stdin {Stream} 
   @return {CommandProcess}
   */
  this.setStdin = function(stdin) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_commandProcess["setStdin(io.vertx.ext.shell.io.Stream)"](stdin._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param eventType {Object} 
   @param handler {function} 
   @return {CommandProcess}
   */
  this.eventHandler = function(eventType, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_commandProcess["eventHandler(io.vertx.ext.shell.io.EventType,io.vertx.core.Handler)"](io.vertx.ext.shell.io.EventType.valueOf(__args[0]), handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
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
    } else throw new TypeError('function invoked with invalid arguments');
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
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_commandProcess;
};

// We export the Constructor function
module.exports = CommandProcess;