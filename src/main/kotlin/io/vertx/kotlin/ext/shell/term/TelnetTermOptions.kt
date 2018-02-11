package io.vertx.kotlin.ext.shell.term

import io.vertx.ext.shell.term.TelnetTermOptions
import io.vertx.core.http.ClientAuth
import io.vertx.core.net.JdkSSLEngineOptions
import io.vertx.core.net.JksOptions
import io.vertx.core.net.OpenSSLEngineOptions
import io.vertx.core.net.PemKeyCertOptions
import io.vertx.core.net.PemTrustOptions
import io.vertx.core.net.PfxOptions

/**
 * A function providing a DSL for building [io.vertx.ext.shell.term.TelnetTermOptions] objects.
 *
 * Telnet terminal options configuration, extends [io.vertx.core.net.NetServerOptions].
 *
 * @param acceptBacklog 
 * @param charset  Set the charset to use when binary mode is active, see [io.vertx.ext.shell.term.TelnetTermOptions] and [io.vertx.ext.shell.term.TelnetTermOptions].
 * @param clientAuth 
 * @param clientAuthRequired 
 * @param crlPaths 
 * @param crlValues 
 * @param enabledCipherSuites 
 * @param enabledSecureTransportProtocols 
 * @param host 
 * @param idleTimeout 
 * @param inBinary  Set the telnet connection to negociate binary data format when receiving from the client, the default value is true. This allows to send data in 8 bit format and thus charset like UTF-8.
 * @param intputrc  The path of the <i>inputrc</i> config.
 * @param jdkSslEngineOptions 
 * @param keyStoreOptions 
 * @param logActivity 
 * @param openSslEngineOptions 
 * @param outBinary  Set the telnet connection to negociate binary data format when sending to the client, the default value is true. This allows to send data in 8 bit format and thus charset like UTF-8.
 * @param pemKeyCertOptions 
 * @param pemTrustOptions 
 * @param pfxKeyCertOptions 
 * @param pfxTrustOptions 
 * @param port 
 * @param receiveBufferSize 
 * @param reuseAddress 
 * @param reusePort 
 * @param sendBufferSize 
 * @param sni 
 * @param soLinger 
 * @param ssl 
 * @param tcpCork 
 * @param tcpFastOpen 
 * @param tcpKeepAlive 
 * @param tcpNoDelay 
 * @param tcpQuickAck 
 * @param trafficClass 
 * @param trustStoreOptions 
 * @param useAlpn 
 * @param usePooledBuffers 
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.vertx.ext.shell.term.TelnetTermOptions original] using Vert.x codegen.
 */
fun TelnetTermOptions(
  acceptBacklog: Int? = null,
  charset: String? = null,
  clientAuth: ClientAuth? = null,
  clientAuthRequired: Boolean? = null,
  crlPaths: Iterable<String>? = null,
  crlValues: Iterable<io.vertx.core.buffer.Buffer>? = null,
  enabledCipherSuites: Iterable<String>? = null,
  enabledSecureTransportProtocols: Iterable<String>? = null,
  host: String? = null,
  idleTimeout: Int? = null,
  inBinary: Boolean? = null,
  intputrc: String? = null,
  jdkSslEngineOptions: io.vertx.core.net.JdkSSLEngineOptions? = null,
  keyStoreOptions: io.vertx.core.net.JksOptions? = null,
  logActivity: Boolean? = null,
  openSslEngineOptions: io.vertx.core.net.OpenSSLEngineOptions? = null,
  outBinary: Boolean? = null,
  pemKeyCertOptions: io.vertx.core.net.PemKeyCertOptions? = null,
  pemTrustOptions: io.vertx.core.net.PemTrustOptions? = null,
  pfxKeyCertOptions: io.vertx.core.net.PfxOptions? = null,
  pfxTrustOptions: io.vertx.core.net.PfxOptions? = null,
  port: Int? = null,
  receiveBufferSize: Int? = null,
  reuseAddress: Boolean? = null,
  reusePort: Boolean? = null,
  sendBufferSize: Int? = null,
  sni: Boolean? = null,
  soLinger: Int? = null,
  ssl: Boolean? = null,
  tcpCork: Boolean? = null,
  tcpFastOpen: Boolean? = null,
  tcpKeepAlive: Boolean? = null,
  tcpNoDelay: Boolean? = null,
  tcpQuickAck: Boolean? = null,
  trafficClass: Int? = null,
  trustStoreOptions: io.vertx.core.net.JksOptions? = null,
  useAlpn: Boolean? = null,
  usePooledBuffers: Boolean? = null): TelnetTermOptions = io.vertx.ext.shell.term.TelnetTermOptions().apply {

  if (acceptBacklog != null) {
    this.setAcceptBacklog(acceptBacklog)
  }
  if (charset != null) {
    this.setCharset(charset)
  }
  if (clientAuth != null) {
    this.setClientAuth(clientAuth)
  }
  if (clientAuthRequired != null) {
    this.setClientAuthRequired(clientAuthRequired)
  }
  if (crlPaths != null) {
    for (item in crlPaths) {
      this.addCrlPath(item)
    }
  }
  if (crlValues != null) {
    for (item in crlValues) {
      this.addCrlValue(item)
    }
  }
  if (enabledCipherSuites != null) {
    for (item in enabledCipherSuites) {
      this.addEnabledCipherSuite(item)
    }
  }
  if (enabledSecureTransportProtocols != null) {
    this.setEnabledSecureTransportProtocols(enabledSecureTransportProtocols.toSet())
  }
  if (host != null) {
    this.setHost(host)
  }
  if (idleTimeout != null) {
    this.setIdleTimeout(idleTimeout)
  }
  if (inBinary != null) {
    this.setInBinary(inBinary)
  }
  if (intputrc != null) {
    this.setIntputrc(intputrc)
  }
  if (jdkSslEngineOptions != null) {
    this.setJdkSslEngineOptions(jdkSslEngineOptions)
  }
  if (keyStoreOptions != null) {
    this.setKeyStoreOptions(keyStoreOptions)
  }
  if (logActivity != null) {
    this.setLogActivity(logActivity)
  }
  if (openSslEngineOptions != null) {
    this.setOpenSslEngineOptions(openSslEngineOptions)
  }
  if (outBinary != null) {
    this.setOutBinary(outBinary)
  }
  if (pemKeyCertOptions != null) {
    this.setPemKeyCertOptions(pemKeyCertOptions)
  }
  if (pemTrustOptions != null) {
    this.setPemTrustOptions(pemTrustOptions)
  }
  if (pfxKeyCertOptions != null) {
    this.setPfxKeyCertOptions(pfxKeyCertOptions)
  }
  if (pfxTrustOptions != null) {
    this.setPfxTrustOptions(pfxTrustOptions)
  }
  if (port != null) {
    this.setPort(port)
  }
  if (receiveBufferSize != null) {
    this.setReceiveBufferSize(receiveBufferSize)
  }
  if (reuseAddress != null) {
    this.setReuseAddress(reuseAddress)
  }
  if (reusePort != null) {
    this.setReusePort(reusePort)
  }
  if (sendBufferSize != null) {
    this.setSendBufferSize(sendBufferSize)
  }
  if (sni != null) {
    this.setSni(sni)
  }
  if (soLinger != null) {
    this.setSoLinger(soLinger)
  }
  if (ssl != null) {
    this.setSsl(ssl)
  }
  if (tcpCork != null) {
    this.setTcpCork(tcpCork)
  }
  if (tcpFastOpen != null) {
    this.setTcpFastOpen(tcpFastOpen)
  }
  if (tcpKeepAlive != null) {
    this.setTcpKeepAlive(tcpKeepAlive)
  }
  if (tcpNoDelay != null) {
    this.setTcpNoDelay(tcpNoDelay)
  }
  if (tcpQuickAck != null) {
    this.setTcpQuickAck(tcpQuickAck)
  }
  if (trafficClass != null) {
    this.setTrafficClass(trafficClass)
  }
  if (trustStoreOptions != null) {
    this.setTrustStoreOptions(trustStoreOptions)
  }
  if (useAlpn != null) {
    this.setUseAlpn(useAlpn)
  }
  if (usePooledBuffers != null) {
    this.setUsePooledBuffers(usePooledBuffers)
  }
}

