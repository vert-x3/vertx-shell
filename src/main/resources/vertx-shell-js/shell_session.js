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

/** @module vertx-shell-js/shell_session */
var utils = require('vertx-js/util/utils');
var Job = require('vertx-shell-js/job');
var CliToken = require('vertx-shell-js/cli_token');
var Session = require('vertx-shell-js/session');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JShellSession = io.vertx.ext.shell.system.ShellSession;

/**

 @class
*/
var ShellSession = function(j_val) {

  var j_shellSession = j_val;
  var that = this;

  /**

   @public

   @return {Session}
   */
  this.session = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_shellSession["session()"](), Session);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Array.<Job>}
   */
  this.jobs = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnListSetVertxGen(j_shellSession["jobs()"](), Job);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param id {number} 
   @return {Job}
   */
  this.getJob = function(id) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return utils.convReturnVertxGen(j_shellSession["getJob(int)"](id), Job);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param args {Array.<CliToken>} 
   @return {Job}
   */
  this.createJob = function(args) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0] instanceof Array) {
      return utils.convReturnVertxGen(j_shellSession["createJob(java.util.List)"](utils.convParamListVertxGen(args)), Job);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_shellSession;
};

// We export the Constructor function
module.exports = ShellSession;