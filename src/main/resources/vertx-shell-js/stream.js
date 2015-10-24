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

/** @module vertx-shell-js/stream */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JStream = io.vertx.ext.shell.io.Stream;

/**

 @class
*/
var Stream = function(j_val) {

  var j_stream = j_val;
  var that = this;

  /**

   @public
   @param data {Object} 
   @return {Stream}
   */
  this.write = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'string') {
      j_stream["write(java.lang.String)"](__args[0]);
      return that;
    }  else if (__args.length === 1 && (typeof __args[0] === 'object' && __args[0] != null)) {
      j_stream["write(io.vertx.core.json.JsonObject)"](utils.convParamJsonObject(__args[0]));
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_stream;
};

/**

 @memberof module:vertx-shell-js/stream
 @param handler {function} 
 @return {Stream}
 */
Stream.ofString = function(handler) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'function') {
    return utils.convReturnVertxGen(JStream["ofString(io.vertx.core.Handler)"](function(jVal) {
    handler(jVal);
  }), Stream);
  } else throw new TypeError('function invoked with invalid arguments');
};

/**

 @memberof module:vertx-shell-js/stream
 @param handler {function} 
 @return {Stream}
 */
Stream.ofJson = function(handler) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'function') {
    return utils.convReturnVertxGen(JStream["ofJson(io.vertx.core.Handler)"](function(jVal) {
    handler(utils.convReturnJson(jVal));
  }), Stream);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = Stream;