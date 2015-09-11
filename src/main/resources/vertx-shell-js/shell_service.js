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

/** @module vertx-shell-js/shell_service */
var utils = require('vertx-js/util/utils');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JShellService = io.vertx.ext.shell.ShellService;
var ShellServiceOptions = io.vertx.ext.shell.ShellServiceOptions;

/**

 @class
*/
var ShellService = function(j_val) {

  var j_shellService = j_val;
  var that = this;

  /**

   @public
   @param startHandler {function} 
   */
  this.start = function(startHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_shellService["start(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        startHandler(null, null);
      } else {
        startHandler(null, ar.cause());
      }
    });
    } else utils.invalidArgs();
  };

  /**

   @public
   @param closeHandler {function} 
   */
  this.close = function(closeHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_shellService["close(io.vertx.core.Handler)"](function(ar) {
      if (ar.succeeded()) {
        closeHandler(null, null);
      } else {
        closeHandler(null, ar.cause());
      }
    });
    } else utils.invalidArgs();
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_shellService;
};

/**

 @memberof module:vertx-shell-js/shell_service
 @param vertx {Vertx} 
 @param options {Object} 
 @return {ShellService}
 */
ShellService.create = function(vertx, options) {
  var __args = arguments;
  if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'object') {
    return utils.convReturnVertxGen(JShellService["create(io.vertx.core.Vertx,io.vertx.ext.shell.ShellServiceOptions)"](vertx._jdel, options != null ? new ShellServiceOptions(new JsonObject(JSON.stringify(options))) : null), ShellService);
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = ShellService;