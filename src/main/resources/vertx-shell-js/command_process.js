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
var Vertx = require('vertx-js/vertx');
var CliToken = require('vertx-shell-js/cli_token');
var Tty = require('vertx-shell-js/tty');
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

   @public

   @return {string} the declared tty type, for instance , , etc... it can be null when the tty does not have declared its type.
   */
  this.type = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_commandProcess["type()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {number} the current width, i.e the number of rows or  if unknown
   */
  this.width = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_commandProcess["width()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {number} the current height, i.e the number of columns or  if unknown
   */
  this.height = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_commandProcess["height()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Vertx} the current Vert.x instance
   */
  this.vertx = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(Vertx, j_commandProcess["vertx()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Array.<CliToken>} the unparsed arguments tokens
   */
  this.argsTokens = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnListSetVertxGen(j_commandProcess["argsTokens()"](), CliToken);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Array.<string>} the actual string arguments of the command
   */
  this.args = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_commandProcess["args()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {CommandLine} the command line object or null
   */
  this.commandLine = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(CommandLine, j_commandProcess["commandLine()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Session} the shell session
   */
  this.session = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(Session, j_commandProcess["session()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {boolean} true if the command is running in foreground
   */
  this.isForeground = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_commandProcess["isForeground()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {CommandProcess}
   */
  this.stdinHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_commandProcess["stdinHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(jVal);
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set an interrupt handler, this handler is called when the command is interrupted, for instance user
   press <code>Ctrl-C</code>.

   @public
   @param handler {function} the interrupt handler 
   @return {CommandProcess} this command
   */
  this.interruptHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_commandProcess["interruptHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a suspend handler, this handler is called when the command is suspended, for instance user
   press <code>Ctrl-Z</code>.

   @public
   @param handler {function} the interrupt handler 
   @return {CommandProcess} this command
   */
  this.suspendHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_commandProcess["suspendHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a resume handler, this handler is called when the command is resumed, for instance user
   types <code>bg</code> or <code>fg</code> to resume the command.

   @public
   @param handler {function} the interrupt handler 
   @return {CommandProcess} this command
   */
  this.resumeHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_commandProcess["resumeHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set an end handler, this handler is called when the command is ended, for instance the command is running
   and the shell closes.

   @public
   @param handler {function} the end handler 
   @return {CommandProcess} a reference to this, so the API can be used fluently
   */
  this.endHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_commandProcess["endHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Write some text to the standard output.

   @public
   @param data {string} the text 
   @return {CommandProcess} a reference to this, so the API can be used fluently
   */
  this.write = function(data) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_commandProcess["write(java.lang.String)"](data);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a background handler, this handler is called when the command is running and put to background.

   @public
   @param handler {function} the background handler 
   @return {CommandProcess} this command
   */
  this.backgroundHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_commandProcess["backgroundHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a foreground handler, this handler is called when the command is running and put to foreground.

   @public
   @param handler {function} the foreground handler 
   @return {CommandProcess} this command
   */
  this.foregroundHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_commandProcess["foregroundHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {CommandProcess}
   */
  this.resizehandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_commandProcess["resizehandler(io.vertx.core.Handler)"](handler);
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

CommandProcess._jclass = utils.getJavaClass("io.vertx.ext.shell.command.CommandProcess");
CommandProcess._jtype = {
  accept: function(obj) {
    return CommandProcess._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(CommandProcess.prototype, {});
    CommandProcess.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
CommandProcess._create = function(jdel) {
  var obj = Object.create(CommandProcess.prototype, {});
  CommandProcess.apply(obj, arguments);
  return obj;
}
module.exports = CommandProcess;