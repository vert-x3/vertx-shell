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

/** @module vertx-shell-js/job */
var utils = require('vertx-js/util/utils');
var Tty = require('vertx-shell-js/tty');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JJob = io.vertx.ext.shell.system.Job;

/**

 @class
*/
var Job = function(j_val) {

  var j_job = j_val;
  var that = this;

  /**
   @return the job id

   @public

   @return {number}
   */
  this.id = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_job["id()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the job status

   @public

   @return {Object}
   */
  this.status = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnEnum(j_job["status()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return when the job was last stopped

   @public

   @return {number}
   */
  this.lastStopped = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_job["lastStopped()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the execution line of the job, i.e the shell command line that launched this job

   @public

   @return {string}
   */
  this.line = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_job["line()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   @return the current tty this job uses

   @public

   @return {Tty}
   */
  this.getTty = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(j_job["getTty()"](), Tty);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a tty on the job.

   @public
   @param tty {Tty} the tty to use 
   */
  this.setTty = function(tty) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_job["setTty(io.vertx.ext.shell.io.Tty)"](tty._jdel);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Run the job, before running the job a {@link Tty} must be set.

   @public
   @param endHandler {function} to be notified when the job terminates 
   */
  this.run = function(endHandler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_job["run(io.vertx.core.Handler)"](function(jVal) {
      endHandler(jVal);
    });
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Attempt to interrupt the job.

   @public

   @return {boolean} true if the job is actually interrupted
   */
  this.interrupt = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_job["interrupt()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Suspend the job.

   @public

   */
  this.resume = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_job["resume()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Resume the job.

   @public

   */
  this.suspend = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_job["suspend()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_job;
};

// We export the Constructor function
module.exports = Job;