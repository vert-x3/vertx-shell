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

/** @module vertx-shell-js/sock_js_term_handler */
var utils = require('vertx-js/util/utils');
var SockJSSocket = require('vertx-web-js/sock_js_socket');
var Vertx = require('vertx-js/vertx');
var Term = require('vertx-shell-js/term');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JSockJSTermHandler = io.vertx.ext.shell.term.SockJSTermHandler;

/**

 @class
*/
var SockJSTermHandler = function(j_val) {

  var j_sockJSTermHandler = j_val;
  var that = this;

  /**

   @public
   @param arg0 {SockJSSocket} 
   */
  this.handle = function(arg0) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_sockJSTermHandler["handle(io.vertx.ext.web.handler.sockjs.SockJSSocket)"](arg0._jdel);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public
   @param handler {function} 
   @return {SockJSTermHandler}
   */
  this.termHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_sockJSTermHandler["termHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnVertxGen(jVal, Term));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_sockJSTermHandler;
};

/**

 @memberof module:vertx-shell-js/sock_js_term_handler
 @param vertx {Vertx} 
 @return {SockJSTermHandler}
 */
SockJSTermHandler.create = function(vertx) {
  var __args = arguments;
  if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
    return utils.convReturnVertxGen(JSockJSTermHandler["create(io.vertx.core.Vertx)"](vertx._jdel), SockJSTermHandler);
  } else throw new TypeError('function invoked with invalid arguments');
};

// We export the Constructor function
module.exports = SockJSTermHandler;