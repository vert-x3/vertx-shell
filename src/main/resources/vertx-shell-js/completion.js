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
var Vertx = require('vertx-js/vertx');
var CliToken = require('vertx-shell-js/cli_token');
var Session = require('vertx-shell-js/session');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JCompletion = io.vertx.ext.shell.cli.Completion;

/**
 The completion object

 @class
*/
var Completion = function(j_val) {

  var j_completion = j_val;
  var that = this;

  /**

   @public

   @return {Vertx} the current Vert.x instance
   */
  this.vertx = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_completion["vertx()"](), Vertx);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Session} the shell current session, useful for accessing data like the current path for file completion, etc...
   */
  this.session = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_completion["session()"](), Session);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string} the current line being completed in raw format, i.e without any char escape performed
   */
  this.rawLine = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_completion["rawLine()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Array.<CliToken>} the current line being completed as preparsed tokens
   */
  this.lineTokens = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnListSetVertxGen(j_completion["lineTokens()"](), CliToken);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   End the completion with a value that will be inserted to complete the line.

   @public
   @param value {string} the value to complete with 
   @param terminal {boolean} true if the value is terminal, i.e can be further completed 
   */
  this.complete = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0] instanceof Array) {
      j_completion["complete(java.util.List)"](utils.convParamListBasicOther(__args[0]));
    }  else if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] ==='boolean') {
      j_completion["complete(java.lang.String,boolean)"](__args[0], __args[1]);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_completion;
};

// We export the Constructor function
module.exports = Completion;