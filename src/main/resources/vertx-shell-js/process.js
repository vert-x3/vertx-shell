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

/** @module vertx-shell-js/process */
var utils = require('vertx-js/util/utils');
var Tty = require('vertx-shell-js/tty');
var Session = require('vertx-shell-js/session');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JProcess = io.vertx.ext.shell.system.Process;

/**
 A process managed by the shell.

 @class
*/
var Process = function(j_val) {

  var j_process = j_val;
  var that = this;

  /**
   Set the process tty.

   @public
   @param tty {Tty} the process tty 
   @return {Process} this object
   */
  this.setTty = function(tty) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_process["setTty(io.vertx.ext.shell.term.Tty)"](tty._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the process tty

   @public

   @return {Tty}
   */
  this.getTty = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_process["getTty()"](), Tty);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set the process session

   @public
   @param session {Session} the process session 
   @return {Process} this object
   */
  this.setSession = function(session) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_process["setSession(io.vertx.ext.shell.session.Session)"](session._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the process session

   @public

   @return {Session}
   */
  this.getSession = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_process["getSession()"](), Session);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Execute the process.

   @public
   @param endHandler {function} the end handler 
   */
  this.execute = function(endHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_process["execute(io.vertx.core.Handler)"](function(jVal) {
      endHandler(jVal);
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Attempt to interrupt the process.

   @public

   @return {boolean} true if the process caught the signal
   */
  this.interrupt = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_process["interrupt()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Suspend the process.

   @public

   */
  this.resume = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_process["resume()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Resume the process.

   @public

   */
  this.suspend = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_process["suspend()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_process;
};

// We export the Constructor function
module.exports = Process;