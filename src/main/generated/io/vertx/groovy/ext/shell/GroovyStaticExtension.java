package io.vertx.groovy.ext.shell;
public class GroovyStaticExtension {
  public static io.vertx.ext.shell.ShellServer create(io.vertx.ext.shell.ShellServer j_receiver, io.vertx.core.Vertx vertx, java.util.Map<String, Object> options) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(io.vertx.ext.shell.ShellServer.create(vertx,
      options != null ? new io.vertx.ext.shell.ShellServerOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null));
  }
  public static io.vertx.ext.shell.ShellService create(io.vertx.ext.shell.ShellService j_receiver, io.vertx.core.Vertx vertx, java.util.Map<String, Object> options) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(io.vertx.ext.shell.ShellService.create(vertx,
      options != null ? new io.vertx.ext.shell.ShellServiceOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null));
  }
  public static io.vertx.ext.shell.term.TermServer createSSHTermServer(io.vertx.ext.shell.term.TermServer j_receiver, io.vertx.core.Vertx vertx, java.util.Map<String, Object> options) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(io.vertx.ext.shell.term.TermServer.createSSHTermServer(vertx,
      options != null ? new io.vertx.ext.shell.term.SSHTermOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null));
  }
  public static io.vertx.ext.shell.term.TermServer createTelnetTermServer(io.vertx.ext.shell.term.TermServer j_receiver, io.vertx.core.Vertx vertx, java.util.Map<String, Object> options) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(io.vertx.ext.shell.term.TermServer.createTelnetTermServer(vertx,
      options != null ? new io.vertx.ext.shell.term.TelnetTermOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null));
  }
  public static io.vertx.ext.shell.term.TermServer createHttpTermServer(io.vertx.ext.shell.term.TermServer j_receiver, io.vertx.core.Vertx vertx, java.util.Map<String, Object> options) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(io.vertx.ext.shell.term.TermServer.createHttpTermServer(vertx,
      options != null ? new io.vertx.ext.shell.term.HttpTermOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null));
  }
  public static io.vertx.ext.shell.term.TermServer createHttpTermServer(io.vertx.ext.shell.term.TermServer j_receiver, io.vertx.core.Vertx vertx, io.vertx.ext.web.Router router, java.util.Map<String, Object> options) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(io.vertx.ext.shell.term.TermServer.createHttpTermServer(vertx,
      router,
      options != null ? new io.vertx.ext.shell.term.HttpTermOptions(io.vertx.lang.groovy.ConversionHelper.toJsonObject(options)) : null));
  }
}
