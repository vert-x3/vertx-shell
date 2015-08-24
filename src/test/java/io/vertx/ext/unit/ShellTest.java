package io.vertx.ext.unit;

import io.termd.core.tty.TtyEvent;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandManager;
import io.vertx.ext.shell.impl.Job;
import io.vertx.ext.shell.impl.JobStatus;
import io.vertx.ext.shell.impl.Shell;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class ShellTest {

  Vertx vertx = Vertx.vertx();

  @Test
  public void testExecuteProcess(TestContext context) {
    CommandManager manager = CommandManager.get(vertx);
    TestTtyConnection conn = new TestTtyConnection();
    Shell shell = new Shell(vertx, conn, manager);
    shell.init();
    context.assertNull(shell.foregroundJob());
    context.assertEquals(Collections.emptyMap(), shell.jobs());
    Async async = context.async();
    manager.registerCommand(Command.create("foo").processHandler(process -> {
      context.assertEquals(1, shell.jobs().size());
      Job job = shell.getJob(1);
      context.assertEquals(job, shell.foregroundJob());
      context.assertEquals("foo", job.line());
      context.assertEquals(JobStatus.RUNNING, job.status());
      async.complete();
    }));
    conn.read("foo\r");
  }

  @Test
  public void testBufferRead(TestContext context) {

  }

  @Test
  public void testSuspendProcess(TestContext context) throws Exception {
    CommandManager manager = CommandManager.get(vertx);
    TestTtyConnection conn = new TestTtyConnection();
    Shell shell = new Shell(vertx, conn, manager);
    shell.init();
    Async async = context.async();
    CountDownLatch latch = new CountDownLatch(1);
    manager.registerCommand(Command.create("foo").processHandler(process -> {
      Job job = shell.getJob(1);
      process.eventHandler("SIGTSTP", v -> {
        context.assertEquals(JobStatus.STOPPED, job.status());
        context.assertNull(shell.foregroundJob());
        async.complete();
      });
      latch.countDown();
    }));
    conn.read("foo\r");
    latch.await(10, TimeUnit.SECONDS);
    conn.sendEvent(TtyEvent.SUSP);
  }

  @Test
  public void testResumeProcessToForeground(TestContext context) throws Exception {
    CommandManager manager = CommandManager.get(vertx);
    TestTtyConnection conn = new TestTtyConnection();
    Shell shell = new Shell(vertx, conn, manager);
    shell.init();
    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(1);
    CountDownLatch latch3 = new CountDownLatch(1);
    CountDownLatch latch4 = new CountDownLatch(1);
    manager.registerCommand(Command.create("foo").processHandler(process -> {
      Job job = shell.getJob(1);
      process.eventHandler("SIGTSTP", v -> {
        context.assertEquals(0L, latch1.getCount());
        context.assertNull(process.stdout());
        latch2.countDown();
      });
      process.eventHandler("SIGCONT", v -> {
        context.assertEquals(0L, latch2.getCount());
        context.assertEquals(JobStatus.RUNNING, job.status());
        context.assertNotNull(process.stdout());
        context.assertEquals(job, shell.foregroundJob());
        conn.out.setLength(0);
        process.stdout().handle("resumed");
        latch3.countDown();
      });
      process.setStdin(txt -> {
        context.assertEquals(0L, latch3.getCount());
        context.assertEquals("hello", txt);
        latch4.countDown();
      });
      latch1.countDown();
    }));
    conn.read("foo\r");
    latch1.await(10, TimeUnit.SECONDS);
    conn.sendEvent(TtyEvent.SUSP);
    latch2.await(10, TimeUnit.SECONDS);
    conn.read("fg\r");
    latch3.await(10, TimeUnit.SECONDS);
    conn.read("hello");
    latch4.await(10, TimeUnit.SECONDS);
    conn.assertWritten("resumed");
  }

  @Test
  public void testResumeProcessToBackground(TestContext context) throws Exception {
    CommandManager manager = CommandManager.get(vertx);
    TestTtyConnection conn = new TestTtyConnection();
    Shell shell = new Shell(vertx, conn, manager);
    shell.init();
    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(1);
    CountDownLatch latch3 = new CountDownLatch(1);
    manager.registerCommand(Command.create("foo").processHandler(process -> {
      Job job = shell.getJob(1);
      process.eventHandler("SIGTSTP", v -> {
        context.assertEquals(0L, latch1.getCount());
        context.assertNull(process.stdout());
        latch2.countDown();
      });
      process.eventHandler("SIGCONT", v -> {
        context.assertEquals(0L, latch2.getCount());
        context.assertEquals(JobStatus.RUNNING, job.status());
        context.assertNotNull(process.stdout());
        context.assertNull(shell.foregroundJob());
        try {
          latch3.await();
        } catch (InterruptedException e) {
          context.fail(e);
        }
        process.stdout().handle("resumed");
      });
      process.setStdin(txt -> {
        context.assertEquals(0L, latch3.getCount());
        context.assertEquals("hello", txt);
        latch3.countDown();
      });
      latch1.countDown();
    }));
    conn.read("foo\r");
    latch1.await(10, TimeUnit.SECONDS);
    conn.sendEvent(TtyEvent.SUSP);
    latch2.await(10, TimeUnit.SECONDS);
    conn.out.setLength(0);
    conn.read("bg\r");
    conn.assertWritten("bg\n% ");
    latch3.countDown();
    conn.read("hello");
    conn.assertWritten("hello");
  }

  @Test
  public void backgroundToForeground(TestContext context) throws Exception {
    CommandManager manager = CommandManager.get(vertx);
    TestTtyConnection conn = new TestTtyConnection();
    Shell shell = new Shell(vertx, conn, manager);
    shell.init();
    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(1);
    CountDownLatch latch3 = new CountDownLatch(1);
    manager.registerCommand(Command.create("foo").processHandler(process -> {
      process.eventHandler("SIGTSTP", v -> {
        latch2.countDown();
      });
      process.eventHandler("SIGCONT", v -> {
        latch3.countDown();
      });
      process.setStdin(txt -> {
        context.assertEquals(0L, latch3.getCount());
        context.assertEquals("hello", txt);
        latch3.countDown();
      });
      latch1.countDown();
    }));
    conn.read("foo\r");
    latch1.await(10, TimeUnit.SECONDS);
    conn.sendEvent(TtyEvent.SUSP);
    latch2.await(10, TimeUnit.SECONDS);
    conn.read("bg\r");
    context.assertNull(shell.foregroundJob());
    conn.read("fg\r");
    context.assertNotNull(shell.foregroundJob());
    context.assertEquals(shell.getJob(1), shell.foregroundJob());
  }
}
