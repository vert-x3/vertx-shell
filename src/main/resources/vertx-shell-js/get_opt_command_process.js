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

/** @module vertx-shell-js/get_opt_command_process */
var utils = require('vertx-js/util/utils');
var CommandProcess = require('vertx-shell-js/command_process');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JGetOptCommandProcess = io.vertx.ext.shell.getopt.GetOptCommandProcess;

/**

 @class
*/
var GetOptCommandProcess = function(j_val) {

  var j_getOptCommandProcess = j_val;
  var that = this;
  CommandProcess.call(this, j_val);

  /**

   @public

   @return {Array.<string>}
   */
  this.arguments = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_getOptCommandProcess["arguments()"]();
    } else utils.invalidArgs();
  };

  /**

   @public
   @param name {string} 
   @return {Array.<string>}
   */
  this.getOption = function(name) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      return j_getOptCommandProcess["getOption(java.lang.String)"](name);
    } else utils.invalidArgs();
  };

  /**

   @public
   @param stdin {function} 
   @return {GetOptCommandProcess}
   */
  this.setStdin = function(stdin) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_getOptCommandProcess["setStdin(io.vertx.core.Handler)"](function(jVal) {
      stdin(jVal);
    });
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param event {string} 
   @param handler {function} 
   @return {GetOptCommandProcess}
   */
  this.eventHandler = function(event, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_getOptCommandProcess["eventHandler(java.lang.String,io.vertx.core.Handler)"](event, handler);
      return that;
    } else utils.invalidArgs();
  };

  /**

   @public
   @param text {string} 
   @return {GetOptCommandProcess}
   */
  this.write = function(text) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_getOptCommandProcess["write(java.lang.String)"](text);
      return that;
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_getOptCommandProcess;
};

// We export the Constructor function
module.exports = GetOptCommandProcess;