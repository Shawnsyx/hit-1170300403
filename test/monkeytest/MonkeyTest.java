package monkeytest;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import monkeys.Monkey;
import monkeys.Monkey.Direction;

/**
 * This is the test class for Monkey.
 * Basically, several observer methods will be tested here.
 */
public class MonkeyTest {

  // Testing Strategy: test the observer methods.
  @Test
  public void monkeyTest() {
    Monkey m1 = new Monkey(1, Direction.RtoL, 5);
    assertEquals("ID -> 1", 1,m1.getID());
    assertEquals("Direction: RtoL",Direction.RtoL,m1.getDirection());
    assertEquals("velocity",5,m1.getVelocity());
  }
}
