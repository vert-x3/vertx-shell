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

/** @module vertx-shell-js/arg_token */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JArgToken = io.vertx.ext.shell.command.ArgToken;

/**

 @class
*/
var ArgToken = function(j_val) {

  var j_argToken = j_val;
  var that = this;

  /**

   @public

   @return {string}
   */
  this.raw = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_argToken["raw()"]();
    } else utils.invalidArgs();
  };

  /**

   @public

   @return {string}
   */
  this.value = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_argToken["value()"]();
    } else utils.invalidArgs();
  };

  /**

   @public

   @return {boolean}
   */
  this.isText = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_argToken["isText()"]();
    } else utils.invalidArgs();
  };

  /**

   @public

   @return {boolean}
   */
  this.isBlank = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_argToken["isBlank()"]();
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_argToken;
};

/**

 @memberof module:vertx-shell-js/arg_token
 @param s {string} 
 @return {ArgToken}
 */
ArgToken.createText = function(s) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'string') {
    return utils.convReturnVertxGen(JArgToken["createText(java.lang.String)"](s), ArgToken);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/arg_token
 @param s {string} 
 @return {ArgToken}
 */
ArgToken.createBlank = function(s) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'string') {
    return utils.convReturnVertxGen(JArgToken["createBlank(java.lang.String)"](s), ArgToken);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/arg_token
 @param s {string} 
 @return {Array.<ArgToken>}
 */
ArgToken.tokenize = function(s) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'string') {
    return utils.convReturnListSetVertxGen(JArgToken["tokenize(java.lang.String)"](s), ArgToken);
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = ArgToken;