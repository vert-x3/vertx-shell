package io.vertx.ext.unit;

import io.vertx.core.Vertx;
import io.vertx.ext.shell.cli.CliToken;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.Command;
import io.vertx.ext.shell.command.CommandManager;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@RunWith(VertxUnitRunner.class)
public class CompletionTest {

  @Rule
  public final RunTestOnContext rule = new RunTestOnContext();

  private CommandManager mgr;

  @Before
  public void before() {
    mgr = CommandManager.get(rule.vertx());
    mgr.registerCommand(Command.command("foo").processHandler(proc -> {}).completeHandler(
        completion -> {
          completion.complete("completed_by_foo", false);
        }
    ));
    mgr.registerCommand(Command.command("bar").processHandler(proc -> {}));
    mgr.registerCommand(Command.command("baz").processHandler(proc -> {}));
  }

  @Test
  public void testEnumerateCommands(TestContext context) {
    Async async = context.async();
    mgr.complete(new TestCompletion(context, "") {
      @Override
      public void complete(List<String> candidates) {
        context.assertEquals(Arrays.asList("bar", "baz", "foo"), candidates.stream().sorted().collect(Collectors.toList()));
        async.complete();
      }
    });
  }

  @Test
  public void testSingleCommand(TestContext context) {
    Async async = context.async();
    mgr.complete(new TestCompletion(context, "f") {
      @Override
      public void complete(String value, boolean terminal) {
        context.assertTrue(terminal);
        context.assertEquals("oo", value);
        async.complete();
      }
    });
  }

  @Test
  public void testExactCommand(TestContext context) {
    Async async = context.async();
    mgr.complete(new TestCompletion(context, "foo") {
      @Override
      public void complete(String value, boolean terminal) {
        context.assertTrue(terminal);
        context.assertEquals("", value);
        async.complete();
      }
    });
  }

  @Test
  public void testAfterExactCommand(TestContext context) {
    Async async = context.async();
    mgr.complete(new TestCompletion(context, "foo ") {
      @Override
      public void complete(String value, boolean terminal) {
        context.assertFalse(terminal);
        context.assertEquals("completed_by_foo", value);
        async.complete();
      }
    });
  }

  @Test
  public void testNotFoundCommand(TestContext context) {
    Async async = context.async();
    mgr.complete(new TestCompletion(context, "not_found") {
      @Override
      public void complete(List<String> candidates) {
        context.assertEquals(Collections.emptyList(), candidates);
        async.complete();
      }
    });
  }

  @Test
  public void testAfterNotFoundCommand(TestContext context) {
    Async async = context.async();
    mgr.complete(new TestCompletion(context, "not_found ") {
      @Override
      public void complete(List<String> candidates) {
        context.assertEquals(Collections.emptyList(), candidates);
        async.complete();
      }
    });
  }

  @Test
  public void testCommandWithCommonPrefix(TestContext context) {
    Async async = context.async();
    mgr.complete(new TestCompletion(context, "b") {
      @Override
      public void complete(String value, boolean terminal) {
        context.assertFalse(terminal);
        context.assertEquals("a", value);
        async.complete();
      }
    });
  }

  @Test
  public void testCommands(TestContext context) {
    Async async = context.async();
    mgr.complete(new TestCompletion(context, "ba") {
      @Override
      public void complete(List<String> candidates) {
        context.assertEquals(Arrays.asList("bar", "baz"), candidates.stream().sorted().collect(Collectors.toList()));
        async.complete();
      }
    });
  }

  class TestCompletion implements Completion {
    final TestContext context;
    final String line;
    public TestCompletion(TestContext context, String line) {
      this.line = line;
      this.context = context;
    }
    @Override
    public Vertx vertx() {
      return rule.vertx();
    }
    @Override
    public String line() {
      return line;
    }
    @Override
    public List<CliToken> lineTokens() {
      return CliToken.tokenize(line);
    }
    @Override
    public void complete(List<String> candidates) {
      context.fail();
    }
    @Override
    public void complete(String value, boolean terminal) {
      context.fail();
    }
  }
}
