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

/** @module vertx-shell-js/dimension */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JDimension = io.vertx.ext.shell.Dimension;

/**

 @class
*/
var Dimension = function(j_val) {

  var j_dimension = j_val;
  var that = this;

  /**

   @public

   @return {number}
   */
  this.width = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_dimension["width()"]();
    } else utils.invalidArgs();
  };

  /**

   @public

   @return {number}
   */
  this.height = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_dimension["height()"]();
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_dimension;
};

/**

 @memberof module:vertx-shell-js/dimension
 @param width {number} 
 @param height {number} 
 @return {Dimension}
 */
Dimension.create = function(width, height) {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] ==='number' && typeof __args[1] ==='number') {
    return utils.convReturnVertxGen(JDimension["create(int,int)"](width, height), Dimension);
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = Dimension;