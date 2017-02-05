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

/** @module vertx-shell-js/job_controller */
var utils = require('vertx-js/util/utils');
var Job = require('vertx-shell-js/job');
var Process = require('vertx-shell-js/process');

var io = Packages.io;
var JsonObject = io.vertx.core.json.JsonObject;
var JJobController = Java.type('io.vertx.ext.shell.system.JobController');

/**
 The job controller.<p/>

 @class
*/
var JobController = function(j_val) {

  var j_jobController = j_val;
  var that = this;

  /**

   @public

   @return {Job} the current foreground job
   */
  this.foregroundJob = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnVertxGen(Job, j_jobController["foregroundJob()"]());
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**

   @public

   @return {Array.<Job>} the active jobs
   */
  this.jobs = function() {
    var __args = arguments;
    if (__args.length === 0) {
      return utils.convReturnListSetVertxGen(j_jobController["jobs()"](), Job);
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
      return utils.convReturnVertxGen(Job, j_jobController["getJob(int)"](id));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Create a job wrapping a process.

   @public
   @param process {Process} the process 
   @param line {string} the line 
   @return {Job} the created job
   */
  this.createJob = function(process, line) {
    var __args = arguments;
    if (__args.length === 2 && typeof __args[0] === 'object' && __args[0]._jdel && typeof __args[1] === 'string') {
      return utils.convReturnVertxGen(Job, j_jobController["createJob(io.vertx.ext.shell.system.Process,java.lang.String)"](process._jdel, line));
    } else throw new TypeError('function invoked with invalid arguments');
  };

  /**
   Close the controller and terminate all the underlying jobs, a closed controller does not accept anymore jobs.

   @public
   @param completionHandler {function} 
   */
  this.close = function() {
    var __args = arguments;
    if (__args.length === 0) {
      j_jobController["close()"]();
    }  else if (__args.length === 1 && typeof __args[0] === 'function') {
      j_jobController["close(io.vertx.core.Handler)"](__args[0]);
    } else throw new TypeError('function invoked with invalid arguments');
  };

  // A reference to the underlying Java delegate
  // NOTE! This is an internal API and must not be used in user code.
  // If you rely on this property your code is likely to break if we change it / remove it without warning.
  this._jdel = j_jobController;
};

JobController._jclass = utils.getJavaClass("io.vertx.ext.shell.system.JobController");
JobController._jtype = {
  accept: function(obj) {
    return JobController._jclass.isInstance(obj._jdel);
  },
  wrap: function(jdel) {
    var obj = Object.create(JobController.prototype, {});
    JobController.apply(obj, arguments);
    return obj;
  },
  unwrap: function(obj) {
    return obj._jdel;
  }
};
JobController._create = function(jdel) {
  var obj = Object.create(JobController.prototype, {});
  JobController.apply(obj, arguments);
  return obj;
}
module.exports = JobController;