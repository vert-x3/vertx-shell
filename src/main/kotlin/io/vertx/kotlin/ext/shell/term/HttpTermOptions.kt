package io.vertx.kotlin.ext.shell.term

import io.vertx.ext.shell.term.HttpTermOptions
import io.vertx.core.http.ClientAuth
import io.vertx.core.http.Http2Settings
import io.vertx.core.http.HttpVersion
import io.vertx.core.net.JdkSSLEngineOptions
import io.vertx.core.net.JksOptions
import io.vertx.core.net.OpenSSLEngineOptions
import io.vertx.core.net.PemKeyCertOptions
import io.vertx.core.net.PemTrustOptions
import io.vertx.core.net.PfxOptions
import io.vertx.ext.auth.AuthOptions
import io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions

/**
 * A function providing a DSL for building [io.vertx.ext.shell.term.HttpTermOptions] objects.
 *
 * The web term configuration options.
 *
 * @param acceptBacklog 
 * @param acceptUnmaskedFrames 
 * @param alpnVersions 
 * @param charset  Set the charset used for encoding / decoding text data from/to SockJS
 * @param clientAuth 
 * @param clientAuthRequired 
 * @param compressionLevel 
 * @param compressionSupported 
 * @param crlPaths 
 * @param crlValues 
 * @param decoderInitialBufferSize 
 * @param decompressionSupported 
 * @param enabledCipherSuites 
 * @param enabledSecureTransportProtocols 
 * @param handle100ContinueAutomatically 
 * @param host 
 * @param http2ConnectionWindowSize 
 * @param idleTimeout 
 * @param initialSettings 
 * @param intputrc  The path of the <i>inputrc</i> config.
 * @param jdkSslEngineOptions 
 * @param keyStoreOptions 
 * @param logActivity 
 * @param maxChunkSize 
 * @param maxHeaderSize 
 * @param maxInitialLineLength 
 * @param maxWebsocketFrameSize 
 * @param maxWebsocketMessageSize 
 * @param openSslEngineOptions 
 * @param pemKeyCertOptions 
 * @param pemTrustOptions 
 * @param pfxKeyCertOptions 
 * @param pfxTrustOptions 
 * @param port 
 * @param receiveBufferSize 
 * @param reuseAddress 
 * @param reusePort 
 * @param sendBufferSize 
 * @param shellHtmlResource  Set <code>shell.html</code> resource to use.
 * @param sni 
 * @param soLinger 
 * @param sockJSHandlerOptions  The SockJS handler options.
 * @param sockJSPath  Configure the SockJS path, the default value is <code>/term/<star></code>.
 * @param ssl 
 * @param tcpCork 
 * @param tcpFastOpen 
 * @param tcpKeepAlive 
 * @param tcpNoDelay 
 * @param tcpQuickAck 
 * @param termJsResource  Set <code>term.js</code> resource to use.
 * @param trafficClass 
 * @param trustStoreOptions 
 * @param useAlpn 
 * @param usePooledBuffers 
 * @param vertsShellJsResource  Set <code>vertxshell.js</code> resource to use.
 * @param websocketSubProtocols 
 *
 * <p/>
 * NOTE: This function has been automatically generated from the [io.vertx.ext.shell.term.HttpTermOptions original] using Vert.x codegen.
 */
fun HttpTermOptions(
  acceptBacklog: Int? = null,
  acceptUnmaskedFrames: Boolean? = null,
  alpnVersions: Iterable<HttpVersion>? = null,
  charset: String? = null,
  clientAuth: ClientAuth? = null,
  clientAuthRequired: Boolean? = null,
  compressionLevel: Int? = null,
  compressionSupported: Boolean? = null,
  crlPaths: Iterable<String>? = null,
  crlValues: Iterable<io.vertx.core.buffer.Buffer>? = null,
  decoderInitialBufferSize: Int? = null,
  decompressionSupported: Boolean? = null,
  enabledCipherSuites: Iterable<String>? = null,
  enabledSecureTransportProtocols: Iterable<String>? = null,
  handle100ContinueAutomatically: Boolean? = null,
  host: String? = null,
  http2ConnectionWindowSize: Int? = null,
  idleTimeout: Int? = null,
  initialSettings: io.vertx.core.http.Http2Settings? = null,
  intputrc: String? = null,
  jdkSslEngineOptions: io.vertx.core.net.JdkSSLEngineOptions? = null,
  keyStoreOptions: io.vertx.core.net.JksOptions? = null,
  logActivity: Boolean? = null,
  maxChunkSize: Int? = null,
  maxHeaderSize: Int? = null,
  maxInitialLineLength: Int? = null,
  maxWebsocketFrameSize: Int? = null,
  maxWebsocketMessageSize: Int? = null,
  openSslEngineOptions: io.vertx.core.net.OpenSSLEngineOptions? = null,
  pemKeyCertOptions: io.vertx.core.net.PemKeyCertOptions? = null,
  pemTrustOptions: io.vertx.core.net.PemTrustOptions? = null,
  pfxKeyCertOptions: io.vertx.core.net.PfxOptions? = null,
  pfxTrustOptions: io.vertx.core.net.PfxOptions? = null,
  port: Int? = null,
  receiveBufferSize: Int? = null,
  reuseAddress: Boolean? = null,
  reusePort: Boolean? = null,
  sendBufferSize: Int? = null,
  shellHtmlResource: io.vertx.core.buffer.Buffer? = null,
  sni: Boolean? = null,
  soLinger: Int? = null,
  sockJSHandlerOptions: io.vertx.ext.web.handler.sockjs.SockJSHandlerOptions? = null,
  sockJSPath: String? = null,
  ssl: Boolean? = null,
  tcpCork: Boolean? = null,
  tcpFastOpen: Boolean? = null,
  tcpKeepAlive: Boolean? = null,
  tcpNoDelay: Boolean? = null,
  tcpQuickAck: Boolean? = null,
  termJsResource: io.vertx.core.buffer.Buffer? = null,
  trafficClass: Int? = null,
  trustStoreOptions: io.vertx.core.net.JksOptions? = null,
  useAlpn: Boolean? = null,
  usePooledBuffers: Boolean? = null,
  vertsShellJsResource: io.vertx.core.buffer.Buffer? = null,
  websocketSubProtocols: String? = null): HttpTermOptions = io.vertx.ext.shell.term.HttpTermOptions().apply {

  if (acceptBacklog != null) {
    this.setAcceptBacklog(acceptBacklog)
  }
  if (acceptUnmaskedFrames != null) {
    this.setAcceptUnmaskedFrames(acceptUnmaskedFrames)
  }
  if (alpnVersions != null) {
    this.setAlpnVersions(alpnVersions.toList())
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
  if (compressionLevel != null) {
    this.setCompressionLevel(compressionLevel)
  }
  if (compressionSupported != null) {
    this.setCompressionSupported(compressionSupported)
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
  if (decoderInitialBufferSize != null) {
    this.setDecoderInitialBufferSize(decoderInitialBufferSize)
  }
  if (decompressionSupported != null) {
    this.setDecompressionSupported(decompressionSupported)
  }
  if (enabledCipherSuites != null) {
    for (item in enabledCipherSuites) {
      this.addEnabledCipherSuite(item)
    }
  }
  if (enabledSecureTransportProtocols != null) {
    this.setEnabledSecureTransportProtocols(enabledSecureTransportProtocols.toSet())
  }
  if (handle100ContinueAutomatically != null) {
    this.setHandle100ContinueAutomatically(handle100ContinueAutomatically)
  }
  if (host != null) {
    this.setHost(host)
  }
  if (http2ConnectionWindowSize != null) {
    this.setHttp2ConnectionWindowSize(http2ConnectionWindowSize)
  }
  if (idleTimeout != null) {
    this.setIdleTimeout(idleTimeout)
  }
  if (initialSettings != null) {
    this.setInitialSettings(initialSettings)
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
  if (maxChunkSize != null) {
    this.setMaxChunkSize(maxChunkSize)
  }
  if (maxHeaderSize != null) {
    this.setMaxHeaderSize(maxHeaderSize)
  }
  if (maxInitialLineLength != null) {
    this.setMaxInitialLineLength(maxInitialLineLength)
  }
  if (maxWebsocketFrameSize != null) {
    this.setMaxWebsocketFrameSize(maxWebsocketFrameSize)
  }
  if (maxWebsocketMessageSize != null) {
    this.setMaxWebsocketMessageSize(maxWebsocketMessageSize)
  }
  if (openSslEngineOptions != null) {
    this.setOpenSslEngineOptions(openSslEngineOptions)
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
  if (shellHtmlResource != null) {
    this.setShellHtmlResource(shellHtmlResource)
  }
  if (sni != null) {
    this.setSni(sni)
  }
  if (soLinger != null) {
    this.setSoLinger(soLinger)
  }
  if (sockJSHandlerOptions != null) {
    this.setSockJSHandlerOptions(sockJSHandlerOptions)
  }
  if (sockJSPath != null) {
    this.setSockJSPath(sockJSPath)
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
  if (termJsResource != null) {
    this.setTermJsResource(termJsResource)
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
  if (vertsShellJsResource != null) {
    this.setVertsShellJsResource(vertsShellJsResource)
  }
  if (websocketSubProtocols != null) {
    this.setWebsocketSubProtocols(websocketSubProtocols)
  }
}

