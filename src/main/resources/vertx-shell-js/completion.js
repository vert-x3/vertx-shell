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

/** @module vertx-shell-js/completion */
var utils = require('vertx-js/util/utils');
var ArgToken = require('vertx-shell-js/arg_token');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JCompletion = io.vertx.ext.shell.completion.Completion;

/**

 @class
*/
var Completion = function(j_val) {

  var j_completion = j_val;
  var that = this;

  /**

   @public

   @return {string}
   */
  this.line = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_completion["line()"]();
    } else utils.invalidArgs();
  };

  /**

   @public

   @return {Array.<ArgToken>}
   */
  this.lineTokens = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnListSetVertxGen(j_completion["lineTokens()"](), ArgToken);
    } else utils.invalidArgs();
  };

  /**

   @public
   @param value {string} 
   @param terminal {boolean} 
   */
  this.complete = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0] instanceof Array) {
      j_completion["complete(java.util.List)"](__args[0]);
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] ==='boolean') {
      j_completion["complete(java.lang.String,boolean)"](__args[0], __args[1]);
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_completion;
};

// We export the Constructor function
module.exports = Completion;