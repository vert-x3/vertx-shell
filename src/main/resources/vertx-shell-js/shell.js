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
var CommandManager = require('vertx-shell-js/command_manager');
var Job = require('vertx-shell-js/job');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JShell = io.vertx.ext.shell.Shell;

/**

 @class
*/
var Shell = function(j_val) {

  var j_shell = j_val;
  var that = this;

  /**

   @public
   @param s {string} 
   @param handler {function} 
   */
  this.createJob = function(s, handler) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'string' && typeof __args[1] === 'function') {
      j_shell["createJob(java.lang.String,io.vertx.core.Handler)"](s, function(ar) {
      if (ar.succeeded()) {
        handler(utils.convReturnVertxGen(ar.result(), Job), null);
      } else {
        handler(null, ar.cause());
      }
    });
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_shell;
};

/**

 @memberof module:vertx-shell-js/shell
 @param vertx {Vertx} 
 @param manager {CommandManager} 
 @return {Shell}
 */
Shell.create = function(vertx, manager) {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object' && __args[1]._jdel) {
    return utils.convReturnVertxGen(JShell["create(io.vertx.core.Vertx,io.vertx.ext.shell.command.CommandManager)"](vertx._jdel, manager._jdel), Shell);
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = Shell;