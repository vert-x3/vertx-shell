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

/** @module vertx-shell-js/base_commands */
var utils = require('vertx-js/util/utils');
var Command = require('vertx-shell-js/command');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JBaseCommands = io.vertx.ext.shell.command.BaseCommands;

/**

 @class
*/
var BaseCommands = function(j_val) {

  var j_baseCommands = j_val;
  var that = this;

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_baseCommands;
};

/**

 @memberof module:vertx-shell-js/base_commands

 @return {Command}
 */
BaseCommands.verticle_ls = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBaseCommands["verticle_ls()"](), Command);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/base_commands

 @return {Command}
 */
BaseCommands.verticle_deploy = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBaseCommands["verticle_deploy()"](), Command);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/base_commands

 @return {Command}
 */
BaseCommands.verticle_undeploy = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBaseCommands["verticle_undeploy()"](), Command);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/base_commands

 @return {Command}
 */
BaseCommands.verticle_factories = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBaseCommands["verticle_factories()"](), Command);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/base_commands

 @return {Command}
 */
BaseCommands.server_ls = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBaseCommands["server_ls()"](), Command);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/base_commands

 @return {Command}
 */
BaseCommands.local_map_get = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBaseCommands["local_map_get()"](), Command);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/base_commands

 @return {Command}
 */
BaseCommands.local_map_put = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBaseCommands["local_map_put()"](), Command);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/base_commands

 @return {Command}
 */
BaseCommands.local_map_rm = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBaseCommands["local_map_rm()"](), Command);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/base_commands

 @return {Command}
 */
BaseCommands.bus_send = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBaseCommands["bus_send()"](), Command);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/base_commands

 @return {Command}
 */
BaseCommands.bus_tail = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBaseCommands["bus_tail()"](), Command);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/base_commands

 @return {Command}
 */
BaseCommands.fs_cd = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBaseCommands["fs_cd()"](), Command);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/base_commands

 @return {Command}
 */
BaseCommands.fs_pwd = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBaseCommands["fs_pwd()"](), Command);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/base_commands

 @return {Command}
 */
BaseCommands.fs_ls = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBaseCommands["fs_ls()"](), Command);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/base_commands

 @return {Command}
 */
BaseCommands.sleep = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBaseCommands["sleep()"](), Command);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/base_commands

 @return {Command}
 */
BaseCommands.echo = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBaseCommands["echo()"](), Command);
  } else utils.invalidArgs();
};

/**

 @memberof module:vertx-shell-js/base_commands

 @return {Command}
 */
BaseCommands.help = function() {
  var __args = arguments;
  if (__args.length === 0) {
    return utils.convReturnVertxGen(JBaseCommands["help()"](), Command);
  } else utils.invalidArgs();
};

// We export the Constructor function
module.exports = BaseCommands;