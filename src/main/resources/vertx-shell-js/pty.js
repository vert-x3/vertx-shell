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

/** @module vertx-shell-js/pty */
var utils = require('vertx-js/util/utils');
var Stream = require('vertx-shell-js/stream');
var Tty = require('vertx-shell-js/tty');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JPty = io.vertx.ext.shell.io.Pty;

/**

 @class
*/
var Pty = function(j_val) {

  var j_pty = j_val;
  var that = this;

  /**
   @return the standard input of the terminal

   @public

   @return {Stream}
   */
  this.stdin = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_pty["stdin()"](), Stream);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set the standard out of the pseudo terminal.

   @public
   @param stdout {Stream} the standard output 
   @return {Pty} this current object
   */
  this.setStdout = function(stdout) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_pty["setStdout(io.vertx.ext.shell.io.Stream)"](stdout._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Resize the terminal.

   @public
   @param width {number} 
   @param height {number} 
   @return {Pty} this current object
   */
  this.setSize = function(width, height) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
      j_pty["setSize(int,int)"](width, height);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the pseudo terminal slave

   @public

   @return {Tty}
   */
  this.slave = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_pty["slave()"](), Tty);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_pty;
};

/**
 Create a new pseudo terminal.

 @memberof module:vertx-shell-js/pty
 @param terminalType {string} the terminal type, for instance  
 @return {Pty} the created pseudo terminal
 */
Pty.create = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JPty["create()"](), Pty);
  }else if (__args.length === 1 && typeof __args[0] === 'string') {
    return utils.convReturnVertxGen(JPty["create(java.lang.String)"](__args[0]), Pty);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = Pty;