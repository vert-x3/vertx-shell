package io.vertx.kotlin.ext.shell

import io.vertx.ext.shell.ShellServiceOptions

fun ShellServiceOptions(
    httpOptions: io.vertx.ext.shell.term.HttpTermOptions? = null,
  reaperInterval: Long? = null,
  sessionTimeout: Long? = null,
  sshOptions: io.vertx.ext.shell.term.SSHTermOptions? = null,
  telnetOptions: io.vertx.ext.shell.term.TelnetTermOptions? = null,
  welcomeMessage: String? = null): ShellServiceOptions = io.vertx.ext.shell.ShellServiceOptions().apply {

  if (httpOptions != null) {
    this.httpOptions = httpOptions
  }

  if (reaperInterval != null) {
    this.reaperInterval = reaperInterval
  }

  if (sessionTimeout != null) {
    this.sessionTimeout = sessionTimeout
  }

  if (sshOptions != null) {
    this.sshOptions = sshOptions
  }

  if (telnetOptions != null) {
    this.telnetOptions = telnetOptions
  }

  if (welcomeMessage != null) {
    this.welcomeMessage = welcomeMessage
  }

}

