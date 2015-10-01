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
var JCliToken = io.vertx.ext.shell.cli.CliToken;

/**
 A parsed token in the command line interface.

 @class
*/
var CliToken = function(j_val) {

  var j_cliToken = j_val;
  var that = this;

  /**
   @return the token value

   @public

   @return {string}
   */
  this.value = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_cliToken["value()"]();
    } else utils.invalidArgs();
  };

  /**
   @return the raw token value, that may contain unescaped chars, for instance 

   @public

   @return {string}
   */
  this.raw = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_cliToken["raw()"]();
    } else utils.invalidArgs();
  };

  /**
   @return true when it's a text token

   @public

   @return {boolean}
   */
  this.isText = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_cliToken["isText()"]();
    } else utils.invalidArgs();
  };

  /**
   @return true when it's a blank token

   @public

   @return {boolean}
   */
  this.isBlank = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_cliToken["isBlank()"]();
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_cliToken;
};

/**
 Create a text token.

 @memberof module:vertx-shell-js/cli_token
 @param text {string} the text 
 @return {CliToken} the token
 */
CliToken.createText = function(text) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'string') {
    return utils.convReturnVertxGen(JCliToken["createText(java.lang.String)"](text), CliToken);
  } else utils.invalidArgs();
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
    return utils.convReturnVertxGen(JCliToken["createBlank(java.lang.String)"](blank), CliToken);
  } else utils.invalidArgs();
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
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = CliToken;