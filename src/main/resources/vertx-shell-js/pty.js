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
var Dimension = require('vertx-shell-js/dimension');
var Stream = require('vertx-shell-js/stream');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JPty = io.vertx.ext.shell.term.Pty;

/**

 @class
*/
var Pty = function(j_val) {

  var j_pty = j_val;
  var that = this;

  /**

   @public

   @return {Stream}
   */
  this.stdin = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_pty["stdin()"](), Stream);
    } else utils.invalidArgs();
  };

  /**

   @public
   @param stdout {Stream} 
   */
  this.setStdout = function(stdout) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_pty["setStdout(io.vertx.ext.shell.Stream)"](stdout._jdel);
    } else utils.invalidArgs();
  };

  /**

   @public
   @param size {Dimension} 
   */
  this.setWindowSize = function(size) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_pty["setWindowSize(io.vertx.ext.shell.Dimension)"](size._jdel);
    } else utils.invalidArgs();
  };

  /**

   @public
   @param event {string} 
   @return {boolean}
   */
  this.sendEvent = function(event) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return j_pty["sendEvent(java.lang.String)"](event);
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_pty;
};

// We export the Constructor function
module.exports = Pty;