package io.vertx.ext.shell.command.base;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.Shell;
import io.vertx.ext.shell.ShellServer;
import io.vertx.ext.shell.system.ExecStatus;
import io.vertx.ext.shell.system.Job;
import io.vertx.ext.shell.term.Pty;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:emad.albloushi@gmail.com">Emad Alblueshi</a>
 */

@RunWith(VertxUnitRunner.class)
public class DeployVerticleTest {

  static public class SomeVerticle extends AbstractVerticle {}
  Vertx vertx;
  ShellServer server;
  String name;

  @Before
  public void before(TestContext context) throws Exception {
    name = SomeVerticle.class.getName();
    vertx = Vertx.vertx();
    server = ShellServer.create(vertx)
      .registerCommandResolver(new BaseCommandPack(vertx)).listen(context.asyncAssertSuccess());
  }

  @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testDeploy(TestContext context) {
    Async async = context.async();
    Shell shell = server.createShell();
    Pty pty = Pty.create();
    StringBuffer result = new StringBuffer();
    pty.stdoutHandler(result::append);
    Job job = shell.createJob("verticle-deploy " + name)
      .setTty(pty.slave());
    job.statusUpdateHandler(status -> {
      if (status == ExecStatus.TERMINATED) {
        async.complete();
      }
    });
    job.run();
    async.awaitSuccess(5000);
    shell.close();
    context.assertTrue(result.toString().startsWith("Deployed"));
  }

  @Test
  public void testDeployWithOptionsAsEmptyString(TestContext context) {
    Async async = context.async();
    Shell shell = server.createShell();
    Pty pty = Pty.create();
    StringBuffer result = new StringBuffer();
    pty.stdoutHandler(result::append);
    Job job = shell.createJob("verticle-deploy " + name + " ''")
      .setTty(pty.slave());
    job.statusUpdateHandler(status -> {
      if (status == ExecStatus.TERMINATED) {
        async.complete();
      }
    });
    job.run();
    async.awaitSuccess(5000);
    shell.close();
    context.assertTrue(result.toString().startsWith("Deployed"));
  }

  @Test
  public void testDeployWithOptionsAsJsonString(TestContext context) {
    Async async = context.async();
    Shell shell = server.createShell();
    Pty pty = Pty.create();
    StringBuffer result = new StringBuffer();
    pty.stdoutHandler(result::append);
    Job job = shell.createJob("verticle-deploy " + name + " '{\"instances\" : 8}'")
      .setTty(pty.slave());
    job.statusUpdateHandler(status -> {
      if (status == ExecStatus.TERMINATED) {
        async.complete();
      }
    });
    job.run();
    async.awaitSuccess(5000);
    shell.close();
    context.assertTrue(result.toString().startsWith("Deployed"));
  }

  @Test
  public void testDeployWithOptionsAsEmptyJsonString(TestContext context) {
    Async async = context.async();
    Shell shell = server.createShell();
    Pty pty = Pty.create();
    StringBuffer result = new StringBuffer();
    pty.stdoutHandler(result::append);
    Job job = shell.createJob("verticle-deploy " + name + " '{}'")
      .setTty(pty.slave());
    job.statusUpdateHandler(status -> {
      if (status == ExecStatus.TERMINATED) {
        async.complete();
      }
    });
    job.run();
    async.awaitSuccess(5000);
    shell.close();
    context.assertTrue(result.toString().startsWith("Deployed"));
  }

  @Test
  public void testDeployWithOptionsAsInvalidJsonString(TestContext context) {
    Async async = context.async();
    Shell shell = server.createShell();
    Pty pty = Pty.create();
    StringBuffer result = new StringBuffer();
    pty.stdoutHandler(result::append);
    Job job = shell.createJob("verticle-deploy " + name + " '[]'")
      .setTty(pty.slave());
    job.statusUpdateHandler(status -> {
      if (status == ExecStatus.TERMINATED) {
        async.complete();
      }
    });
    job.run();
    async.awaitSuccess(5000);
    shell.close();
    context.assertTrue(result.toString().startsWith("Invalid JSON string"));
  }
}

