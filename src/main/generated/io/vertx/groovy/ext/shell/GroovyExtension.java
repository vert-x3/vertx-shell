package io.vertx.groovy.ext.shell;
public class GroovyExtension {
  public static io.vertx.ext.shell.session.Session put(io.vertx.ext.shell.session.Session j_receiver, java.lang.String key, java.lang.Object obj) {
    io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.put(key,
      io.vertx.lang.groovy.ConversionHelper.unwrap(obj)));
    return j_receiver;
  }
  public static <T>java.lang.Object get(io.vertx.ext.shell.session.Session j_receiver, java.lang.String key) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.get(key));
  }
  public static <T>java.lang.Object remove(io.vertx.ext.shell.session.Session j_receiver, java.lang.String key) {
    return io.vertx.lang.groovy.ConversionHelper.wrap(j_receiver.remove(key));
  }
}
