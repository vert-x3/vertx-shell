package io.vertx.ext.shell;

import io.vertx.core.Vertx;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandResolver;
import io.vertx.ext.shell.support.TestCommands;
import io.vertx.ext.shell.support.TestTermServer;
import io.vertx.ext.shell.support.TestTtyConnection;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class ShellServerPromptTest {
  private Vertx vertx;
  private TestCommands commands;

  @Before
  public void before() {
    vertx = Vertx.vertx();
    commands = new TestCommands(vertx);
    //server = ShellServer.create(vertx).registerCommandResolver(commands);
  }

   @After
  public void after(TestContext context) {
    vertx.close(context.asyncAssertSuccess());
  }

  @Test
  public void testPrompt(TestContext context) {
    commands = new TestCommands(vertx);
    ShellServer server =  ShellServer.create(vertx, new ShellServerOptions()
      .setWelcomeMessage("")
      .setSessionTimeout(100)
      .setReaperInterval(100));
    server.shellHandler(shell -> shell.setPrompt(s -> "FOOPROMPT"));
    TestTermServer termServer = new TestTermServer(vertx);
    server.registerTermServer(termServer);
    server.
      registerCommandResolver(CommandResolver.baseCommands(vertx)).
      registerCommandResolver(commands).
      listen(context.asyncAssertSuccess());

    TestTtyConnection conn = termServer.openConnection();

    Async async = context.async();
    commands.add(CommandBuilder.command("foo").processHandler(process -> {
      context.assertEquals(null, conn.checkWritten("FOOPROMPTfoo\n"));
      process.stdinHandler(cp -> context.fail());
      process.endHandler(v -> async.complete()
      );
      process.end();

    }));
    conn.read("foo\r");
    async.awaitSuccess(5000);
  }

}
