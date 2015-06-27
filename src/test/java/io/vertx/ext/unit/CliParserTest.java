package io.vertx.ext.unit;

import io.vertx.ext.shell.command.Option;
import io.vertx.ext.shell.command.impl.CliParser;
import io.vertx.ext.shell.command.impl.CliRequest;
import io.vertx.ext.shell.command.impl.CommandImpl;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class CliParserTest {

  @Test
  public void testZeroArgumentOption() {
    CommandImpl command = new CommandImpl("").option(Option.create("f", 0));
    CliParser parser = new CliParser(command);
    CliRequest request = parser.parse("-f");
    assertEquals(Collections.singletonMap("f", Arrays.<String>asList()), request.getOptions());
    assertEquals(Collections.<String>emptyList(), request.getArguments());
  }

  @Test
  public void testOneArgumentOption() {
    CommandImpl command = new CommandImpl("").option(Option.create("f", 1));
    CliParser parser = new CliParser(command);
    CliRequest request = parser.parse("-f f_value");
    assertEquals(Collections.singletonMap("f", Arrays.asList("f_value")), request.getOptions());
    assertEquals(Collections.<String>emptyList(), request.getArguments());
    try {
      parser.parse("-f");
      fail();
    } catch (IllegalArgumentException ignore) {
    }
  }

  @Test
  public void testIllegalOption() {
    CommandImpl command = new CommandImpl("");
    CliParser parser = new CliParser(command);
    try {
      parser.parse("-f");
      fail();
    } catch (IllegalArgumentException ignore) {
    }
  }

  @Test
  public void testSingleArgument() {
    CommandImpl command = new CommandImpl("");
    CliParser parser = new CliParser(command);
    CliRequest request = parser.parse("arg_value");
    assertEquals(Collections.<String, List<String>>emptyMap(), request.getOptions());
    assertEquals(Arrays.asList("arg_value"), request.getArguments());
  }

  @Test
  public void testOptionAndArgument() {
    CommandImpl command = new CommandImpl("").option(Option.create("f", 1));
    CliParser parser = new CliParser(command);
    CliRequest request = parser.parse("arg_value");
    assertEquals(Collections.<String, List<String>>emptyMap(), request.getOptions());
    assertEquals(Arrays.asList("arg_value"), request.getArguments());
    request = parser.parse("-f f_value");
    assertEquals(Collections.singletonMap("f", Arrays.asList("f_value")), request.getOptions());
    assertEquals(Collections.<String>emptyList(), request.getArguments());
    request = parser.parse("-f f_value arg_value");
    assertEquals(Collections.singletonMap("f", Arrays.asList("f_value")), request.getOptions());
    assertEquals(Arrays.asList("arg_value"), request.getArguments());
  }
}
