package io.vertx.kotlin.ext.shell.term

import io.vertx.ext.shell.term.HttpTermOptions
import io.vertx.core.http.ClientAuth
import io.vertx.core.http.HttpVersion

fun HttpTermOptions(
        acceptBacklog: Int? = null,
    alpnVersions: List<HttpVersion>? = null,
    charset: String? = null,
    clientAuth: ClientAuth? = null,
    clientAuthRequired: Boolean? = null,
    compressionLevel: Int? = null,
    compressionSupported: Boolean? = null,
    decompressionSupported: Boolean? = null,
    handle100ContinueAutomatically: Boolean? = null,
    host: String? = null,
    http2ConnectionWindowSize: Int? = null,
    idleTimeout: Int? = null,
    initialSettings: io.vertx.core.http.Http2Settings? = null,
    intputrc: String? = null,
    logActivity: Boolean? = null,
    maxChunkSize: Int? = null,
    maxHeaderSize: Int? = null,
    maxInitialLineLength: Int? = null,
    maxWebsocketFrameSize: Int? = null,
    port: Int? = null,
    receiveBufferSize: Int? = null,
    reuseAddress: Boolean? = null,
    sendBufferSize: Int? = null,
    shellHtmlResource: io.vertx.core.buffer.Buffer? = null,
    soLinger: Int? = null,
    sockJSHandlerOptions: io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions? = null,
    sockJSPath: String? = null,
    ssl: Boolean? = null,
    tcpKeepAlive: Boolean? = null,
    tcpNoDelay: Boolean? = null,
    termJsResource: io.vertx.core.buffer.Buffer? = null,
    trafficClass: Int? = null,
    useAlpn: Boolean? = null,
    usePooledBuffers: Boolean? = null,
    vertsShellJsResource: io.vertx.core.buffer.Buffer? = null,
    websocketSubProtocols: String? = null): HttpTermOptions = io.vertx.ext.shell.term.HttpTermOptions().apply {

    if (acceptBacklog != null) {
        this.acceptBacklog = acceptBacklog
    }

    if (alpnVersions != null) {
        this.alpnVersions = alpnVersions
    }

    if (charset != null) {
        this.charset = charset
    }

    if (clientAuth != null) {
        this.clientAuth = clientAuth
    }

    if (clientAuthRequired != null) {
        this.isClientAuthRequired = clientAuthRequired
    }

    if (compressionLevel != null) {
        this.compressionLevel = compressionLevel
    }

    if (compressionSupported != null) {
        this.isCompressionSupported = compressionSupported
    }

    if (decompressionSupported != null) {
        this.isDecompressionSupported = decompressionSupported
    }

    if (handle100ContinueAutomatically != null) {
        this.isHandle100ContinueAutomatically = handle100ContinueAutomatically
    }

    if (host != null) {
        this.host = host
    }

    if (http2ConnectionWindowSize != null) {
        this.http2ConnectionWindowSize = http2ConnectionWindowSize
    }

    if (idleTimeout != null) {
        this.idleTimeout = idleTimeout
    }

    if (initialSettings != null) {
        this.initialSettings = initialSettings
    }

    if (intputrc != null) {
        this.intputrc = intputrc
    }

    if (logActivity != null) {
        this.logActivity = logActivity
    }

    if (maxChunkSize != null) {
        this.maxChunkSize = maxChunkSize
    }

    if (maxHeaderSize != null) {
        this.maxHeaderSize = maxHeaderSize
    }

    if (maxInitialLineLength != null) {
        this.maxInitialLineLength = maxInitialLineLength
    }

    if (maxWebsocketFrameSize != null) {
        this.maxWebsocketFrameSize = maxWebsocketFrameSize
    }

    if (port != null) {
        this.port = port
    }

    if (receiveBufferSize != null) {
        this.receiveBufferSize = receiveBufferSize
    }

    if (reuseAddress != null) {
        this.isReuseAddress = reuseAddress
    }

    if (sendBufferSize != null) {
        this.sendBufferSize = sendBufferSize
    }

    if (shellHtmlResource != null) {
        this.shellHtmlResource = shellHtmlResource
    }

    if (soLinger != null) {
        this.soLinger = soLinger
    }

    if (sockJSHandlerOptions != null) {
        this.sockJSHandlerOptions = sockJSHandlerOptions
    }

    if (sockJSPath != null) {
        this.sockJSPath = sockJSPath
    }

    if (ssl != null) {
        this.isSsl = ssl
    }

    if (tcpKeepAlive != null) {
        this.isTcpKeepAlive = tcpKeepAlive
    }

    if (tcpNoDelay != null) {
        this.isTcpNoDelay = tcpNoDelay
    }

    if (termJsResource != null) {
        this.termJsResource = termJsResource
    }

    if (trafficClass != null) {
        this.trafficClass = trafficClass
    }

    if (useAlpn != null) {
        this.isUseAlpn = useAlpn
    }

    if (usePooledBuffers != null) {
        this.isUsePooledBuffers = usePooledBuffers
    }

    if (vertsShellJsResource != null) {
        this.vertsShellJsResource = vertsShellJsResource
    }

    if (websocketSubProtocols != null) {
        this.websocketSubProtocols = websocketSubProtocols
    }

}

