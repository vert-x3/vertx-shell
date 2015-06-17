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
var Stream = require('vertx-shell-js/stream');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JProcess = io.vertx.ext.shell.Process;

/**

 @class
*/
var Process = function(j_val) {

  var j_process = j_val;
  var that = this;

  /**

   @public

   @return {Stream}
   */
  this.stdin = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_process["stdin()"](), Stream);
    } else utils.invalidArgs();
  };

  /**

   @public
   @param stdout {Stream} 
   */
  this.setStdout = function(stdout) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_process["setStdout(io.vertx.ext.shell.Stream)"](stdout._jdel);
    } else utils.invalidArgs();
  };

  /**

   @public
   @param beginHandler {function} 
   */
  this.run = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_process["run()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'function') {
      j_process["run(io.vertx.core.Handler)"](__args[0]);
    } else utils.invalidArgs();
  };

  /**

   @public
   @param signal {Object} 
   */
  this.sendSignal = function(signal) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_process["sendSignal(io.vertx.ext.shell.Signal)"](io.vertx.ext.shell.Signal.valueOf(__args[0]));
    } else utils.invalidArgs();
  };

  /**

   @public
   @param handler {function} 
   */
  this.endHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_process["endHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(jVal);
    });
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_process;
};

// We export the Constructor function
module.exports = Process;