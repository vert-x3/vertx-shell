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
var Process = require('vertx-shell-js/process');
var Tty = require('vertx-shell-js/tty');
var Session = require('vertx-shell-js/session');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JJob = Java.type('io.vertx.ext.shell.system.Job');

/**

 @class
*/
var Job = function(j_val) {

  var j_job = j_val;
  var that = this;

  /**

   @public

   @return {number} the job id
   */
  this.id = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_job["id()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Object} the job exec status
   */
  this.status = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnEnum(j_job["status()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {number} when the job was last stopped
   */
  this.lastStopped = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_job["lastStopped()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {string} the execution line of the job, i.e the shell command line that launched this job
   */
  this.line = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return j_job["line()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a tty on the job.

   @public
   @param tty {Tty} the tty to use 
   @return {Job} this object
   */
  this.setTty = function(tty) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_job["setTty(io.vertx.ext.shell.term.Tty)"](tty._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set a session on the job.

   @public
   @param session {Session} the session to use 
   @return {Job} this object
   */
  this.setSession = function(session) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'object' && __args[0]._jdel) {
      j_job["setSession(io.vertx.ext.shell.session.Session)"](session._jdel);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Set an handler called when the job terminates.

   @public
   @param handler {function} the terminate handler 
   @return {Job} this object
   */
  this.statusUpdateHandler = function(handler) {
    var __args = arguments;
    if (__args.length === 1 && typeof __args[0] === 'function') {
      j_job["statusUpdateHandler(io.vertx.core.Handler)"](function(jVal) {
      handler(utils.convReturnEnum(jVal));
    });
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Run the job, before running the job a  must be set.

   @public

   @return {Job} this object
   */
  this.run = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_job["run()"]();
      return that;
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
   Resume the job.

   @public
   @param foreground {boolean} true when the job is resumed in foreground 
   @return {Job}
   */
  this.resume = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(Job, j_job["resume()"]());
    }  else if (__args.length === 1 && typeof __args[0] ==='boolean') {
      j_job["resume(boolean)"](__args[0]);
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Send the job to background.

   @public

   @return {Job} this object
   */
  this.toBackground = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_job["toBackground()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Send the job to foreground.

   @public

   @return {Job} this object
   */
  this.toForeground = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_job["toForeground()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Resume the job.

   @public

   @return {Job} this object
   */
  this.suspend = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_job["suspend()"]();
      return that;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Terminate the job.

   @public

   */
  this.terminate = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_job["terminate()"]();
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Process} the first process in the job
   */
  this.process = function() {
    var __args = arguments;
    if (__args.length === 0) {
      if (that.cachedprocess == null) {
        that.cachedprocess = utils.convReturnVertxGen(Process, j_job["process()"]());
      }
      return that.cachedprocess;
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_job;
};

Job._jclass = utils.getJavaClass("io.vertx.ext.shell.system.Job");
Job._jtype = {
  accept: function(obj) {
    return Job._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(Job.prototype, {});
    Job.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
Job._create = function(jdel) {
  var obj = Object.create(Job.prototype, {});
  Job.apply(obj, arguments);
  return obj;
}
module.exports = Job;