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

/** @module vertx-shell-js/tty */
var utils = require('vertx-js/util/utils');
var Stream = require('vertx-shell-js/stream');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JTty = io.vertx.ext.shell.io.Tty;

/**
 Provide interactions with the Shell TTY.

 @class
*/
var Tty = function(j_val) {

  var j_tty = j_val;
  var that = this;

  /**
   @return the declared tty type, for instance , ,  etc... it can be null
   when the tty does not have declared its type.

   @public

   @return {string}
   */
  this.type = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_tty["type()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the current width, i.e the number of rows or  if unknown

   @public

   @return {number}
   */
  this.width = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_tty["width()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the current height, i.e the number of columns or  if unknown

   @public

   @return {number}
   */
  this.height = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_tty["height()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a stream on the standard input to read the data.

   @public
   @param stdin {Stream} the standard input 
   @return {Tty} this object
   */
  this.setStdin = function(stdin) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_tty["setStdin(io.vertx.ext.shell.io.Stream)"](stdin._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the standard output for emitting data

   @public

   @return {Stream}
   */
  this.stdout = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_tty["stdout()"](), Stream);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a resize handler, the handler is called when the tty size changes.

   @public
   @param handler {function} the resize handler 
   @return {Tty} this object
   */
  this.resizehandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_tty["resizehandler(io.vertx.core.Handler)"](handler);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_tty;
};

// We export the Constructor function
module.exports = Tty;