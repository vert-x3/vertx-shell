package io.vertx.kotlin.ext.shell.term

import io.vertx.ext.shell.term.TelnetTermOptions
import io.vertx.core.http.ClientAuth

fun TelnetTermOptions(
    acceptBacklog: Int? = null,
  charset: String? = null,
  clientAuth: ClientAuth? = null,
  clientAuthRequired: Boolean? = null,
  host: String? = null,
  idleTimeout: Int? = null,
  inBinary: Boolean? = null,
  intputrc: String? = null,
  logActivity: Boolean? = null,
  outBinary: Boolean? = null,
  port: Int? = null,
  receiveBufferSize: Int? = null,
  reuseAddress: Boolean? = null,
  sendBufferSize: Int? = null,
  soLinger: Int? = null,
  ssl: Boolean? = null,
  tcpKeepAlive: Boolean? = null,
  tcpNoDelay: Boolean? = null,
  trafficClass: Int? = null,
  useAlpn: Boolean? = null,
  usePooledBuffers: Boolean? = null): TelnetTermOptions = io.vertx.ext.shell.term.TelnetTermOptions().apply {

  if (acceptBacklog != null) {
    this.acceptBacklog = acceptBacklog
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

  if (host != null) {
    this.host = host
  }

  if (idleTimeout != null) {
    this.idleTimeout = idleTimeout
  }

  if (inBinary != null) {
    this.inBinary = inBinary
  }

  if (intputrc != null) {
    this.intputrc = intputrc
  }

  if (logActivity != null) {
    this.logActivity = logActivity
  }

  if (outBinary != null) {
    this.outBinary = outBinary
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

  if (soLinger != null) {
    this.soLinger = soLinger
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

  if (trafficClass != null) {
    this.trafficClass = trafficClass
  }

  if (useAlpn != null) {
    this.isUseAlpn = useAlpn
  }

  if (usePooledBuffers != null) {
    this.isUsePooledBuffers = usePooledBuffers
  }

}

