package io.vertx.kotlin.ext.shell

import io.vertx.ext.shell.ShellServerOptions

fun ShellServerOptions(
        reaperInterval: Long? = null,
    sessionTimeout: Long? = null,
    welcomeMessage: String? = null): ShellServerOptions = io.vertx.ext.shell.ShellServerOptions().apply {

    if (reaperInterval != null) {
        this.reaperInterval = reaperInterval
    }

    if (sessionTimeout != null) {
        this.sessionTimeout = sessionTimeout
    }

    if (welcomeMessage != null) {
        this.welcomeMessage = welcomeMessage
    }

}

