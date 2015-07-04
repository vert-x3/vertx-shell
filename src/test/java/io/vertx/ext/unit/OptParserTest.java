package io.vertx.ext.unit;

import io.vertx.ext.shell.getopt.Option;
import io.vertx.ext.shell.getopt.impl.OptParser;
import io.vertx.ext.shell.getopt.impl.OptRequest;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public class OptParserTest {

  @Test
  public void testZeroArgumentOption() {
    OptParser parser = new OptParser(Option.create("f", 0));
    OptRequest request = parser.parse("-f");
    assertEquals(Collections.singletonMap("f", Arrays.<String>asList()), request.getOptions());
    assertEquals(Collections.<String>emptyList(), request.getArguments());
  }

  @Test
  public void testOneArgumentOption() {
    OptParser parser = new OptParser(Option.create("f", 1));
    OptRequest request = parser.parse("-f f_value");
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
    OptParser parser = new OptParser();
    try {
      parser.parse("-f");
      fail();
    } catch (IllegalArgumentException ignore) {
    }
  }

  @Test
  public void testSingleArgument() {
    OptParser parser = new OptParser();
    OptRequest request = parser.parse("arg_value");
    assertEquals(Collections.<String, List<String>>emptyMap(), request.getOptions());
    assertEquals(Arrays.asList("arg_value"), request.getArguments());
  }

  @Test
  public void testOptionAndArgument() {
    OptParser parser = new OptParser(Option.create("f", 1));
    OptRequest request = parser.parse("arg_value");
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
