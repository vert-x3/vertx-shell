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
var JTty = io.vertx.ext.shell.Tty;

/**

 @class
*/
var Tty = function(j_val) {

  var j_tty = j_val;
  var that = this;

  /**
   @return the current width, the number of rows

   @public

   @return {number}
   */
  this.width = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_tty["width()"]();
    } else utils.invalidArgs();
  };

  /**
   @return the current height, the number of columns

   @public

   @return {number}
   */
  this.height = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_tty["height()"]();
    } else utils.invalidArgs();
  };

  /**

   @public
   @param stdin {function} 
   @return {Tty}
   */
  this.setStdin = function(stdin) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_tty["setStdin(io.vertx.core.Handler)"](function(jVal) {
      stdin(jVal);
    });
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
      return utils.convReturnVertxGen(j_tty["stdout()"](), Stream);
    } else utils.invalidArgs();
  };

  /**

   @public
   @param event {string} 
   @param handler {function} 
   @return {Tty}
   */
  this.eventHandler = function(event, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_tty["eventHandler(java.lang.String,io.vertx.core.Handler)"](event, handler);
      return that;
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_tty;
};

// We export the Constructor function
module.exports = Tty;