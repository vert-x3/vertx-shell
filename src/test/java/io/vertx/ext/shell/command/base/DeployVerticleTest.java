package io.vertx.ext.shell.command.base;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
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

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author <a href="mailto:emad.albloushi@gmail.com">Emad Alblueshi</a>
 */

@RunWith(VertxUnitRunner.class)
public class DeployVerticleTest {

  Vertx vertx;
  ShellServer server;
  static AtomicReference<Context> ctx;

  static public class SomeVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
      ctx.set(this.vertx.getOrCreateContext());
    }
  }

  @Before
  public void before(TestContext context) throws Exception {
    ctx = new AtomicReference<>(null);
    vertx = Vertx.vertx();
    server = ShellServer.create(vertx)
      .registerCommandResolver(new BaseCommandPack(vertx));
    server.listen()
      .onComplete(context.asyncAssertSuccess());
  }

  @After
  public void after(TestContext context) {
    vertx.close()
      .onComplete(context.asyncAssertSuccess());
  }

  @Test
  public void testDeploy(TestContext context) {
    String cmd = "verticle-deploy io.vertx.ext.shell.command.base.DeployVerticleTest$SomeVerticle";
    String result = testDeployCmd(context, cmd);
    context.assertNotNull(ctx.get());
    context.assertEquals(result, "Deployed " + ctx.get().deploymentID());
    context.assertEquals(1, ctx.get().getInstanceCount());
  }

  @Test
  public void testDeployWithOptionsAsEmptyString(TestContext context) {
    String cmd = "verticle-deploy io.vertx.ext.shell.command.base.DeployVerticleTest$SomeVerticle ''";
    String result = testDeployCmd(context, cmd);
    context.assertNotNull(ctx.get());
    context.assertEquals(result, "Deployed " + ctx.get().deploymentID());
    context.assertEquals(1, ctx.get().getInstanceCount());
  }

  @Test
  public void testDeployWithOptionsAsJsonInstance(TestContext context) {
    String cmd =
      "verticle-deploy io.vertx.ext.shell.command.base.DeployVerticleTest$SomeVerticle '{\"instances\" : 8}'";
    String result = testDeployCmd(context, cmd);
    context.assertNotNull(ctx.get());
    context.assertEquals(result, "Deployed " + ctx.get().deploymentID());
    context.assertEquals(8, ctx.get().getInstanceCount());
  }

  @Test
  public void testDeployWithOptionsAsJsonConfig(TestContext context) {
    String cmd =
      "verticle-deploy io.vertx.ext.shell.command.base.DeployVerticleTest$SomeVerticle '{\"config\":{\"ok\":true}}'";
    String result = testDeployCmd(context, cmd);
    context.assertNotNull(ctx.get());
    context.assertEquals(result, "Deployed " + ctx.get().deploymentID());
    context.assertEquals(1, ctx.get().getInstanceCount());
    context.assertNotNull(ctx.get().config());
    context.assertTrue(ctx.get().config().containsKey("ok"));
    context.assertEquals(true, ctx.get().config().getBoolean("ok"));
  }

  @Test
  public void testDeployWithOptionsAsEmptyJsonString(TestContext context) {
    String cmd = "verticle-deploy io.vertx.ext.shell.command.base.DeployVerticleTest$SomeVerticle '{}'";
    String result = testDeployCmd(context, cmd);
    context.assertNotNull(ctx.get());
    context.assertEquals(result, "Deployed " + ctx.get().deploymentID());
    context.assertEquals(1, ctx.get().getInstanceCount());
  }

  @Test
  public void testDeployWithOptionsAsInvalidJsonString(TestContext context) {
    String cmd = "verticle-deploy io.vertx.ext.shell.command.base.DeployVerticleTest$SomeVerticle '{'";
    String result = testDeployCmd(context, cmd);
    String msg =
      "Could not deploy io.vertx.ext.shell.command.base.DeployVerticleTest$SomeVerticle with deployment options";
    context.assertNull(ctx.get());
    context.assertTrue(result.startsWith(msg));
  }

  private String testDeployCmd(TestContext context, String cmd) {
    Async async = context.async();
    Shell shell = server.createShell();
    Pty pty = Pty.create();
    StringBuilder result = new StringBuilder();
    pty.stdoutHandler(result::append);
    Job job = shell.createJob(cmd)
      .setTty(pty.slave());
    job.statusUpdateHandler(status -> {
      if (status == ExecStatus.TERMINATED) {
        async.complete();
      }
    });
    job.run();
    async.awaitSuccess(5000);
    shell.close();
    return result.toString().trim();
  }
}

