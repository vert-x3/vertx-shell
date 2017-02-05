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

/** @module vertx-shell-js/cli_token */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JCliToken = Java.type('io.vertx.ext.shell.cli.CliToken');

/**
 A parsed token in the command line interface.

 @class
*/
var CliToken = function(j_val) {

  var j_cliToken = j_val;
  var that = this;

  /**

   @public

   @return {string} the token value
   */
  this.value = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_cliToken["value()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string} the raw token value, that may contain unescaped chars, for instance 
   */
  this.raw = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_cliToken["raw()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {boolean} true when it's a text token
   */
  this.isText = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_cliToken["isText()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {boolean} true when it's a blank token
   */
  this.isBlank = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_cliToken["isBlank()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_cliToken;
};

CliToken._jclass = utils.getJavaClass("io.vertx.ext.shell.cli.CliToken");
CliToken._jtype = {
  accept: function(obj) {
    return CliToken._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(CliToken.prototype, {});
    CliToken.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
CliToken._create = function(jdel) {
  var obj = Object.create(CliToken.prototype, {});
  CliToken.apply(obj, arguments);
  return obj;
}
/**
 Create a text token.

 @memberof module:vertx-shell-js/cli_token
 @param text {string} the text 
 @return {CliToken} the token
 */
CliToken.createText = function(text) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'string') {
    return utils.convReturnVertxGen(CliToken, JCliToken["createText(java.lang.String)"](text));
  } else throw new TypeError('function invoked with invalid arguments');
};

/**
 Create a new blank token.

 @memberof module:vertx-shell-js/cli_token
 @param blank {string} the blank value 
 @return {CliToken} the token
 */
CliToken.createBlank = function(blank) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'string') {
    return utils.convReturnVertxGen(CliToken, JCliToken["createBlank(java.lang.String)"](blank));
  } else throw new TypeError('function invoked with invalid arguments');
};

/**
 Tokenize the string argument and return a list of tokens.

 @memberof module:vertx-shell-js/cli_token
 @param s {string} the tokenized string 
 @return {Array.<CliToken>} the tokens
 */
CliToken.tokenize = function(s) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'string') {
    return utils.convReturnListSetVertxGen(JCliToken["tokenize(java.lang.String)"](s), CliToken);
  } else throw new TypeError('function invoked with invalid arguments');
};

module.exports = CliToken;