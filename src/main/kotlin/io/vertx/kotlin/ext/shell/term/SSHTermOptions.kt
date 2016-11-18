package io.vertx.kotlin.ext.shell.term

import io.vertx.ext.shell.term.SSHTermOptions

fun SSHTermOptions(
        defaultCharset: String? = null,
    host: String? = null,
    intputrc: String? = null,
    port: Int? = null): SSHTermOptions = io.vertx.ext.shell.term.SSHTermOptions().apply {

    if (defaultCharset != null) {
        this.defaultCharset = defaultCharset
    }

    if (host != null) {
        this.host = host
    }

    if (intputrc != null) {
        this.intputrc = intputrc
    }

    if (port != null) {
        this.port = port
    }

}

