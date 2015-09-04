package io.vertx.ext.unit;

import io.termd.core.tty.TtyEvent;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.registry.CommandRegistry;
import io.vertx.ext.shell.impl.Job;
import io.vertx.ext.shell.impl.JobStatus;
import io.vertx.ext.shell.impl.Shell;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class ShellTest {

  Vertx vertx = Vertx.vertx();

  @Test
  public void testVertx(TestContext context) {
    CommandRegistry manager = CommandRegistry.get(vertx);
    TestTtyConnection conn = new TestTtyConnection();
    Shell shell = new Shell(vertx, conn, manager);
    shell.init();
    Async async = context.async();
    manager.registerCommand(Command.command("foo").processHandler(process -> {
      context.assertEquals(vertx, process.vertx());
      async.complete();
    }));
    conn.read("foo\r");
  }

  @Test
  public void testExecuteProcess(TestContext context) {
    CommandRegistry manager = CommandRegistry.get(vertx);
    TestTtyConnection conn = new TestTtyConnection();
    Shell shell = new Shell(vertx, conn, manager);
    shell.init();
    context.assertNull(shell.foregroundJob());
    context.assertEquals(Collections.emptyMap(), shell.jobs());
    Async async = context.async();
    manager.registerCommand(Command.command("foo").processHandler(process -> {
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
  public void testHandleReadlineBuffered(TestContext context) {
    CommandRegistry manager = CommandRegistry.get(vertx);
    TestTtyConnection conn = new TestTtyConnection();
    Shell shell = new Shell(vertx, conn, manager);
    shell.init();
    Async async = context.async();
    manager.registerCommand(Command.command("_not_consumed").processHandler(process -> {
      async.complete();
    }));
    manager.registerCommand(Command.command("read").processHandler(process -> {
      StringBuilder buffer = new StringBuilder();
      process.setStdin(line -> {
        buffer.append(line);
        if (buffer.toString().equals("the_line")) {
          process.end();
        }
      });
    }), context.asyncAssertSuccess(v -> {
      conn.read("read\rthe_line_not_consumed\r");
    }));
  }

  @Test
  public void testExecuteReadlineBuffered(TestContext context) {
    CommandRegistry manager = CommandRegistry.get(vertx);
    TestTtyConnection conn = new TestTtyConnection();
    Shell shell = new Shell(vertx, conn, manager);
    shell.init();
    Async async = context.async();
    AtomicInteger count = new AtomicInteger();
    manager.registerCommand(Command.command("read").processHandler(process -> {
      if (count.incrementAndGet() == 2) {
        async.complete();
      }
      process.end(0);
    }), context.asyncAssertSuccess(v -> {
      conn.read("read\rread\r");
    }));
  }

  @Test
  public void testSuspendProcess(TestContext context) throws Exception {
    CommandRegistry manager = CommandRegistry.get(vertx);
    TestTtyConnection conn = new TestTtyConnection();
    Shell shell = new Shell(vertx, conn, manager);
    shell.init();
    Async async = context.async();
    CountDownLatch latch = new CountDownLatch(1);
    manager.registerCommand(Command.command("foo").processHandler(process -> {
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
  public void testSuspendedProcessDisconnectedFromTty(TestContext context) throws Exception {
    CommandRegistry manager = CommandRegistry.get(vertx);
    TestTtyConnection conn = new TestTtyConnection();
    Shell shell = new Shell(vertx, conn, manager);
    shell.init();
    Async async = context.async();
    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(1);
    CountDownLatch latch3 = new CountDownLatch(1);
    manager.registerCommand(Command.command("read").processHandler(process -> {
      process.setStdin(line -> {
        context.fail("Should not process line " + line);
      });
      process.eventHandler("SIGTSTP", v -> {
        context.assertNull(process.stdout());
        latch2.countDown();
      });
      latch1.countDown();
    }));
    manager.registerCommand(Command.command("wait").processHandler(process -> {
      // Do nothing, this command is used to escape from readline and make
      // sure that the read data is not sent to the stopped command
      latch3.countDown();
      process.eventHandler("SIGTSTP", v -> {
        process.end(0);
      });
    }));
    manager.registerCommand(Command.command("end").processHandler(process -> {
      async.complete();
    }));
    conn.read("read\r");
    latch1.await(10, TimeUnit.SECONDS);
    conn.sendEvent(TtyEvent.SUSP);
    latch2.await(10, TimeUnit.SECONDS);
    conn.read("wait\r");
    latch3.await(10, TimeUnit.SECONDS);
    conn.sendEvent(TtyEvent.SUSP);
    conn.read("end\r");
  }

  @Test
  public void testResumeProcessToForeground(TestContext context) throws Exception {
    CommandRegistry manager = CommandRegistry.get(vertx);
    TestTtyConnection conn = new TestTtyConnection();
    Shell shell = new Shell(vertx, conn, manager);
    shell.init();
    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(1);
    CountDownLatch latch3 = new CountDownLatch(1);
    CountDownLatch latch4 = new CountDownLatch(1);
    manager.registerCommand(Command.command("foo").processHandler(process -> {
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
    CommandRegistry manager = CommandRegistry.get(vertx);
    TestTtyConnection conn = new TestTtyConnection();
    Shell shell = new Shell(vertx, conn, manager);
    shell.init();
    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(1);
    CountDownLatch latch3 = new CountDownLatch(1);
    manager.registerCommand(Command.command("foo").processHandler(process -> {
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
    conn.assertWritten("resumed");
    conn.read("hello");
    conn.assertWritten("hello");
  }

  @Test
  public void backgroundToForeground(TestContext context) throws Exception {
    CommandRegistry manager = CommandRegistry.get(vertx);
    TestTtyConnection conn = new TestTtyConnection();
    Shell shell = new Shell(vertx, conn, manager);
    shell.init();
    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(1);
    CountDownLatch latch3 = new CountDownLatch(1);
    manager.registerCommand(Command.command("foo").processHandler(process -> {
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

  @Test
  public void testExecuteBufferedCommand(TestContext context) throws Exception {
    CommandRegistry manager = CommandRegistry.get(vertx);
    TestTtyConnection conn = new TestTtyConnection();
    Shell shell = new Shell(vertx, conn, manager);
    shell.init();
    CountDownLatch latch = new CountDownLatch(1);
    Async async = context.async();
    manager.registerCommand(Command.command("foo").processHandler(process -> {
      context.assertEquals(null, conn.checkWritten("% foo\n"));
      conn.read("bar");
      context.assertNull(conn.checkWritten("bar"));
      process.end();
      latch.countDown();
    }));
    manager.registerCommand(Command.command("bar").processHandler(process -> {
      context.assertEquals(null, conn.checkWritten("% bar\n"));
      async.complete();
    }));
    conn.read("foo\r");
    latch.await(10, TimeUnit.SECONDS);
    conn.read("\r");
  }

  @Test
  public void testEchoCharsDuringExecute(TestContext context) throws Exception {
    CommandRegistry manager = CommandRegistry.get(vertx);
    TestTtyConnection conn = new TestTtyConnection();
    Shell shell = new Shell(vertx, conn, manager);
    shell.init();
    Async async = context.async();
    manager.registerCommand(Command.command("foo").processHandler(process -> {
      context.assertEquals(null, conn.checkWritten("% foo\n"));
      conn.read("\u0007");
      context.assertNull(conn.checkWritten("^G"));
      conn.read("A");
      context.assertNull(conn.checkWritten("A"));
      conn.read("\r");
      context.assertNull(conn.checkWritten("\n"));
      conn.read("\003");
      context.assertNull(conn.checkWritten("^C"));
      conn.read("\004");
      context.assertNull(conn.checkWritten("^D"));
      async.complete();
    }));
    conn.read("foo\r");
  }

  @Test
  public void testExit(TestContext context) throws Exception {
    for (String cmd : Arrays.asList("exit", "logout")) {
      CommandRegistry manager = CommandRegistry.get(vertx);
      TestTtyConnection conn = new TestTtyConnection();
      Shell shell = new Shell(vertx, conn, manager);
      shell.init();
      conn.read(cmd + "\r");
      context.assertTrue(conn.closed);
    }
  }

}
