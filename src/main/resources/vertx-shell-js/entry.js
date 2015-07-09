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

/** @module vertx-shell-js/entry */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JEntry = io.vertx.ext.shell.completion.Entry;

/**

 @class
*/
var Entry = function(j_val) {

  var j_entry = j_val;
  var that = this;

  /**

   @public

   @return {string}
   */
  this.value = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_entry["value()"]();
    } else utils.invalidArgs();
  };

  /**

   @public

   @return {boolean}
   */
  this.isTerminal = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_entry["isTerminal()"]();
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_entry;
};

/**

 @memberof module:vertx-shell-js/entry
 @param terminal {boolean} 
 @param value {string} 
 @return {Entry}
 */
Entry.entry = function(terminal, value) {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] ==='boolean' && typeof __args[1] === 'string') {
    return utils.convReturnVertxGen(JEntry["entry(boolean,java.lang.String)"](terminal, value), Entry);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/entry
 @param value {string} 
 @return {Entry}
 */
Entry.terminalEntry = function(value) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'string') {
    return utils.convReturnVertxGen(JEntry["terminalEntry(java.lang.String)"](value), Entry);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/entry
 @param value {string} 
 @return {Entry}
 */
Entry.nonTerminalEntry = function(value) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'string') {
    return utils.convReturnVertxGen(JEntry["nonTerminalEntry(java.lang.String)"](value), Entry);
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = Entry;