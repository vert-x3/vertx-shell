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

/** @module vertx-shell-js/term */
var utils = require('vertx-js/util/utils');
var Completion = require('vertx-shell-js/completion');
var SignalHandler = require('vertx-shell-js/signal_handler');
var Tty = require('vertx-shell-js/tty');
var Session = require('vertx-shell-js/session');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JTerm = io.vertx.ext.shell.term.Term;

/**
 The terminal.

 @class
*/
var Term = function(j_val) {

  var j_term = j_val;
  var that = this;
  Tty.call(this, j_val);

  /**

   @public

   @return {string} the declared tty type, for instance , , etc... it can be null when the tty does not have declared its type.
   */
  this.type = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_term["type()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {number} the current width, i.e the number of rows or  if unknown
   */
  this.width = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_term["width()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {number} the current height, i.e the number of columns or  if unknown
   */
  this.height = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_term["height()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {Term}
   */
  this.resizehandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_term["resizehandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {Term}
   */
  this.stdinHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_term["stdinHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(jVal);
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param data {string} 
   @return {Term}
   */
  this.write = function(data) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_term["write(java.lang.String)"](data);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {number} the last time this term received input
   */
  this.lastAccessedTime = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_term["lastAccessedTime()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Echo some text in the terminal, escaped if necessary.<p/>

   @public
   @param text {string} the text to echo 
   @return {Term} a reference to this, so the API can be used fluently
   */
  this.echo = function(text) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_term["echo(java.lang.String)"](text);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Associate the term with a session.

   @public
   @param session {Session} the session to set 
   @return {Term} a reference to this, so the API can be used fluently
   */
  this.setSession = function(session) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      return utils.convReturnVertxGen(j_term["setSession(io.vertx.ext.shell.session.Session)"](session._jdel), Term);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set an interrupt signal handler on the term.

   @public
   @param handler {SignalHandler} the interrupt handler 
   @return {Term} a reference to this, so the API can be used fluently
   */
  this.interruptHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_term["interruptHandler(io.vertx.ext.shell.term.SignalHandler)"](handler._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a suspend signal handler on the term.

   @public
   @param handler {SignalHandler} the suspend handler 
   @return {Term} a reference to this, so the API can be used fluently
   */
  this.suspendHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_term["suspendHandler(io.vertx.ext.shell.term.SignalHandler)"](handler._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Prompt the user a line of text, providing a completion handler to handle user's completion.

   @public
   @param prompt {string} the displayed prompt 
   @param lineHandler {function} the line handler called with the line 
   @param completionHandler {function} the completion handler 
   */
  this.readline = function() {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_term["readline(java.lang.String,io.vertx.core.Handler)"](__args[0], function(jVal) {
      __args[1](jVal);
    });
    }  else if (__args.length === 3 && typeof __args[0] === 'string' && typeof __args[1] === 'function' && typeof __args[2] === 'function') {
      j_term["readline(java.lang.String,io.vertx.core.Handler,io.vertx.core.Handler)"](__args[0], function(jVal) {
      __args[1](jVal);
    }, function(jVal) {
      __args[2](utils.convReturnVertxGen(jVal, Completion));
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a handler that will be called when the terminal is closed.

   @public
   @param handler {function} the handler 
   @return {Term} a reference to this, so the API can be used fluently
   */
  this.closeHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_term["closeHandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Close the connection to terminal.

   @public

   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_term["close()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_term;
};

// We export the Constructor function
module.exports = Term;