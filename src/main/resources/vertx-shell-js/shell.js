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

/** @module vertx-shell-js/shell */
var utils = require('vertx-js/util/utils');
var Job = require('vertx-shell-js/job');
var CliToken = require('vertx-shell-js/cli_token');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JShell = io.vertx.ext.shell.system.Shell;

/**
 An interactive session between a consumer and a shell.<p/>

 @class
*/
var Shell = function(j_val) {

  var j_shell = j_val;
  var that = this;

  /**
   @return the jobs active in this session

   @public

   @return {Array.<Job>}
   */
  this.jobs = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnListSetVertxGen(j_shell["jobs()"](), Job);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Returns an active job in this session by its .

   @public
   @param id {number} the job id 
   @return {Job} the job of  when not found
   */
  this.getJob = function(id) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] ==='number') {
      return utils.convReturnVertxGen(j_shell["getJob(int)"](id), Job);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   See {@link Shell#createJob}

   @public
   @param line {string} 
   @return {Job}
   */
  this.createJob = function() {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0] instanceof Array) {
      return utils.convReturnVertxGen(j_shell["createJob(java.util.List)"](utils.convParamListVertxGen(__args[0])), Job);
    }  else if (__args.length === 1 && typeof __args[0] === 'string') {
      return utils.convReturnVertxGen(j_shell["createJob(java.lang.String)"](__args[0]), Job);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Close the shell session and terminate all the underlying jobs.

   @public
   @param completionHandler {function} 
   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_shell["close()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'function') {
      j_shell["close(io.vertx.core.Handler)"](__args[0]);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_shell;
};

// We export the Constructor function
module.exports = Shell;